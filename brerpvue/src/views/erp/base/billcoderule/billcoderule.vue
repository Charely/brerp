<template>
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
  
      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('单据编号规则表')">导出</a-button>
        <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <!-- 高级查询区域 -->
        <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown>
      </div>
  
      <!-- table区域-begin -->
      <div>
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>
  
        <a-table
          ref="table"
          size="middle"
          :scroll="{x:true}"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
          class="j-table-force-nowrap"
          @change="handleTableChange">
  
          <template slot="htmlSlot" slot-scope="text">
            <div v-html="text"></div>
          </template>
          <template slot="imgSlot" slot-scope="text,record">
            <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
            <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
          </template>
          <template slot="fileSlot" slot-scope="text">
            <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
            <a-button
              v-else
              :ghost="true"
              type="primary"
              icon="download"
              size="small"
              @click="downloadFile(text)">
              下载
            </a-button>
          </template>
  
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
  
            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a @click="handleDetail(record)">详情</a>
                </a-menu-item>
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
  
        </a-table>
      </div>
  
      <scmbillcoderule-modal ref="modalForm" @ok="modalFormOk"></scmbillcoderule-modal>
    </a-card>
  </template>
  
  <script>
  
    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import ScmbillcoderuleModal from './modules/ScmbillcoderuleModal'
    import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  
    export default {
      name: 'ScmbillcoderuleList',
      mixins:[JeecgListMixin, mixinDevice],
      components: {
        ScmbillcoderuleModal
      },
      data () {
        return {
          description: '单据编号规则表管理页面',
          // 表头
          columns: [
            {
              title: '#',
              dataIndex: '',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            {
              title:'编号规则',
              align:"center",
              dataIndex: 'billrulecode'
            },
            {
              title:'是否包含年',
              align:"center",
              dataIndex: 'billruleyear',
              customRender: (text) => (text ? filterMultiDictText(this.dictOptions['billruleyear'], text) : ''),
            },
            {
              title:'是否包含月',
              align:"center",
              dataIndex: 'billrulemonth',
              customRender: (text) => (text ? filterMultiDictText(this.dictOptions['billrulemonth'], text) : ''),
            },
            {
              title:'是否包含日',
              align:"center",
              dataIndex: 'billruleday',
              customRender: (text) => (text ? filterMultiDictText(this.dictOptions['billruleday'], text) : ''),
            },
            {
              title:'流水号位数',
              align:"center",
              dataIndex: 'billrulenum'
            },
            {
              title:'断号重用',
              align:"center",
              dataIndex: 'billruledhsy',
              customRender: (text) => (text ? filterMultiDictText(this.dictOptions['billruledhsy'], text) : ''),
            },
            {
              title:'固定前缀',
              align:"center",
              dataIndex: 'fixedcode'
            },
            {
              title: '操作',
              dataIndex: 'action',
              align:"center",
              fixed:"right",
              width:147,
              scopedSlots: { customRender: 'action' }
            }
          ],
          url: {
            list: "/base/scmbillcoderule/list",
            delete: "/base/scmbillcoderule/delete",
            deleteBatch: "/base/scmbillcoderule/deleteBatch",
            exportXlsUrl: "/base/scmbillcoderule/exportXls",
            importExcelUrl: "base/scmbillcoderule/importExcel",
            
          },
          dictOptions:{},
          superFieldList:[],
        }
      },
      created() {
        this.$set(this.dictOptions, 'billruleyear', [{text:'是',value:'Y'},{text:'否',value:'N'}])
        this.$set(this.dictOptions, 'billrulemonth', [{text:'是',value:'Y'},{text:'否',value:'N'}])
        this.$set(this.dictOptions, 'billruleday', [{text:'是',value:'Y'},{text:'否',value:'N'}])
        this.$set(this.dictOptions, 'billruledhsy', [{text:'是',value:'Y'},{text:'否',value:'N'}])
      this.getSuperFieldList();
      },
      computed: {
        importExcelUrl: function(){
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        },
      },
      methods: {
        initDictConfig(){
        },
        getSuperFieldList(){
          let fieldList=[];
          fieldList.push({type:'string',value:'billrulecode',text:'编号规则',dictCode:''})
          fieldList.push({type:'switch',value:'billruleyear',text:'是否包含年'})
          fieldList.push({type:'switch',value:'billrulemonth',text:'是否包含月'})
          fieldList.push({type:'switch',value:'billruleday',text:'是否包含日'})
          fieldList.push({type:'int',value:'billrulenum',text:'流水号位数',dictCode:''})
          fieldList.push({type:'switch',value:'billruledhsy',text:'断号重用'})
          fieldList.push({type:'string',value:'fixedcode',text:'固定前缀',dictCode:''})
          this.superFieldList = fieldList
        }
      }
    }
  </script>
  <style scoped>
    @import '~@assets/less/common.less';
  </style>