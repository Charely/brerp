<template>
   <a-spin :spinning="confirmLoading">
     <j-form-container :disabled="formDisabled">
       <!-- 主表单区域 -->
       <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
         <a-row>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="清单编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入清单编码" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="清单名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入清单名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="物料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialid">
              <c-select-material v-model="model.materialid" placeholder="请选择物料" :button="false"/>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="有效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="canenable">
              <j-switch v-model="model.canenable"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="有效期从" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromdate">
              <j-date placeholder="请选择有效期从" v-model="model.fromdate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="有效期止" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enddate">
              <j-date placeholder="请选择有效期止" v-model="model.enddate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
     </j-form-container>
      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="货源清单分录" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="materialvendorlinkitemTable.loading"
            :columns="materialvendorlinkitemTable.columns"
            :dataSource="materialvendorlinkitemTable.dataSource"
            :maxHeight="300"
            :disabled="formDisabled"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
</template>

<script>

  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'MaterialvendorlinkForm',
    mixins: [JEditableTableModelMixin],
    components: {
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
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        refKeys: ['materialvendorlinkitem', ],
        tableKeys:['materialvendorlinkitem', ],
        activeKey: 'materialvendorlinkitem',
        itemTable:{},
        // 货源清单分录
        materialvendorlinkitemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '供应商',
              key: 'vendorid',
              type: FormTypes.sel_vendor,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title:'固定供应商',
              key:'fixedvendor',
              type:FormTypes.checkbox,
              width:"100px",
              placeholder:'请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: '物料编码',
            //   key: 'materialcode',
            //   type: FormTypes.input,
            //   width:"100px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            // {
            //   title: '物料名称',
            //   key: 'materialname',
            //   type: FormTypes.input,
            //   width:"100px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
            {
              title: '最大供货数量',
              key: 'maxqty',
              type: FormTypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '最小供货数量',
              key: 'minqty',
              type: FormTypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '订货次数',
              key: 'orderbatch',
              type: FormTypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '累计订货数量',
              key: 'sumorderqty',
              type: FormTypes.input,
              disabled:true,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '累计订货金额',
              key: 'sumordervalue',
              type: FormTypes.input,
              disabled:true,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '累计订货次数',
              key: 'sumorderbatch',
              type: FormTypes.input,
              disabled:true,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '最近订单编号',
              key: 'lastpocode',
              type: FormTypes.input,
              disabled:true,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/po/materialvendorlink/add",
          edit: "/po/materialvendorlink/edit",
          materialvendorlinkitem: {
            list: '/po/materialvendorlink/queryMaterialvendorlinkitemByMainId'
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
      this.itemTable=this.materialvendorlinkitemTable
    },
    methods: {
     addBefore(){
            this.materialvendorlinkitemTable.dataSource=[]
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
          this.requestSubTableData(this.url.materialvendorlinkitem.list, params, this.materialvendorlinkitemTable)
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
          materialvendorlinkitemList: allValues.tablesValue[0].values,
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