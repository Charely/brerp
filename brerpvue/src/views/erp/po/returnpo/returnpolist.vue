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
            <j-input v-model="queryParam.billcode" placeholder="请输入单据编号"></j-input>
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
        <!-- <a-button type="primary" icon="download" @click="handleExportXls('采购订单')">导出</a-button>
        <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
          @change="handleImportExcel">
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload> -->
        <a-button type="primary" @click="handleaudit('2')">审核</a-button>
        <a-button type="primary" @click="handleaudit('0')">取消审核</a-button>
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
      <scm-returnpo-modal ref="modalForm" @ok="modalFormOk" />
  
    </a-card>
  </template>
  
  <script>
  
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ScmReturnpoModal from './modules/ScmReturnpoModal'
  import '@/assets/less/TableExpand.less'
import { getAction } from '../../../../api/manage'
  
  export default {
    name: 'returnpolist',
    mixins: [JeecgListMixin],
    components: {
      ScmReturnpoModal,
    },
    data () {
      return {
        description: '退货订单列表管理页面',
        objectcode:'scmpo',
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
            title: '采购日期',
            align: 'center',
            dataIndex: 'podate',
          },
          {
            title: '采购日期',
            align: 'center',
            dataIndex: 'billcode',
          },
          {
            title: '采购部门',
            align: 'center',
            dataIndex: 'podeptid_dictText',
          },
          {
            title: '采购人员',
            align: 'center',
            dataIndex: 'poemptid_dictText',
          },
          {
            title: '供应商',
            align: 'center',
            dataIndex: 'vendorid_dictText',
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'remark',
          },
          {
            title:'单据状态',
            align:'center',
            dataIndex:'status',
            customRender:(text)=>{
              if(text === '' || text === null || text === undefined || text ==='0'){
                return '制单'
              }else if(text ==='1'){
                return '审批中'
              } else if(text ==='2'){
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
          list: '/po/scmpo/list/1',
          delete: '/po/scmpo/delete',
          deleteBatch: '/po/scmpo/deleteBatch',
          exportXlsUrl: '/po/scmpo/exportXls',
          importExcelUrl: '/po/scmpo/importExcel',
          auditurl:'/po/scmpo/updatestatus'
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
        fieldList.push({ type: 'string', value: 'podate', text: '采购日期', dictCode: '' })
        fieldList.push({ type: 'string', value: 'podeptid', text: '采购部门', dictCode: '' })
        fieldList.push({ type: 'string', value: 'poemptid', text: '采购人员', dictCode: '' })
        fieldList.push({ type: 'string', value: 'vendorid', text: '供应商', dictCode: '' })
        fieldList.push({ type: 'string', value: 'remark', text: '备注', dictCode: '' })
        this.superFieldList = fieldList
      }
    }
  }
  </script>
  <style  scoped>
   @import '~@assets/less/common.less';
  </style>