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
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
    </div>

    <div>
      <a-button @click="handlequery" type="primary" icon="plus">查询</a-button>
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleOpen(record)">打开</a>
          <a-divider type="vertical" />
          <a @click="handleStop(record)">关账</a>
          <a-divider type="vertical" />
          <a @click="handleClose(record)">月结</a>
          <a-divider type="vertical" />
        </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { getAction } from '../../../../api/manage'

  export default {
    name: 'MvperiodList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      
    },
    data () {
      return {
        description: '库存期间管理页面',
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
            title:'公司',
            align:"center",
            dataIndex: 'companyid_dictText'
          },
          {
            title:'期间',
            align:'center',
            dataIndex:'perioddate'
          },
          {
            title:'期间从',
            align:"center",
            dataIndex: 'fromdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'期间止',
            align:"center",
            dataIndex: 'enddate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'期间状态',
            align:"center",
            dataIndex: 'periodstate',
            customRender:function(text){
              if(text ==='init'){
                return '初始'
              }else if(text === 'open'){
                return '打开'
              }else if(text === 'stopped'){
                return '关帐'
              }else if(text === 'closed'){
                return '月结'
              }else if(text === 'future'){
                return '未来'
              }
            }
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
        ipagination:{
            current: 1,
            pageSize: 12,
            pageSizeOptions: ['10', '20', '30'],
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
        },
        url: {
          list: "/base/imperiod/list",
          addperiod:'/base/imperiod/addperiod',
          delete: "/base/imperiod/delete",
          deleteBatch: "/base/imperiod/deleteBatch",
          exportXlsUrl: "/base/imperiod/exportXls",
          importExcelUrl: "base/imperiod/importExcel",
          opernurl:'/base/imperiod/changeperiodstate',
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    mounted(){
      this.dataSource=[];
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      handleOpen(record){
        let companyid=this.queryParam.companyid;
        if(companyid === undefined || companyid === ''){
          this.$message.error("请先选择公司");
          return;
        }
        let periodstate='open';
        let perioddate=record.perioddate;
        let parmas={companyid:companyid,perioddate:perioddate,periodstate:periodstate};
        let that=this;
        getAction(this.url.opernurl,parmas).then(res=>{
          if(res.success){
            that.loadData();
            this.$message.success("打开成功");
          }else{
            this.$message.error(res.message);
          }
        })
      },
      handlequery(){
        if(this.queryParam.companyid === undefined || this.queryParam.companyid === ''){
          this.$message.error("请先选择公司");
          return;
        }
        getAction(this.url.addperiod,{companyid:this.queryParam.companyid}).then(res=>{
          if(res.success){
            //this.$message.success("查询成功");
          }
        })
        this.loadData();
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'companyid',text:'公司',dictCode:''})
        fieldList.push({type:'date',value:'fromdate',text:'期间从'})
        fieldList.push({type:'date',value:'enddate',text:'期间止'})
        fieldList.push({type:'string',value:'periodstate',text:'期间状态',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>