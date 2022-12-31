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
            <a-form-model-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入单据日期" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseid">
              <j-search-select-tag ref="wfref" v-model="model.warehouseid" placeholder="请输入仓库" :buttons="false"
                dict= "warehouse,name,id" :async="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="盘点部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deptid">
              <j-select-depart v-model="model.deptid" placeholder="请输入库存部门" :pid="this.model.companyid"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="盘点人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="emptid">
              <c-select-user v-model="model.emptid" placeholder="请输入盘点人员" :buttons="false" />
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
      <a-tab-pane tab="盘点单分录" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="scminventorycheckitemTable.loading"
          :columns="scminventorycheckitemTable.columns"
          :dataSource="scminventorycheckitemTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :alwaysEdit="true"
          :rowSelection="true"
          :toolbar="true"
          @valueChange="handleValueChange"
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
    name: 'ScminventorycheckForm',
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
          companyid:[{required:true,message:'请选择公司'}],
          //billcode: [{ required: true, message: '请选择单据编号' }],
          warehouseid: [{ required: true, message: '请选择仓库' }]
        },
        refKeys: ['scminventorycheckitem', ],
        tableKeys:['scminventorycheckitem', ],
        activeKey: 'scminventorycheckitem',
        // 盘点单分录
        scminventorycheckitemTable: {
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
              title: '物料编号',
              key: 'materialcode',
               type: JVXETypes.input,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '物料名称',
              key: 'materialname',
               type: JVXETypes.hidden,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '计量单位',
              key: 'uomid',
              type: JVXETypes.sel_search,
              dictCode: 'scmuom,uomname,id',
              width: "120px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '货位',
              key: 'stocklocationid',
              type: JVXETypes.popup,
              popupCode:'stocklocationhelp',
              field:'stocklocationname',
              orgFields:'id,locationname',
              destFields:'stocklocationid,stocklocationname',
              placeholder: '请输入${title}',
              defaultValue:'',
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
            title: '库存状态',
            key: 'stocktypeid',
            width: "120px",
            type: JVXETypes.sel_search,
            dictCode: 'scmstocktype,stockname,stockcode',
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
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
            {
              title: '账本余额',
              key: 'budgetqty',
              type: JVXETypes.inputNumber,
              disabled:true,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '实际余额',
              key: 'realityqty',
               type: JVXETypes.inputNumber,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/inventory/scminventorycheck/add",
          edit: "/inventory/scminventorycheck/edit",
          queryById: "/inventory/scminventorycheck/queryById",
          scminventorycheckitem: {
            list: '/inventory/scminventorycheck/queryScminventorycheckitemByMainId'
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
    watch:{
      'model.companyid':{
          handler(val,old){
          // this.warehousedict="warehouse where companyid ="+val+',name,code';
          if(!this.model.id){
            this.$refs.wfref.newdict="warehouse where companyid = '"+val+"',name,id";
            if(val != old){
              this.model.warehouseid=''
            }
            }

            let currentuser = this.$store.getters.userInfo;
            localStorage.setItem(currentuser.id+"_outstock_companyid",val);
      },
    },
      'model.warehouseid':{
        immediate:true,
        handler(val,old){
          let currentuser = this.$store.getters.userInfo;
          localStorage.setItem(currentuser.id+"_outstock_warehouseid",val);
          for(let i=0;i<this.scminventorycheckitemTable.columns.length;i++){
            if(this.scminventorycheckitemTable.columns[i].key === 'stocklocationid'){
              let curcolumn=this.scminventorycheckitemTable.columns[i];
              if(!curcolumn.hasOwnProperty("param")){
                this.$set(curcolumn,'param',{ownerwhid:val});
              } else{
                delete curcolumn.param ;
                this.$set(curcolumn,'param',{ownerwhid:val});
              }
              break;
            }
          }
        }
      }
    },
    methods: {
      addBefore(){
        this.scminventorycheckitemTable.dataSource=[]
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
          this.requestSubTableData(this.url.scminventorycheckitem.list, params, this.scminventorycheckitemTable)
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
          scminventorycheckitemList: allValues.tablesValue[0].tableData,
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