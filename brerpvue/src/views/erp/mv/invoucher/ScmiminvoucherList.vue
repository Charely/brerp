<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="公司">
              <j-search-select-tag v-model="queryParam.companyid" placeholder="请选择公司"
                  dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
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
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" @click="handleaudit('2')">审核</a-button>
      <a-button type="primary" @click="handleaudit('0')">取消审核</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('入库凭证')">导出</a-button>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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

    <scmiminvoucher-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ScmiminvoucherModal from './modules/ScmiminvoucherModal'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "ScmiminvoucherList",
    mixins:[JeecgListMixin],
    components: {
      ScmiminvoucherModal
    },
    data () {
      return {
        description: '入库凭证管理页面',
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
            title:'业务日期',
            align:"center",
            dataIndex: 'billdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'公司',
            align:"center",
            dataIndex: 'companyid_dictText'
          },
          {
            title:'单据编号',
            align:"center",
            dataIndex: 'billcode'
          },
          {
            title:'部门',
            align:"center",
            dataIndex: 'vdeptid_dictText'
          },
          {
            title:'人员',
            align:"center",
            dataIndex: 'vemptid_dictText'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'是否暂估',
            align:"center",
            dataIndex: 'iszg',
            customRender:(text)=>{
              if(text === '0'){
                return '暂估';
              } else if(text == '1'){
                return '冲销';
              }else if(text == '2'){
                return '核销';
              }
            }
          },
          {
            title: '单据状态',
            align: 'center',
            dataIndex: 'status',
            customRender: (text) => {
              if (text === '' || text === null || text === undefined || text === '0') {
                return '制单'
              } else if (text === '1') {
                return '审批中'
              } else if (text === '2') {
                return '审批通过'
              }
            }
          },
          {
            title:'是否记账',
            align:"center",
            dataIndex: 'tallFlag',
            customRender:(text)=>{
              if(text === '0'){
                return '未记账';
              } else if(text == '1'){
                return '已记账';
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/im/scmiminvoucher/list/0",
          delete: "/im/scmiminvoucher/delete",
          deleteBatch: "/im/scmiminvoucher/deleteBatch",
          exportXlsUrl: "/im/scmiminvoucher/exportXls",
          importExcelUrl: "im/scmiminvoucher/importExcel",
          auditurl:"/im/scmiminvoucher/updatestatus",
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'date',value:'billdate',text:'业务日期'})
        fieldList.push({type:'string',value:'companyid',text:'公司',dictCode:''})
        fieldList.push({type:'string',value:'billcode',text:'单据编号',dictCode:''})
        fieldList.push({type:'string',value:'vdeptid',text:'部门',dictCode:''})
        fieldList.push({type:'string',value:'vemptid',text:'人员',dictCode:''})
        fieldList.push({type:'string',value:'remarks',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'fromtype',text:'单据类型',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>