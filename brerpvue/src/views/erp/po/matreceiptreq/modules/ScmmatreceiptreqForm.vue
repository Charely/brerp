<template>
   <a-spin :spinning="confirmLoading">
     <j-form-container :disabled="formDisabled">
       <!-- 主表单区域 -->
       <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
         <a-row>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="业务日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入业务日期" ></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-vendor v-model="model.vendorid" :buttons="false"
              placeholder="请输入供应商" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注" ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
     </j-form-container>
      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="收料申请单分录" :key="refKeys[0]" :forceRender="true">
         
          <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmmatreceiptreqitemTable.loading"
          :columns="scmmatreceiptreqitemTable.columns" 
          :dataSource="scmmatreceiptreqitemTable.dataSource" 
          :maxHeight="300"
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           :alwaysEdit="true"
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix">
            <a-button type="primary" @click="getPoData" :disabled="formDisabled">参照采购订单</a-button>
            <a-button type="primary" @click="getWwData" :disabled="formDisabled">参照委外订单</a-button>
          </template>
          </j-vxe-table>
        </a-tab-pane>
      </a-tabs>
      <receipt-req-refer-po ref="ReceiptReqReferPo" @ok="ReceiptReqReferPo"></receipt-req-refer-po>
      <receipt-req-refer-ww ref="ReceiptReqReferWw" @ok="ReceiptReqReferWw"></receipt-req-refer-ww>
    </a-spin>
</template>
<script>
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  //import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'

  import { validateDuplicateValue,getNowFormatDate } from '@/utils/util'
  import { getAction } from '../../../../../api/manage'
  import ReceiptReqReferPo from '../../../base/custom/ReceiptReqReferPo.vue'
  import  ReceiptReqReferWw from '../../../base/custom/CReceiptReqReferWw.vue'
  import { queryMaterialVoInfoById }  from '@/api/erp/commonapi.js'

  export default {
    name: 'ScmmatreceiptreqForm',
    mixins: [JVxeTableModelMixin],
    components: {
      ReceiptReqReferPo,
      ReceiptReqReferWw
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model:{
        },
        validatorRules: {
          billdate:[{required:true,message:'业务日期必填'}],
          vendorid:[{required:true,message:'供应商必填'}],
          companyid:[{required:true,message:'公司必填'}],
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        refKeys: ['scmmatreceiptreqitem', ],
        tableKeys:['scmmatreceiptreqitem', ],
        activeKey: 'scmmatreceiptreqitem',
        itemTable:{},
        // 收料申请单分录
        scmmatreceiptreqitemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料',
              key: 'materialid',
              type: FormTypes.sel_material,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules:[
              ],
              props:{
                  materialtype:'0',
               }
            },
            {
            title: '物料编号',
            key: 'materialcode',
            type: FormTypes.input,
            disabled: true,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '物料名称',
            key: 'materialname',
            type: FormTypes.hidden,
            disabled: true,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
            {
              title: '计量单位',
              key: 'uomid',
              type: FormTypes.sel_search,
              dictCode:'scmuom,uomname,id',
              width: "100px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title:"收货数量",
              key:'qty',
              type:FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              statistics: ['sum']
            },
            {
              title:"收货仓库",
              key:'warehouseid',
              type:FormTypes.sel_search,
              width:"150px",
              dictCode:"warehouse,name,id",
              placeholder: '请输入${title}',
              defaultValue:''
            },
            {
              title: '批次编号',
              key: 'batchcode',
              type: FormTypes.input,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
              customRender:(text,record,index)=>{
                if(text){
                  return text;
                }else{
                  return text;
                }
              }
            },
            {
              title: '供应商批次',
              key: 'vendorbatchcode',
              type: FormTypes.input,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '批次生效日期',
              key: 'batchstartdate',
              type: FormTypes.date,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules:[
              ]
            },
            {
              title: '批次失效日期',
              key: 'batchenddate',
              type: FormTypes.date,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '备注',
              key: 'remarks',
              type: FormTypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: 'fromid',
              key: 'fromid',
              type: FormTypes.hidden,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: 'fromitemid',
              key: 'fromitemid',
              type: FormTypes.hidden,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title:'fromtype',
              key:'fromtype',
              type:FormTypes.hidden,
            },
            {
              title:'taxinprice',
              key:'taxinprice',
              type:FormTypes.hidden,
            },
            {
              title:'taxrate',
              key:'taxrate',
              type:FormTypes.hidden,
            },
            {
              title:'taxexprice',
              key:'taxexprice',
              type:FormTypes.hidden,
            },
            {
              title:'taxinvalue',
              key:'taxinvalue',
              type:FormTypes.hidden,
            },
            {
              title:'taxexvalue',
              key:'taxexvalue',
              type:FormTypes.hidden,
            }
          ]
        },
        url: {
          add: "/po/scmmatreceiptreq/add",
          edit: "/po/scmmatreceiptreq/edit",
          scmmatreceiptreqitem: {
            list: '/po/scmmatreceiptreq/queryScmmatreceiptreqitemByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
      this.itemTable=this.scmmatreceiptreqitemTable
      this.model.billdate=getNowFormatDate();
      console.log("created:"+getNowFormatDate());
    },
    watch:{
      'model.companyid':{
        handler(val,old){
          localStorage.setItem("receiptreq_companyid",this.model.companyid);
        }
      }
    },
    methods: {
      //参照委外订单
      getWwData(){
        if(this.model.vendorid === '' || this.model.vendorid === undefined){
          this.$message.error("请先选择供应商");
          return;
        }
        this.$nextTick(() => {
        this.$refs.ReceiptReqReferWw.show('其它', '委外订单', "", "1,3",this.model.vendorid)
      })
      },
      ReceiptReqReferWw(selectedRowKeys){
        let ids = "";
        for (let i = 0; i < selectedRowKeys.length; i++) {
          if (selectedRowKeys[i] != undefined) {
            ids += selectedRowKeys[i] + ",";
          }
        }
        let url = "/outsource/scmoutsource/queryScmoursourceItemlistbyids";
        let param = { ids: ids };
        getAction(url,param).then(async res=>{
          if(res.success){
            let resu = res.result.records || res.result;
          //console.log("resu" + resu);
          //todo 给新增行赋值
          let info = [];
          if (resu != null && resu.length > 0) {
            for (let i = 0; i < resu.length; i++) {
              let currentValue = parseFloat(resu[i]["qty"], 2)
              let materialvoinfo =  await queryMaterialVoInfoById({materialid: resu[i].materialid});
              let defaultWarehouseid=''
              if(materialvoinfo.success){
                if(materialvoinfo.result.defwarehouseid != '' && materialvoinfo.result.defwarehouseid != undefined){
                  defaultWarehouseid = materialvoinfo.result.defwarehouseid
                }
              }
              const newData = {
                itemcode:'0001',
                materialid: resu[i]["materialid"],
                materialcode:resu[i]["materialcode"],
                materialname:resu[i]["materialname"],
                uomid:resu[i]["uomid"],
                batchid:"",
                warehouseid:defaultWarehouseid,
                qty: (resu[i]["qty"]-resu[i]["receiptreqqty"]).toFixed(2),
                fromid: resu[i]["parentid"],
                fromitemid: resu[i]["id"],
                fromtype:"outsource",
                vendorbatchcode:'',
                batchstartdate:getNowFormatDate(),
                batchenddate:getNowFormatDate(),
                taxinprice:resu[i].taxinprice,
                taxrate:resu[i].taxrate,
                taxexprice:resu[i].taxexprice,
                taxinvalue:(resu[i].taxinprice * (resu[i]["qty"]-resu[i]["receiptreqqty"])).toFixed(2),
                taxexvalue:(resu[i].taxexprice * (resu[i]["qty"]-resu[i]["receiptreqqty"])).toFixed(2)
              }
              info.push(newData);
            }
            this.scmmatreceiptreqitemTable.dataSource = info;
          }
          }
        })
      },
      getPoData(){
        if(this.model.vendorid === '' || this.model.vendorid === undefined){
          this.$message.warning("请先选择供应商");
          return;
        }
        this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.ReceiptReqReferPo.show('其它', '采购订单', "", "1,3",this.model.vendorid)
      })
      },
      ReceiptReqReferPo(selectedRowKeys){
        let ids = "";
        for (let i = 0; i < selectedRowKeys.length; i++) {
          if (selectedRowKeys[i] != undefined) {
            ids += selectedRowKeys[i] + ",";
          }
        }
        let url = "/po/scmpo/queryScmpoitemlistbyids";
      let param = { ids: ids };
      getAction(url, param).then(async res => {
        if (res) {
          let resu = res.result.records || res.result;
          //console.log("resu" + resu);
          //todo 给新增行赋值
          let info = [];
          if (resu != null && resu.length > 0) {
            for (let i = 0; i < resu.length; i++) {
              let currentValue = parseFloat(resu[i]["prqty"], 2)
              let materialvoinfo =  await queryMaterialVoInfoById({materialid: resu[i].materialid});
              let defaultWarehouseid=''
              if(materialvoinfo.success){
                if(materialvoinfo.result.defwarehouseid != '' && materialvoinfo.result.defwarehouseid != undefined){
                  defaultWarehouseid = materialvoinfo.result.defwarehouseid
                }
              }
              const newData = {
                itemcode:'0001',
                materialid: resu[i]["materialid"],
                materialcode:resu[i]["materialcode"],
                materialname:resu[i]["materialname"],
                uomid:resu[i]["uomid"],
                batchid:"",
                qty: (resu[i]["qty"]-resu[i]["receiptreqqty"]).toFixed(2),
                fromid: resu[i]["parentid"],
                fromitemid: resu[i]["id"],
                vendorbatchcode:'',
                warehouseid:defaultWarehouseid,
                fromtype:'po',
                batchstartdate:getNowFormatDate(),
                batchenddate:getNowFormatDate(),
                taxinprice:resu[i].taxinprice,
                taxrate:resu[i].taxrate,
                taxexprice:resu[i].taxexprice,
                taxinvalue:(resu[i].taxinprice * (resu[i]["qty"]-resu[i]["receiptreqqty"])).toFixed(2),
                taxexvalue:(resu[i].taxexprice * (resu[i]["qty"]-resu[i]["receiptreqqty"])).toFixed(2)
              }
              info.push(newData);
            }
            this.scmmatreceiptreqitemTable.dataSource = info;
          }
        }
      })
      },
     addBefore(){
          
            this.scmmatreceiptreqitemTable.dataSource=[]
          
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.scmmatreceiptreqitem.list, params, this.scmmatreceiptreqitemTable)
        }
      },
      //校验所有一对一子表表单
    validateSubForm(allValues){
        return new Promise((resolve,reject)=>{
          Promise.all([
          ]).then(() => {
            resolve(allValues)
          }).catch(e => {
            if (e.error === VALIDATE_NO_PASSED) {
              // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
              this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
            } else {
              console.error(e)
            }
          })
        })
    },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          scmmatreceiptreqitemList: allValues.tablesValue[0].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     close() {
        this.visible = false
        this.$emit('close')
        this.$refs.form.clearValidate();
      },

    }
  }
</script>

<style scoped>
</style>