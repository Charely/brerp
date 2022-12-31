<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="清单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入清单编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="物料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialid">
              <c-select-material v-model="model.materialid" placeholder="请输入物料" :buttons="false" ></c-select-material>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="版本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="version">
              <a-input-number v-model="model.version" placeholder="请输入版本" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isenable">
              <a-input v-model="model.isenable" placeholder="请输入启用" ></a-input>
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
      <a-tab-pane tab="物料清单分录" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="scmbillofmaterialitemTable.loading"
          :columns="scmbillofmaterialitemTable.columns"
          :dataSource="scmbillofmaterialitemTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :bordered="true"
          :toolbar="true"
          @valueChange="handleValueChange"
          >
        </j-vxe-table>
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
    name: 'ScmbillofmaterialForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 6 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 6 },
          sm: { span: 16 },
        },
        model:{
         },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['scmbillofmaterialitem', ],
        tableKeys:['scmbillofmaterialitem', ],
        activeKey: 'scmbillofmaterialitem',
        // 物料清单分录
        scmbillofmaterialitemTable: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '行号',
              key: 'itemcode',
               type: JVXETypes.input,
              width:"80px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '物料',
              key: 'materialid',
               type: JVXETypes.materialSelect,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '物料编号',
              key: 'materialcode',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '物料名称',
              key: 'materialname',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '数量',
              key: 'qty',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '计量单位',
              key: 'uomid',
               type: JVXETypes.sel_search,
              width:"200px",
              placeholder: '请输入${title}',
              dictCode: 'scmuom,uomname,id',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/base/scmbillofmaterial/add",
          edit: "/base/scmbillofmaterial/edit",
          queryById: "/base/scmbillofmaterial/queryById",
          scmbillofmaterialitem: {
            list: '/base/scmbillofmaterial/queryScmbillofmaterialitemByMainId'
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
        this.scmbillofmaterialitemTable.dataSource=[]
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
          this.requestSubTableData(this.url.scmbillofmaterialitem.list, params, this.scmbillofmaterialitemTable)
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
          scmbillofmaterialitemList: allValues.tablesValue[0].tableData,
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