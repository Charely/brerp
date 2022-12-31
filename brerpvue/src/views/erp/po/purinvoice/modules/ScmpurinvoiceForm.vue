<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!-- <a-col :span="6">
            <a-form-model-item label="采购组织" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="purorgid">
              <a-input v-model="model.purorgid" placeholder="请输入采购组织"></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入单据日期"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-vendor v-model="model.vendorid" :buttons="false" placeholder="请输入供应商"></c-select-vendor>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="采购部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deptid">
              <j-select-depart v-model="model.deptid" placeholder="请输入采购部门"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="采购人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="emptid">
              <c-select-user v-model="model.emptid"  :buttons="false" placeholder="请输入采购人员"></c-select-user>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="采购发票分录" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmpurinvoiceitemTable.loading"
          :columns="scmpurinvoiceitemTable.columns" 
          :dataSource="scmpurinvoiceitemTable.dataSource" 
          :maxHeight="300"
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           :alwaysEdit="true"
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix">
            <a-button type="primary" @click="getReferpoData" :disabled="formDisabled">参照采购订单</a-button>
            <a-button type="primary" @click="getReferinstockData" :disabled="formDisabled">参照采购入库单</a-button>
          </template>
          </j-vxe-table>
      </a-tab-pane>
      <purinvoice-refer-po ref="purinvoicereferpo" @ok="getSelectPoData"></purinvoice-refer-po>
      <purinvoice-refer-instock ref="purinvoicereferinstock" @ok="getSelectInstockData"></purinvoice-refer-instock>
    </a-tabs>
  </a-spin>
</template>

<script>

import { getAction } from '@/api/manage'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getRefPromise, VALIDATE_FAILED } from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
import { validateDuplicateValue,getNowFormatDate } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import PurinvoiceReferPo from '@/views/erp/base/custom/CPurinvoiceReferPo.vue'

import PurinvoiceReferInstock  from '@/views/erp/base/custom/CPurinvoiceReferInstock.vue'

export default {
  name: 'ScmpurinvoiceForm',
  mixins: [JVxeTableModelMixin],
  components: {
    JFormContainer,
    PurinvoiceReferPo,
    PurinvoiceReferInstock
  },
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      model: {
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {
        companyid: [
            { required: true, message: '公司必填!' },
          ],
          vendorid:[
          { required: true, message: '供应商必填' },
          ]
      },
      refKeys: ['scmpurinvoiceitem',],
      tableKeys: ['scmpurinvoiceitem',],
      activeKey: 'scmpurinvoiceitem',
      // 采购发票分录
      scmpurinvoiceitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: JVXETypes.materialSelect,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            props:{
              materialtype:'0',
            }
          },
          {
            title: '物料编号',
            key: 'materialcode',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '物料名称',
            key: 'materialname',
            type: JVXETypes.input,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '采购数量',
            key: 'qty',
            type: JVXETypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '含税单价',
            key: 'taxinprice',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '不含税单价',
            key: 'taxexprice',
            type: JVXETypes.input,
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '税率',
            key: 'taxrate',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '含税金额',
            key: 'taxinvalue',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '不含税金额',
            key: 'taxexvalue',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '备注',
            key: 'remarks',
            type: JVXETypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title:"fromtype",
            key:"fromtype",
            type:JVXETypes.hidden,
          },
        ]
      },
      url: {
        add: "/po/scmpurinvoice/add",
        edit: "/po/scmpurinvoice/edit",
        queryById: "/po/scmpurinvoice/queryById",
        scmpurinvoiceitem: {
          list: '/po/scmpurinvoice/queryScmpurinvoiceitemByMainId'
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
    formDisabled () {
      return this.disabled
    },
  },
  watch:{
    'model.companyid':{
      handler(val,old){
        localStorage.setItem("poinvoice_companyid",val);
      }
    },
    'model.vendorid':{
      handler(val,old){
        localStorage.setItem("poinvoice_vendorid",val);
      }
    }
  },
  created () {
    if(this.model.id){

    }else{
      this.model.billdate=getNowFormatDate();
    }
  },
  methods: {

    /**
     * 将选中的采购入库单赋值到采购发票上
     * @param {选中的采购入库单} selectedRowKeys 
     */
    getSelectInstockData(selectedRowKeys){
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
          if (selectedRowKeys[i] != undefined) {
            ids += selectedRowKeys[i] + ",";
          }
      }
      if(ids === ''){
        return;
      }
      let instockurl='/inventory/scminventoy/queryscminventoryitembyids';
      let params={ids:ids}
      let that=this;
      getAction(instockurl,params).then(res=>{
        if(res.success){
          let sores=res.result;
          this.scmpurinvoiceitemTable.dataSource=[]
          for(let i=0;i<sores.length;i++){
            let needqty=0;
            if(sores[i].isred === '1'){
              needqty=((sores[i].qty-sores[i].invoiceqty) *(-1)).toFixed(2);
            }else{
              needqty=((sores[i].qty-sores[i].invoiceqty) *1).toFixed(2);
            }
            if(that.model.vendorid === '' || that.model.vendor === undefined){
              //获取选中的物料行更新供应商
              let par={id:sores[i].parentid}
              getAction('/inventory/scminventoy/queryById',par).then(r=>{
                if(r.success){
                  that.model.vendorid=r.result.vendorid
                }
              })
            }
            const newColumnData={
              materialid:sores[i].materialid,
              materialcode:sores[i].materialcode,
              materialname:sores[i].materialname,
              qty:needqty,
              uomid:sores[i].uomid,
              taxinprice:sores[i].taxinprice,
              taxrate:sores[i].taxrate,
              taxexprice:sores[i].taxexprice,
              taxexvalue:(sores[i].taxexprice * needqty).toFixed(2),
              taxinvalue:parseFloat(needqty*sores[i].taxinprice).toFixed(2),
              fromitemid:sores[i].id,
              fromid:sores[i].parentid,
              fromtype:"INSTOCK",
            }
           this.scmpurinvoiceitemTable.dataSource.push(newColumnData);
          }
        }
    })
    },
    /**获取参照采购订单的数据 */
    getSelectPoData(selectedRowKeys){
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
          if (selectedRowKeys[i] != undefined) {
            ids += selectedRowKeys[i] + ",";
          }
      }
      if(ids === ''){
        return;
      }
      let sourl='/inventory/scminventoy/queryScmpoitemlistbyids';
      let params={ids:ids}
      let that=this;
      getAction(sourl,params).then(res=>{
        if(res.success){
          let sores=res.result;
          this.scmpurinvoiceitemTable.dataSource=[]
          for(let i=0;i<sores.length;i++){
            let needqty=0;
            if(sores[i].isred === '1'){
              needqty=((sores[i].qty-sores[i].invoiceqty) *(-1)).toFixed(2);
            }else{
              needqty=((sores[i].qty-sores[i].invoiceqty) *1).toFixed(2);
            }
            if(that.model.vendorid === '' || that.model.vendor === undefined){
              //获取选中的物料行更新供应商
              let par={id:sores[i].parentid}
              getAction('/po/scmpo/queryById',par).then(r=>{
                if(r.success){
                  that.model.vendorid=r.result.vendorid
                }
              })
            }
            const newColumnData={
              materialid:sores[i].materialid,
              materialcode:sores[i].materialcode,
              materialname:sores[i].materialname,
              qty:needqty,
              uomid:sores[i].uomid,
              taxinprice:sores[i].taxinprice,
              taxrate:sores[i].taxrate,
              taxexprice:sores[i].taxexprice,
              taxexvalue:(sores[i].taxexprice * needqty).toFixed(2),
              taxinvalue:parseFloat(needqty*sores[i].taxinprice).toFixed(2),
              fromitemid:sores[i].id,
              fromid:sores[i].parentid,
              fromtype:"PO",
            }
           this.scmpurinvoiceitemTable.dataSource.push(newColumnData);
          }
        }
      })
    },
    getReferinstockData(){
      this.$nextTick(() => {
        this.$refs.purinvoicereferinstock.show('其它', '采购入库单', "", "1,3", this.model.vendorid)
      })
    },
    getReferpoData(){
      this.$nextTick(() => {
        this.$refs.purinvoicereferpo.show('其它', '采购订单', "", "1,3", this.model.vendorid)
      })
    },
    addBefore () {
      this.scmpurinvoiceitemTable.dataSource = []
    },
    getAllTable () {
      let values = this.tableKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter () {
      this.$nextTick(() => {
      })
      // 加载子表数据
      if (this.model.id) {
        let params = { id: this.model.id }
        this.requestSubTableData(this.url.scmpurinvoiceitem.list, params, this.scmpurinvoiceitemTable)
      }
    },
    //校验所有一对一子表表单
    validateSubForm (allValues) {
      return new Promise((resolve, reject) => {
        Promise.all([
        ]).then(() => {
          resolve(allValues)
        }).catch(e => {
          if (e.error === VALIDATE_FAILED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      })
    },
    /** 整理成formData */
    classifyIntoFormData (allValues) {
      let main = Object.assign(this.model, allValues.formValue)
      return {
        ...main, // 展开
        scmpurinvoiceitemList: allValues.tablesValue[0].tableData,
      }
    },
    validateError (msg) {
      this.$message.error(msg)
    },

  }
}
</script>

<style scoped>

</style>