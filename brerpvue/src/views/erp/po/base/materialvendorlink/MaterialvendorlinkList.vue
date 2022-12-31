<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">

    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域 end -->

    <!-- 操作按钮区域 begin -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('货源清单')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete"/>
            <span>删除</span>
          </a-menu-item>
        </a-menu>
        <a-button>
          <span>批量操作</span>
          <a-icon type="down"/>
        </a-button>
      </a-dropdown>
    </div>
    <!-- 操作按钮区域 end -->

    <!-- table区域 begin -->
    <div>

      <a-alert type="info" showIcon style="margin-bottom: 16px;">
        <template slot="message">
          <span>已选择</span>
          <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
          <span>项</span>
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </template>
      </a-alert>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :loading="loading"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :expandedRowKeys="expandedRowKeys"
        :rowSelection="{selectedRowKeys, onChange: onSelectChange}"
        @expand="handleExpand"
        @change="handleTableChange"
      >

        <!-- 内嵌table区域 begin -->
        <template slot="expandedRowRender" slot-scope="record">
          <a-tabs tabPosition="top">
            <a-tab-pane tab="货源清单分录" key="materialvendorlinkitem" forceRender>
              <materialvendorlinkitem-sub-table :record="record"/>
            </a-tab-pane>
          </a-tabs>
        </template>
        <!-- 内嵌table区域 end -->

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

        <template slot="imgSlot" slot-scope="text,record">
          <div style="font-size: 12px;font-style: italic;">
            <span v-if="!text">无图片</span>
            <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;"/>
          </div>
        </template>


        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            ghost
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            <span>下载</span>
          </a-button>
        </template>

        <template slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>

        </template>

      </a-table>
    </div>
    <!-- table区域 end -->

    <!-- 表单区域 -->
    <materialvendorlink-modal ref="modalForm" @ok="modalFormOk"/>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import MaterialvendorlinkModal from './modules/MaterialvendorlinkModal'
  import MaterialvendorlinkitemSubTable from './subTables/MaterialvendorlinkitemSubTable'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'

  export default {
    name: 'MaterialvendorlinkList',
    mixins: [JeecgListMixin],
    components: {
      MaterialvendorlinkModal,
      MaterialvendorlinkitemSubTable,
    },
    data() {
      return {
        description: '货源清单列表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            key: 'rowIndex',
            width: 60,
            align: 'center',
            customRender: (t, r, index) => parseInt(index) + 1
          },
          {
            title: '清单编码',
            align: 'center',
            dataIndex: 'code',
          },
          {
            title: '清单名称',
            align: 'center',
            dataIndex: 'name',
          },
          {
            title: '物料',
            align: 'center',
            dataIndex: 'materialcode',
          },
          {
            title: '物料',
            align: 'center',
            dataIndex: 'materialname',
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'remark',
          },
          {
            title: '有效',
            align: 'center',
            dataIndex: 'canenable',
            customRender: (text) => (!text ? "" : (text == "Y" ? "是" : "否"))
          },
          {
            title: '有效期从',
            align: 'center',
            dataIndex: 'fromdate',
          },
          {
            title: '有效期止',
            align: 'center',
            dataIndex: 'enddate',
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            width:147,
            scopedSlots: { customRender: 'action' },
          },
        ],
        // 字典选项
        dictOptions: {},
        // 展开的行test
        expandedRowKeys: [],
        url: {
          list: '/po/materialvendorlink/list',
          delete: '/po/materialvendorlink/delete',
          deleteBatch: '/po/materialvendorlink/deleteBatch',
          exportXlsUrl: '/po/materialvendorlink/exportXls',
          importExcelUrl: '/po/materialvendorlink/importExcel',
        },
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl() {
        return window._CONFIG['domianURL'] + this.url.importExcelUrl
      }
    },
    methods: {
      initDictConfig() {
      },

      handleExpand(expanded, record) {
        this.expandedRowKeys = []
        if (expanded === true) {
          this.expandedRowKeys.push(record.id)
        }
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'code',text:'清单编码',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'清单名称',dictCode:''})
        fieldList.push({type:'string',value:'vendorid',text:'供应商',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'switch',value:'canenable',text:'有效'})
        fieldList.push({type:'string',value:'vendorcode',text:'供应商编码',dictCode:''})
        fieldList.push({type:'string',value:'vendorname',text:'供应商名称',dictCode:''})
        fieldList.push({type:'date',value:'fromdate',text:'有效期从'})
        fieldList.push({type:'date',value:'enddate',text:'有效期止'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style lang="less" scoped>
  @import '~@assets/less/common.less';
</style>