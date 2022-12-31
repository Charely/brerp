<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="8" >
            <a-form-model-item label="核算组织" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请输入核算组织"  dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" >
            <a-form-model-item label="核算范围编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hscode">
              <a-input v-model="model.hscode" placeholder="请输入核算范围编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" >
            <a-form-model-item label="核算范围名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hsname">
              <a-input v-model="model.hsname" placeholder="请输入核算范围名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" >
            <a-form-model-item label="是否启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hsisvalid">
              <a-checkbox v-model="model.hsisvalid" placeholder="请输入是否启用" ></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" >
            <a-form-model-item label="核算层次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hslevel">
              <j-search-select-tag v-model="model.hslevel" placeholder="请输入核算层次" dict='scmhslevel,name,id'/>
            </a-form-model-item>
          </a-col>
          <a-col :span="8" >
            <a-form-model-item label="统一计价方法" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hslevel">
              <j-search-select-tag v-model="model.jjmethod" placeholder="请选择统一计价方法" dict="scmjjmethod,jjname,jjcode" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="核算范围分录" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="scmhsfwitemTable.loading"
          :columns="scmhsfwitemTable.columns"
          :dataSource="scmhsfwitemTable.dataSource"
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
    name: 'ScmhsfwForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 8 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 8 },
          sm: { span: 16 },
        },
        model:{
         },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['scmhsfwitem', ],
        tableKeys:['scmhsfwitem', ],
        activeKey: 'scmhsfwitem',
        // 核算范围分录
        scmhsfwitemTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '仓库编号',
              key: 'warehouseid',
               type: JVXETypes.sel_search,
              dictCode:"warehouse,name,id",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            // {
            //   title: 'parentid',
            //   key: 'parentid',
            //    type: JVXETypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue:'',
            // },
          ]
        },
        url: {
          add: "/mv/scmhsfw/add",
          edit: "/mv/scmhsfw/edit",
          queryById: "/mv/scmhsfw/queryById",
          scmhsfwitem: {
            list: '/mv/scmhsfw/queryScmhsfwitemByMainId'
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
        this.scmhsfwitemTable.dataSource=[]
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
          this.requestSubTableData(this.url.scmhsfwitem.list, params, this.scmhsfwitemTable)
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
          scmhsfwitemList: allValues.tablesValue[0].tableData,
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