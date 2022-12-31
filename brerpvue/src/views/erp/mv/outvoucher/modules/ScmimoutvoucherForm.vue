<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="6" >
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="业务日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date placeholder="请选择业务日期" v-model="model.billdate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col  :sm="6">
            <a-form-model-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseid">
              <j-search-select-tag ref="wfref" v-model="model.warehouseid" placeholder="请输入仓库" :buttons="false"
                dict= "warehouse,name,id" :async="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vdeptid">
              <j-select-depart v-model="model.vdeptid" placeholder="请输入部门" :pid="this.model.companyid"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vemptid">
              <c-select-user v-model="model.vemptid" placeholder="请输入人员"  :buttons="false" ></c-select-user>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入备注" ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="入库凭证分录" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="scmiminvoucheritemTable.loading"
          :columns="scmiminvoucheritemTable.columns"
          :dataSource="scmiminvoucheritemTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import { getRefPromise,VALIDATE_FAILED} from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'ScmimoutvoucherForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer,
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['scmiminvoucheritem', ],
        tableKeys:['scmiminvoucheritem', ],
        activeKey: 'scmiminvoucheritem',
        // 入库凭证分录
        scmiminvoucheritemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '物料',
              key: 'materialid',
               type: JVXETypes.materialSelect,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '数量',
              key: 'qty',
               type: JVXETypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '入库单价',
              key: 'taxinprice',
               type: JVXETypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '入库金额',
              key: 'taxinvalue',
               type: JVXETypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '仓库',
              key: 'warehouseid',
               type: JVXETypes.hidden,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '货位',
              key: 'stocklocationid',
              width: "200px",
              type: JVXETypes.popup,
              popupCode:'stocklocationhelp',
              field:'stocklocationname',
              orgFields:'id,locationname',
              destFields:'stocklocationid,stocklocationname',
              placeholder: '请输入${title}',
              defaultValue: '',
          },
            {
              title: '批号',
              key: 'batchid',
               type: JVXETypes.batchSelect,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '库存类型',
              key: 'inventorykindid',
              width: "120px",
              type: JVXETypes.sel_search,
              dictCode: 'scminventorykinds,kindname,kindcode',
              options: [],
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
           
          ]
        },
        url: {
          add: "/im/scmiminvoucher/add",
          edit: "/im/scmiminvoucher/edit",
          queryById: "/im/scmiminvoucher/queryById",
          scmiminvoucheritem: {
            list: '/im/scmiminvoucher/queryScmiminvoucheritemByMainId'
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
    },
    methods: {
      addBefore(){
        this.scmiminvoucheritemTable.dataSource=[]
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
          this.requestSubTableData(this.url.scmiminvoucheritem.list, params, this.scmiminvoucheritemTable)
        }
      },
      //校验所有一对一子表表单
        validateSubForm(allValues){
            return new Promise((resolve,reject)=>{
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
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          scmiminvoucheritemList: allValues.tablesValue[0].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>