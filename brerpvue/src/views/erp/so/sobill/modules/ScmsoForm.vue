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
              <a-input v-model="model.billcode" placeholder="请输入单据编号" disabled="true" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入单据日期" ></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="销售部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sodepartid">
              <j-select-depart v-model="model.sodepartid" placeholder="请输入销售部门" :pid="model.companyid"/>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="销售人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="soempid">
              <c-select-user v-model="model.soempid" placeholder="请输入销售人员" :buttons="false" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customerid">
              <c-select-customer v-model="model.customerid" placeholder="请输入客户" :buttons="false" />
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
        <a-tab-pane tab="销售订单分录" :key="refKeys[0]" :forceRender="true">
            <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmsoitemTable.loading"
          :columns="scmsoitemTable.columns" 
          :dataSource="scmsoitemTable.dataSource" 
          :maxHeight="300"
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           :alwaysEdit="true"
           @valueChange="handleValueChange">
          </j-vxe-table>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
</template>

<script>

  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  //import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'

  import { validateDuplicateValue,getNowFormatDate } from '@/utils/util'
  import CSelectCustomer from '../../../base/custom/CSelectCustomer.vue'
import CSelectUser from '../../../base/custom/CSelectUser.vue'
import { handleValueChange } from '@/utils/erputils/erpcommonutils'
  export default {
    name: 'ScmsoForm',
    mixins: [JVxeTableModelMixin],
    components: {
      CSelectCustomer,
        CSelectUser
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
          companyid: [
            { required: true, message: '公司必填!' },
          ],
          customerid:[
          { required: true, message: '客户必填' },
          ]
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        refKeys: ['scmsoitem', ],
        tableKeys:['scmsoitem', ],
        activeKey: 'scmsoitem',
        itemTable:{},
        itemcusObject:'scmsoitem',
        // 销售订单分录
        scmsoitemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料',
              key: 'materialid',
              type: FormTypes.sel_material,
              width:"150px",
              placeholder: '请输入${title}',
              defaultValue:'',
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
              title: '物料编号',
              key: 'materialname',
              type: FormTypes.hidden,
              width: "100px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '计量单位',
              key: 'uomid',
              type: FormTypes.sel_search,
              dictCode:'scmuom,uomname,id',
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '销售数量',
              key: 'qty',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              statistics: ['sum']
            },
            {
              title: '含税单价',
              key: 'taxinprice',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '不含税单价',
              key: 'taxexprice',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '税率',
              key: 'taxrate',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '含税总价',
              key: 'taxinvalue',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              statistics: ['sum']
            },
            {
              title: '不含税总价',
              key: 'taxexvalue',
              type: FormTypes.inputNumber,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              statistics: ['sum']
            },
            {
              title: '备注',
              key: 'remarks',
              type: FormTypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: 'parentid',
            //   key: 'parentid',
            //   type: FormTypes.input,
            //   width:"100px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
          ]
        },
        url: {
          add: "/so/scmso/add",
          edit: "/so/scmso/edit",
          scmsoitem: {
            list: '/so/scmso/queryScmsoitemByMainId'
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
      this.itemTable=this.scmsoitemTable
      if(this.model.id){

      }else{
        this.model.billdate=getNowFormatDate()
      }
    },
    methods: {
     handleValueChange(event){
      handleValueChange(event)
     },
     addBefore(){
            this.scmsoitemTable.dataSource=[]
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
          this.requestSubTableData(this.url.scmsoitem.list, params, this.scmsoitemTable)
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
          scmsoitemList: allValues.tablesValue[0].tableData,
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