<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date placeholder="请选择单据日期" v-model="model.billdate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="销售客户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerid">
              <c-select-customer v-model="model.customerid" placeholder="请输入销售客户" :buttons="false"></c-select-customer>
            </a-form-model-item>
          </a-col>

          <a-col :xs="24" :sm="6">
            <a-form-model-item label="销售部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departid">
              <j-select-depart v-model="model.departid" placeholder="请输入销售部门" :pid="model.companyid"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="销售人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="emptid">
              <c-select-user v-model="model.emptid" placeholder="请输入销售人员" :buttons="false"></c-select-user>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="销售发票分录" :key="refKeys[0]" :forceRender="true">
        <!-- <j-editable-table :ref="refKeys[0]" :loading="scmsalesinvoiceitemTable.loading"
          :columns="scmsalesinvoiceitemTable.columns" :dataSource="scmsalesinvoiceitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" @valueChange="handleValueChange"
          :actionButton="true">
          <template slot="buttonAfter">
            <a-button type="primary" @click="getReferSoData">参照销售订单</a-button>
          </template>
        </j-editable-table> -->

        <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmsalesinvoiceitemTable.loading"
          :columns="scmsalesinvoiceitemTable.columns" 
          :dataSource="scmsalesinvoiceitemTable.dataSource" 
          :maxHeight="300"
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           :alwaysEdit="true"
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix">
            <a-button type="primary" @click="getReferSoData">参照销售订单</a-button>
          </template>
          </j-vxe-table>

      </a-tab-pane>
    </a-tabs>
    <saleinvice-refer-so ref="salesinvoicereferso" @ok="getSelectSoData"></saleinvice-refer-so>
  </a-spin>

</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
//import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'

import { validateDuplicateValue,getNowFormatDate } from '@/utils/util'
import SaleinviceReferSo from '@/views/erp/base/custom/CSaleinvoiceReferso.vue'
import { getAction } from '../../../../../api/manage'

export default {
  name: 'ScmsaleinvoiceForm',
  mixins: [JVxeTableModelMixin],
  components: {
    SaleinviceReferSo
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
      validatorRules: {
        companyid: [
            { required: true, message: '公司必填!' },
          ],
          customerid:[
          { required: true, message: '客户必填' },
          ]
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scmsalesinvoiceitem',],
      tableKeys: ['scmsalesinvoiceitem',],
      activeKey: 'scmsalesinvoiceitem',
      // 销售发票分录
      scmsalesinvoiceitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: FormTypes.sel_material,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
            props:{
              materialtype:'1',
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
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '销售数量',
            key: 'qty',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
            statistics: ['sum'],
            // validateRules:[
            //   {
            //     handler({cellValue,row,column},callback,target){
            //       if(row['taxinvalue'] < 0 && cellValue >0){
            //         callback(false,'当前行为退货行，不允许输入正数');
            //       }
            //     }
            //   }
            // ]
          },
          {
            title: '含税单价',
            key: 'taxinprice',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
          },
          {
            title: '不含税单价',
            key: 'taxexprice',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
          },
          {
            title: '税率',
            key: 'taxrate',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
          },
          {
            title: '含税金额',
            key: 'taxinvalue',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
            statistics: ['sum'],
          },
          {
            title: '不含税金额',
            key: 'taxexvalue',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: 0,
            statistics: ['sum'],
          },
          {
            title: 'fromitemid',
            key: 'fromitemid',
            type: FormTypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: 'fromid',
            key: 'fromid',
            type: FormTypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: 'parentid',
            key: 'parentid',
            type: FormTypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
        ]
      },
      url: {
        add: "/so/scmsaleinvoice/add",
        edit: "/so/scmsaleinvoice/edit",
        scmsalesinvoiceitem: {
          list: '/so/scmsaleinvoice/queryScmsalesinvoiceitemByMainId'
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
  created () {
    if(this.model.id){

    }else{
      this.model.billdate=getNowFormatDate();
    }
  },
  watch:{
    "model.companyid":{
      handler(val,old){
        localStorage.setItem("salesinvoice_companyid",val)
      }
    },
    "model.customerid":{
      handler(val,old){
        localStorage.setItem("salesinvoice_customerid",val)
      }
    }
  },
  methods: {
    /**获取参照销售订单的数据 */
    getSelectSoData(selectedRowKeys){
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
          if (selectedRowKeys[i] != undefined) {
            ids += selectedRowKeys[i] + ",";
          }
      }
      if(ids === ''){
        return;
      }
      let sourl='/so/scmso/queryitembyitemid';
      let params={ids:ids}
      getAction(sourl,params).then(res=>{
        if(res.success){
          let sores=res.result;
          this.scmsalesinvoiceitemTable.dataSource=[]
          for(let i=0;i<sores.length;i++){
            let needqty=0;
            if(sores[i].isred === '1'){
              needqty=((sores[i].qty-sores[i].invoiceqty) *(-1)).toFixed(2);
            }else{
              needqty=((sores[i].qty-sores[i].invoiceqty) *1).toFixed(2);
            }
            const newColumnData={
              materialid:sores[i].materialid,
              materialcode:sores[i].materialcode,
              qty:needqty,
              uomid:sores[i].uomid,
              taxinprice:sores[i].taxinprice,
              taxrate:sores[i].taxrate,
              taxexprice:sores[i].taxexprice,
              taxexvalue:(sores[i].taxexprice * needqty).toFixed(2),
              taxinvalue:parseFloat(needqty*sores[i].taxinprice).toFixed(2),
              fromitemid:sores[i].id,
              fromid:sores[i].parentid,
            }
           this.scmsalesinvoiceitemTable.dataSource.push(newColumnData);
          }
        }
      })
    },
    getReferSoData () {
      this.$nextTick(() => {
        this.$refs.salesinvoicereferso.show('其它', '销售订单', "", "1,3", this.model.customerid)
      })
    },
    addBefore () {
      this.scmsalesinvoiceitemTable.dataSource = []
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
        this.requestSubTableData(this.url.scmsalesinvoiceitem.list, params, this.scmsalesinvoiceitemTable)
      }
    },
    //校验所有一对一子表表单
    validateSubForm (allValues) {
      return new Promise((resolve, reject) => {
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
    classifyIntoFormData (allValues) {
      let main = Object.assign(this.model, allValues.formValue)

      return {
        ...main, // 展开
        scmsalesinvoiceitemList: allValues.tablesValue[0].tableData,
      }
    },
    validateError (msg) {
      this.$message.error(msg)
    },
    close () {
      this.visible = false
      this.$emit('close')
      this.$refs.form.clearValidate();
    },

  }
}
</script>

<style scoped>

</style>