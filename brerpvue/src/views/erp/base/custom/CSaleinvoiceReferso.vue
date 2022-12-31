<template>
    <a-modal :title="title" :width="1250" :height="700" :visible="visible" @ok="handleOk" @cancel="handleCancel" cancelText="关闭"
      wrapClassName="ant-modal-cust-warp" style="top:5%;height: 700;">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper" v-if="selectType === 'list'">
        <!-- 搜索区域 -->
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="24">
              <a-form-item label="单据编号" :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                <a-input placeholder="请输入单据编号查询" v-model="queryParam.billcode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="商品信息" :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                <a-input placeholder="条码|名称|规格|型号" v-model="queryParam.materialparam"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-range-picker style="width: 100%" v-model="queryParam.createTimeRange" format="YYYY-MM-DD"
                  :placeholder="['开始时间', '结束时间']" @change="onDateChange" @ok="onDateOk" />
              </a-form-item>
            </a-col>
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-col :md="6" :sm="24">
                <a-button type="primary" @click="searchQuery">查询</a-button>
                <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
              </a-col>
            </span>
          </a-row>
        </a-form>
      </div>
      <!-- table区域-begin -->
      <a-table v-if="selectType === 'list'" bordered ref="table" size="middle" rowKey="itemid" :columns="columns"
        :dataSource="dataSource" :pagination="ipagination" :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: getType }"
        :customRow="rowAction" @change="handleTableChange">
        <span slot="numberCustomRender" slot-scope="text, record">
          <a @click="myHandleDetail(record)">{{ record.number }}</a>
        </span>
      </a-table>
    </a-modal>
  </template>
  
  <script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import Vue from 'vue'
  import { stubString } from 'lodash'
  export default {
    name: 'SaleinviceReferSo',
    mixins: [JeecgListMixin],
    components: {
    },
    data () {
      return {
        title: "操作",
        visible: false,
        disableMixinCreated: true,
        selectedRowKeys: [],
        selectedDetailRowKeys: [],
        selectBillRows: [],
        selectBillDetailRows: [],
        showType: 'basic',
        selectType: 'list',
        linkNumber: '',
        organId: '',
        discount: '',
        deposit: '',
        remark: '',
        vendorid:'',
        customerid:'',
        showStatisticsRow:true,
        hasStatisticsColumn:true,
        queryParam: {
          preqcode: "",
          materialParam: "",
          type: "",
          subType: "",
          status: ""
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
          {
            title: '单据编号', dataIndex: 'billcode', width: 100,
          },
          {
            title:'业务日期',dataIndex:'billdate',width:100
          },
          { title: '客户编号', dataIndex: 'customercode', width: 60 },
          { title: '客户名称', dataIndex: 'customername', width: 100 },
         
          {
            title: '产品编号', dataIndex: 'materialcode', width: 60, 
          },
          {
            title: '产品名称', dataIndex: 'materialname', width: 100, 
          },
          {
            title:'待开票数量',
            dataIndex:'needoutqty',
            width:100,
            customRender:(text,row,index)=>{
                let invoiceqty=row['invoiceqty']
               if(row['invoiceqty'] === null || row['invoiceqty'] === undefined){
                invoiceqty=0;
               }
               if(row.isred === '1'){
                return (0-(row['soqty'] - invoiceqty)).toFixed(2)
               }else{
                return (row['soqty'] - invoiceqty).toFixed(2)
               }
            }
          },
          {
            title: '订单数量', 
            dataIndex: 'soqty', 
            width: 100,
            customRender:(text,row,index)=>{
                if(row.isred === '1'){
                    return (0- text).toFixed(2);
                }else{
                    return text
                }
            }
          },
          {
            title: '已开票数量', dataIndex: 'invoiceqty', width: 100,
            customRender:(text,row,index)=>{
              let invoiceqty=0;
              if(!text){
                invoiceqty=0;
              }else{
                invoiceqty=text;
              }
              if(row.isred === '1'){
                    return (0- invoiceqty).toFixed(2);
                }else{
                    return invoiceqty
                }
            }
          },
         
        ],
        dataSource: [],
        dataSourceDetail: [],
        url: {
          list: "/so/scmso/getrefersolist/3"
        }
      }
    },
    computed: {
      getType: function () {
        return 'checkbox';
      }
    },
    created () {
    },
    methods: {
      show (type, subType, organType, status,customerid) {
        this.selectType = 'list'
        this.customerid=customerid;
        this.showType = 'basic'
        this.queryParam.type = type
        this.queryParam.subType = subType
        this.queryParam.roleType = Vue.ls.get('roleType')
        this.queryParam.status = status
        this.queryParam.customerid=customerid
        this.queryParam.companyid=localStorage.getItem("salesinvoice_companyid")
        //this.columns[0].title = organType
        this.model = Object.assign({}, {});
        this.visible = true;
        this.loadData(1)
      },
      myHandleDetail (record) {
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      onSelectChange (selectedRowKeys) {
        this.selectedRowKeys = selectedRowKeys;
      },
      onSelectDetailChange (selectedRowKeys) {
        this.selectedDetailRowKeys = selectedRowKeys;
      },
      handleOk () {
        this.getSelectBillRows();
        let curcustomerid=''
        for(let i=0;i<this.selectBillRows.length;i++){
          if(curcustomerid === ''){
            curcustomerid=this.selectBillRows[i].customerid
          }else{
            if(curcustomerid != this.selectBillRows[i].customerid){
              this.$message.error("不同客户不允许同时结算!");
              return;
            }
          }
        }
        if(this.selectedRowKeys){
          this.$emit('ok',this.selectedRowKeys);
        }
        
        this.close();
      },
      //查询明细列表
      loadDetailData (arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        if (this.selectBillRows && this.selectBillRows.length > 0) {
          let record = this.selectBillRows[0]
          let param = {
            headerId: record.id,
            mpList: '',
            linkType: this.showType
          }
          this.loading = true;
          getAction('/depotItem/getDetailList', param).then((res) => {
            if (res.code === 200) {
              let list = res.data.rows;
              let listEx = []
              for (let j = 0; j < list.length; j++) {
                let info = list[j];
                //去掉已经全部转换的明细
                if (info.preNumber !== info.finishNumber) {
                  listEx.push(info)
                }
              }
              this.dataSourceDetail = listEx
              this.ipagination.total = res.data.total;
            }
            if (res.code === 510) {
              this.$message.warning(res.data)
            }
            this.loading = false;
          })
        }
      },
      onDateChange: function (value, dateString) {
        this.queryParam.beginTime = dateString[0].substring(0,10)
        this.queryParam.endTime = dateString[1].substring(0,10)
      },
      onDateOk (value) {
        console.log(value);
      },
      searchReset () {
        this.queryParam = {
          type: this.queryParam.type,
          subType: this.queryParam.subType,
          status: "1,3",
          vendorid:this.queryParam.vendorid,
        }
        this.loadData(1);
      },
      getSelectBillRows () {
        let dataSource = this.dataSource;
        this.selectBillRows = [];
        for (let i = 0, len = dataSource.length; i < len; i++) {
          if (this.selectedRowKeys.includes(dataSource[i].itemid)) {
            this.selectBillRows.push(dataSource[i]);
          }
        }
      },
      getSelectBillDetailRows () {
        let dataSource = this.dataSourceDetail;
        this.selectBillDetailRows = [];
        for (let i = 0, len = dataSource.length; i < len; i++) {
          if (this.selectedDetailRowKeys.includes(dataSource[i].id)) {
            this.selectBillDetailRows.push(dataSource[i]);
          }
        }
      },
      rowAction (record, index) {
        return {
          on: {
            click: () => {
              let arr = []
              arr.push(record.itemid)
              this.selectedRowKeys = arr
            },
            dblclick: () => {
              let arr = []
              arr.push(record.id)
              this.selectedRowKeys = arr
              this.handleOk()
            }
          }
        }
      }
    }
  }
  </script>
  
  <style scoped>
  </style>