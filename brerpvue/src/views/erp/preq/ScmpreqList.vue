<template>
  <a-card class="j-inner-table-wrapper" :bordered="false">

    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">

          <a-form-item label="公司">
            <j-search-select-tag v-model="queryParam.companyid" placeholder="请选择公司"
              dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
          </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">

          <a-form-item label="单据编号">
            <j-input v-model="queryParam.preqcode" placeholder="请输入单据编号"></j-input>
          </a-form-item>
          </a-col>
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </a-col>
            </span>
            
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域 end -->

    <!-- 操作按钮区域 begin -->
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
      <a-button type="primary" icon="plus" @click="handlePrint('scmpreq')">打印预览</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('采购申请')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
        @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery">
      </j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel">
            <a-icon type="delete" />
            <span>删除</span>
          </a-menu-item>
        </a-menu>
        <a-button>
          <span>批量操作</span>
          <a-icon type="down" />
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

      <a-table ref="table" size="middle" bordered rowKey="id" class="j-table-force-nowrap" :scroll="{ x: true }"
        :loading="loading" :columns="columns" :dataSource="dataSource" :pagination="ipagination"
        :expandedRowKeys="expandedRowKeys" :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
        @expand="handleExpand" @change="handleTableChange">

        <!-- 内嵌table区域 begin -->
        <template slot="expandedRowRender" slot-scope="record">
          <a-tabs tabPosition="top">
            <a-tab-pane tab="采购申请表体" key="scmpreqitem" forceRender>
              <scmpreqitem-sub-table :record="record" />
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
            <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;" />
          </div>
        </template>


        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
            <span>下载</span>
          </a-button>
        </template>

        <template slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多
              <a-icon type="down" />
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleAudit(record,record.preqcode,'scmpreq')">提交审批</a>
              </a-menu-item>
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
    <scmpreq-modal ref="modalForm" @ok="modalFormOk" />

    <print-preview ref="preView"></print-preview>
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ScmpreqModal from './modules/ScmpreqModal'
import ScmpreqitemSubTable from './subTables/ScmpreqitemSubTable'
// import printPreview from '@/views/printmodule/demo/custom/preview'
import '@/assets/less/TableExpand.less'
import { getAction, postAction } from '../../../api/manage'

export default {
  name: 'ScmpreqList',
  mixins: [JeecgListMixin],
  components: {
    ScmpreqModal,
    ScmpreqitemSubTable,
    printPreview,
  },
  data () {
    return {
      description: '采购申请列表管理页面',
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
          title: '公司',
          align: 'center',
          wdith:100,
          dataIndex: 'companyid_dictText',
        },
        {
          title: '申请编号',
          align: 'center',
          dataIndex: 'preqcode',
        },
        {
          title: '申请部门',
          align: 'center',
          dataIndex: 'preqdept_dictText',
        },
        {
          title: '申请人',
          align: 'center',
          dataIndex: 'preqemp_dictText',
        },
        {
          title: '备注',
          align: 'center',
          dataIndex: 'remarks',
        },
        {
          title: '申请总数量',
          align: 'center',
          dataIndex: 'sumqty',
        },
        // {
        //   title: '申请总金额',
        //   align: 'center',
        //   dataIndex: 'sumvalue',
        // },
        {
          title: '审批状态',
          align: 'center',
          dataIndex: 'status',
          customRender: (text) => {
            if (text === '0' || text === ''  || text === null || text === undefined) {
              return '制单'
            } else if (text === '1') {
              return '审批中'
            } else if (text === '2') {
              return '审批通过'
            }
          }
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      // 字典选项
      dictOptions: {},
      // 展开的行test
      expandedRowKeys: [],
      url: {
        list: '/preq/scmpreq/list',
        delete: '/preq/scmpreq/delete',
        deleteBatch: '/preq/scmpreq/deleteBatch',
        exportXlsUrl: '/preq/scmpreq/exportXls',
        importExcelUrl: '/preq/scmpreq/importExcel',
        auditUrl: '/preq/scmpreq/audit',
      },
      superFieldList: [],
    }
  },
  created () {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl () {
      return window._CONFIG['domianURL'] + this.url.importExcelUrl
    }
  },
  methods: {
    //提交审批操作
    // handleAudit (record) {
    //   let params = { dataid: record.id };
    //   console.log(record.id);
    //   getAction(this.url.auditUrl, params).then((res) => {
    //     if (res.success) {
    //       this.$message.success(res.result);
    //     } else {
    //       this.$message.error(res.result);
    //     }
    //   })
    // },
    initDictConfig () {
    },

    handleExpand (expanded, record) {
      this.expandedRowKeys = []
      if (expanded === true) {
        this.expandedRowKeys.push(record.id)
      }
    },
    getSuperFieldList () {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'preqcode', text: '申请编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'preqdept', text: '申请部门', dictCode: '' })
      fieldList.push({ type: 'string', value: 'preqemp', text: '申请人', dictCode: '' })
      fieldList.push({ type: 'string', value: 'remarks', text: '备注', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'sumqty', text: '申请总数量', dictCode: '' })
      fieldList.push({ type: 'BigDecimal', value: 'sumvalue', text: '申请总金额', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
</style>