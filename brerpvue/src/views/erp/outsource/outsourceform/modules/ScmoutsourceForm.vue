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
          <!-- <a-col :span="6" >
            <a-form-model-item label="委外类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="outsourcetypeid">
              <a-input v-model="model.outsourcetypeid" placeholder="请输入委外类型" ></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :span="6" >
            <a-form-model-item label="业务日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入业务日期" ></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" >
            <a-form-model-item label="委外供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-vendor v-model="model.vendorid" placeholder="请输入委外供应商" :buttons="false" ></c-select-vendor>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="委外部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="invdepartid">
              <j-select-depart v-model="model.deptid" placeholder="请输入委外部门"  :pid="model.companyid" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="委外人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="invemployid">
              <c-select-user v-model="model.emptid" placeholder="请输入委外人员" :buttons="false" />
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
      <a-tab-pane tab="委外订单分录" :key="refKeys[0]" :forceRender="true">
        <a-row>
          <a-col :span="16">
            <j-vxe-table 
          keep-source
          :ref="refKeys[0]"
          :loading="scmoutsourceitemTable.loading"
          :columns="scmoutsourceitemTable.columns"
          :dataSource="scmoutsourceitemTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowSelectionType="radio"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          @selectRowChange="selectRowChange"
          @added="added"
          @valueChange="handleValueChange">

          <template slot="toolbarSuffix">
            <a-button type="primary" @click="getBomInfo">分解BOM</a-button>
          </template>
        </j-vxe-table>
          </a-col>
          <a-col :span="8">
            <j-vxe-table 
          keep-source
          :ref="scmoutsourceboms"
          :loading="scmoutsourceboms.loading"
          :columns="scmoutsourceboms.columns"
          :dataSource="scmoutsourceboms.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="false"
          :toolbar="true"
          @selectRowChange="selectRowChange"
          @valueChange="handleValueChange">
        </j-vxe-table>
          </a-col>
        </a-row>
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
import JSelectDepart from '../../../../../components/jeecgbiz/JSelectDepart.vue'
  import { uuid } from '@/api/erp/commonapi.js'

  export default {
    name: 'ScmoutsourceForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer
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
          vendorid:[{required:true}]
        },
        selectData:'',
        bominfoData:[],
        refKeys: ['scmoutsourceitem', ],
        tableKeys:['scmoutsourceitem', ],
        activeKey: 'scmoutsourceitem',
        // 委外订单分录
        scmoutsourceitemTable: {
          loading: false,
          dataSource: [],
          columns: [

            {
              title: '物料',
              key: 'materialid',
              type: JVXETypes.materialSelect,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [
              {
                required: true, // 必填
                message: '${title}不能为空' // 提示的文本
              },
            ],
              Props:{
                materialtype:'0',
              },
          
            },
            {
              title: '物料编号',
              key: 'materialcode',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '物料名称',
              key: 'materialname',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '委外数量',
              key: 'qty',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '计量单位',
              key: 'uomid',
              type: JVXETypes.sel_search,
            dictCode: 'scmuom,uomname,id',
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '含税单价',
              key: 'taxinprice',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '不含税单价',
              key: 'taxexprice',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '税率',
              key: 'taxrate',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '含税金额',
              key: 'taxinvalue',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '不含税金额',
              key: 'taxexvalue',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '备注',
              key: 'remarks',
               type: JVXETypes.input,
              width:"100px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title:'bomitemid',
              key:'bomitemid',
              type:JVXETypes.hidden,
              width:'100px',
            }
          ]
        },

        scmoutsourceboms:{
          loading:false,
          dataSource:[],
          copyDataSource:[],
          columns:[
            {
              title:'物料',
              key:'materialid',
              type:JVXETypes.materialSelect,
              dataIndex:'materialid'
            },
            {
              title:'数量',
              key:'qty',
              type:JVXETypes.inputNumber,
              dataIndex:'qty',
            },
            {
              title:'',
              key:'parentid',
              type:JVXETypes.hidden,
              dataIndex:'parentid',
            }
          ]
        },
        url: {
          add: "/outsource/scmoutsource/add",
          edit: "/outsource/scmoutsource/edit",
          queryById: "/outsource/scmoutsource/queryById",
          bominfo:"/base/scmbillofmaterial/getbominfobymaterialid",
          querybominfobyitemid:'/outsource/scmoutsource/queryscmoutsourcebominfo',
          scmoutsourceitem: {
            list: '/outsource/scmoutsource/queryScmoutsourceitemByMainId'
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
      getBomInfo(){
        console.log("this.selectedRowIds...."+JSON.stringify(this.selectData));
        if(this.selectData[0].qty === undefined || this.selectData[0].qty === ''){
          this.$message.error("请先输入数量！");
          return;
        }
        let orgQty=this.selectData[0].qty;

        //TODO 获取当前物料的bom信息自动填充到当前BOM信息中去
          getAction(this.url.bominfo,{id:this.selectData[0].materialid}).then(res=>{
            if(res.success){
              this.bominfoData=res.result;
              for(let j=0;j<this.scmoutsourceboms.copyDataSource.length;j++){
                if(this.scmoutsourceboms.copyDataSource[j].parentid === this.selectData[0].bomitemid){
                  this.scmoutsourceboms.copyDataSource.splice(j)
                }
              }
              this.scmoutsourceboms.dataSource=[];
              for(let i=0;i<this.bominfoData.length;i++){
                let curNewData={
                  parentid:this.selectData[0].bomitemid,
                  materialid:this.bominfoData[i].materialid,
                  qty:(this.bominfoData[i].qty * orgQty).toFixed(2)
                };
                this.scmoutsourceboms.dataSource.push(curNewData);
                this.scmoutsourceboms.copyDataSource.push(curNewData);
              }
            }
          })
    
      },
      selectRowChange(event){
        let { type,action,row,selectedRows,selectedRowIds } =event
        if(action="selected"){
          if(this.model.id){
             let curitemid=row.bomitemid;
            // this.scmoutsourceboms.dataSource=[];
             getAction(this.url.querybominfobyitemid,{itemid:curitemid}).then(res=>{
              if(res.success){
                this.scmoutsourceboms.dataSource=res.result;
              }
             })
          }else{
              this.selectData = selectedRows
              if(this.scmoutsourceboms.copyDataSource.length>0){
                this.scmoutsourceboms.dataSource=[];
                for(let i=0;i<this.scmoutsourceboms.copyDataSource.length;i++){
                  if(row.bomitemid === this.scmoutsourceboms.copyDataSource[i].parentid){
                    this.scmoutsourceboms.dataSource.push(this.scmoutsourceboms.copyDataSource[i]);
                  }
                }
              }else{
                this.scmoutsourceboms.dataSource=[];
              }
           }
        }
      },
      addBefore(){
        this.scmoutsourceitemTable.dataSource=[]
      },
      added(event){
        let { row,table,target} = event;
        row.bomitemid=row.id;
        console.log(event);
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
          this.requestSubTableData(this.url.scmoutsourceitem.list, params, this.scmoutsourceitemTable)
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
          scmoutsourceitemList: allValues.tablesValue[0].tableData,
          scmoutsourceitembomList:this.scmoutsourceboms.copyDataSource,
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