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
      <a-button type="primary" icon="download" @click="handleExportXls('采购订单')">导出</a-button>
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

      <a-table ref="table" 
      size="middle" 
      bordered rowKey="id" 
      :scroll="{ x: true }"
        :loading="loading" 
        :columns="columns" 
        :dataSource="dataSource"
         :pagination="ipagination"
        :expandedRowKeys="expandedRowKeys"
         :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
        @expand="handleExpand" 
        @change="handleTableChange">

        <template slot="confirmqty" slot-scope="text, record">
          <editable-cell :text="text" @change="onCellChange(record, 'confirmqty', $event)" />
         </template>


        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

         <template slot="imgSlot" slot-scope="text,record">
          <div style="font-size: 12px;font-style: italic;">

            <img :src="text"  :width="200"  alt="" style="width:50px;height:50px;" />
          </div>
        </template> 

        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
            <span>下载</span>
          </a-button>
        </template>

        <template slot="action" slot-scope="text, record">
          <a @click="createBarCode(record)">生成待发货二维码</a>
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
  </a-card>
</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import '@/assets/less/TableExpand.less'
import { getAction, postAction } from '../../../api/manage';
import { WindowsFill } from '@ant-design/icons/lib/dist';
import { thirdLogin } from '../../../api/login';

const EditableCell = {
  template: `
      <div class="editable-cell">
        <div v-if="editable" class="editable-cell-input-wrapper">
          <a-input :value="value" @change="handleChange" @pressEnter="check" /><a-icon
            type="check"
            class="editable-cell-icon-check"
            @click="check"
          />
        </div>
        <div v-else class="editable-cell-text-wrapper">
          {{ value || ' ' }}
          <a-icon type="edit" class="editable-cell-icon" @click="edit" />
        </div>
      </div>
    `,
  props: {
    text: String,
  },
  data() {
    return {
      value: this.text,
      editable: false,
    };
  },
  methods: {
    handleChange(e) {
      const value = e.target.value;
      this.value = value;
    },
    check() {
      this.editable = false;
      this.$emit('change', this.value);
    },
    edit() {
      this.editable = true;
    },
  },
};


export default {
  name: 'orderlist',
  mixins: [JeecgListMixin],
  components: {
    EditableCell
  },
  data () {
    return {
      description: '采购订单列表管理页面',
      vendorid:'',
      picurl:'',
      pics:[],
      queryParam:{
        'vendorid':this.$store.getters.userInfo.innervendorid
      },
      // 表头
      columns: [
      {
          title: '单据编号', dataIndex: 'billcode', width: 100,
        },
        {
          title:'订单日期',dataIndex:'billdate',width:100
        },
        { title: '供应商名称', dataIndex: 'vendorname', width: 70 },
        {
          title: '产品编号', dataIndex: 'materialcode', width: 100, 
        },
        {
          title: '产品名称', dataIndex: 'materialname', width: 100, 
        },
        {
          title: '采购数量', dataIndex: 'poqty', width: 100,
        },
        {
          title:'待发货数量',dataIndex:'unpoqty',width:100,
          customRender(text,record,index){
            if(record){
              return record["poqty"]-record["receiptreqqty"];
            }
          }
        },
        {
          title:'本次发货数量',
          dataIndex:'confirmqty',
          width:100,
          scopedSlots: { customRender: 'confirmqty' },
        },
        {
          title:'在途未接收数量',
          dataIndex:'ztqty',
          width:100,
        },
        {
          title:'已发货数量',
          dataIndex:'receiptreqqty',
          width:100,
          customRender(text){
            if(text === null || text === undefined){
              return 0;
            }else{
              return text;
            }
          }
        },
        {
          title:'待发货二维码',
          dataIndex:'imgsrc',
          width:200,
          scopedSlots:{customRender:'imgSlot'}
        },
      ],
      // 字典选项
      dictOptions: {},
      // 展开的行test
      expandedRowKeys: [],
      url: {
        list: '/po/scmpo/vendorreferpolist',
        createbarcode:'/base/scmbarcode/add',
        getbarcodeimg:'/base/scmbarcode/vendorreferpolist',
      },
      superFieldList: [],
    }
  },
  beforeCreate (){
   
  },
  created () {
    // this.getSuperFieldList();

    //获取登录用户
    // console.log("currentuser:"+JSON.stringify(this.$store.getters.userInfo))

    
  },
  mounted(){
    // let userInfo=this.$store.getters.userInfo;
    //  if(userInfo.innervendorid){
    //     this.vendorid=userInfo.innervendorid
    //  }

    //  this.queryParam.vendorid=this.vendorid;

    //  this.dataSource=[];

    //  console.log("this.vendorid:"+this.vendorid)

    //  this.loadData(1);
    
  },
  computed: {
    importExcelUrl () {
      return window._CONFIG['domianURL'] + this.url.importExcelUrl
    }
  },
  methods: {

    /**
     * 获取二维码数据并进行显示
     * @param {单据行数据} record 
     */
    getImgView(text,record){
      let params={id:record.itemid}
      let imgsrc='';
       getAction(this.url.getbarcodeimg,params).then(res=>{
        if(res.success){
          console.log("imageview:"+JSON.stringify(res))
          let currentimg=res.result;
          imgsrc=currentimg
          return imgsrc;
        }
      })
    },

    /**
     * 
     * @param {*} record 
     */
    createBarCode(record){
      if(record){
        if(!record.hasOwnProperty('confirmqty')){
          this.$message.warning('请先确认本次发货数量');
          return;
        }
        if(record.confirmqty === null || record.confirmqty === undefined || record.confirmqty === '0'){
          this.$message.warning("请先确认本次发货数量不能为空或者不能为0");
          return;
        }

        let params={};
        params.billid=record.billid;
        params.billitemid=record.itemid;
        params.bartext=record.confirmqty;
        params.vendorid=this.$store.getters.userInfo.innervendorid;
        postAction(this.url.createbarcode,params).then(res=>{
          if(res){
            this.$message.success("确认成功");
            this.loadData(1);
            return;
          }
        })
      }else{
        return;
      }
    },

    /**
     * 
     * @param {行主键id} record 
     * @param {修改的列名称} columnname 
     * @param {修改的列值} event 
     */
    onCellChange(record,columnname,event){
      let confirmqty=event;
      let needtoqty=record["poqty"]-record["receiptreqqty"]
      if(confirmqty > needtoqty){
        this.$message.warning("超过带发出数量了,请重新编辑");
        return ;
      }
     // let changeDatasource=[];
      for(let i=0;i<this.dataSource.length;i++){
        let cur=this.dataSource[i];
        if(cur.itemid === record.itemid){
          this.$set(cur,'confirmqty',confirmqty);
        }
       // changeDatasource.push(cur);
      }

     // this.dataSource=changeDatasource;
    },
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
<style lang="less" scoped>
@import '~@assets/less/common.less';
</style>