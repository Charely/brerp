<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <!-- <a-col :xs="24" :sm="6">
            <a-form-model-item label="库存组织" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imorgid">
              <a-input v-model="model.imorgid" placeholder="请输入库存组织"></a-input>
            </a-form-model-item>
          </a-col> -->
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromwarehouseid">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" :disabled="true"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromwarehouseid">
              <j-date v-model="model.billdate" placeholder="请输入单据日期"></j-date>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="移出仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromwarehouseid">
              <j-search-select-tag ref='fws' v-model="model.fromwarehouseid" placeholder="请输入移出仓库" dict="warehouse,name,id" async="true">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="移入仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="towarehouseid">
              <j-search-select-tag ref="tws" v-model="model.towarehouseid" placeholder="请输入移入仓库" dict="warehouse,name,id" async="true">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="库存部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transferdeptid">
              <j-select-depart v-model="model.transferdeptid" placeholder="请输入库存部门"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="库存人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transferemptid">
              <c-select-user v-model="model.transferemptid" :buttons="false" placeholder="请输入库存人员"></c-select-user>
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
      <a-tab-pane tab="移库单分录" :key="refKeys[0]" :forceRender="true">
        <!-- <j-editable-table :ref="refKeys[0]" :loading="scmtransferbillitemTable.loading"
          :columns="scmtransferbillitemTable.columns" :dataSource="scmtransferbillitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" 
          :rowSelection="true" :actionButton="true" /> -->

        <j-vxe-table keep-source :ref="refKeys[0]" :loading="scmtransferbillitemTable.loading"
          :columns="scmtransferbillitemTable.columns" :dataSource="scmtransferbillitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" :toolbar="true" :bordered="true"
          :alwaysEdit="true" @valueChange="handleValueChange">
        </j-vxe-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
// import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue } from '@/utils/util'
//import { getRefPromise, VALIDATE_FAILED } from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
import JFormContainer from '@/components/jeecg/JFormContainer'
import { JVXETypes } from '@/components/jeecg/JVxeTable/index';

export default {
  name: 'ScmtransferbillForm',
  mixins: [JVxeTableModelMixin],
  components: {
    JFormContainer,
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
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scmtransferbillitem',],
      tableKeys: ['scmtransferbillitem',],
      activeKey: 'scmtransferbillitem',
      // 移库单分录
      scmtransferbillitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: FormTypes.sel_material,
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
            props: {
              materialtype: '2',
            }
          },
          {
            title: '计量单位',
            key: 'uomid',
            type: FormTypes.sel_search,
            dictCode: 'scmuom,uomname,id',
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          {
            title: '移库数量',
            key: 'qty',
            type: FormTypes.inputNumber,
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
            statistics: ['sum']
          },
          {
            title: '移出货位',
            key: 'fromstocklocationid',
            width: "200px",
            type: JVXETypes.popup,
            popupCode:'stocklocationhelp',
            field:'fromstocklocationname',
            orgFields:'id,locationname',
            destFields:'fromstocklocationid,fromstocklocationname',
            placeholder: '请输入${title}',
            defaultValue: '',
          },  
          {
            title: '移入货位',
            key: 'tostocklocationid',
            width: "200px",
            type: JVXETypes.popup,
            popupCode:'stocklocationhelp',
            field:'tostocklocationname',
            orgFields:'id,locationname',
            destFields:'tostocklocationid,tostocklocationname',
            placeholder: '请输入${title}',
            defaultValue: '',
          }, 
          {
            title: '批次编号',
            key: 'batchid',
            type: JVXETypes.batchSelect,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '账本数量',
            key: 'balanceqty',
            type: FormTypes.input,
            disabled: true,
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '库存状态',
            key: 'stocktypeid',
            type: FormTypes.sel_search,
            dictCode: 'scmstocktype,stockname,stockcode',
            width:'120px',
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          {
            title: '库存类型',
            key: 'inventorykindid',
            type: FormTypes.sel_search,
            dictCode: 'scminventorykinds,kindname,kindcode',
            width:'120px',
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          {
            title: '备注',
            key: 'remarks',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
        ]
      },
      url: {
        add: "/inventory/scmtransferbill/add",
        edit: "/inventory/scmtransferbill/edit",
        scmtransferbillitem: {
          list: '/inventory/scmtransferbill/queryScmtransferbillitemByMainId'
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
  watch:{
    "model.companyid":{
      handler(val,old){
        let currentuser = this.$store.getters.userInfo
        localStorage.setItem(currentuser.id+"_outstock_companyid",val);
        if(!this.model.id){
          this.$refs.fws.newdict="warehouse where companyid = '"+val+"',name,id";
          this.$refs.tws.newdict="warehouse where companyid ='"+val+"',name,id";
          if(val != old){
            this.model.fromwarehouseid=''
            this.model.towarehouseid=''
          }
        }
      }
    },
    "model.fromwarehouseid":{
      handler(val,old){
        let currentuser = this.$store.getters.userInfo
        localStorage.setItem(currentuser.id+"_outstock_warehouseid",val);
        if(val){
          for(let i=0;i<this.scmtransferbillitemTable.columns.length;i++){
            if(this.scmtransferbillitemTable.columns[i].key === 'fromstocklocationid'){
              let curcolumn=this.scmtransferbillitemTable.columns[i];
              if(!curcolumn.hasOwnProperty("param")){
                this.$set(curcolumn,'param',{ownerwhid:val});
              } else{
                delete curcolumn.param ;
                this.$set(curcolumn,'param',{ownerwhid:val});
                // curcolumn.param={ownerwhid:val};
              }
              break;
            }
          }
        }
      }
    },
    'model.towarehouseid':{
      handler(val){
       if(val){
        for(let i=0;i<this.scmtransferbillitemTable.columns.length;i++){
            if(this.scmtransferbillitemTable.columns[i].key === 'tostocklocationid'){
              let curcolumn=this.scmtransferbillitemTable.columns[i];
              if(!curcolumn.hasOwnProperty("param")){
                this.$set(curcolumn,'param',{ownerwhid:val});
              } else{
                delete curcolumn.param ;
                this.$set(curcolumn,'param',{ownerwhid:val});
                // curcolumn.param={ownerwhid:val};
              }
              break;
            }
          }
       }
      }
    }
  },
  computed: {
    formDisabled () {
      return this.disabled
    },
  },
  created () {
  },
  methods: {
    addBefore () {
      this.scmtransferbillitemTable.dataSource = []
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
        this.requestSubTableData(this.url.scmtransferbillitem.list, params, this.scmtransferbillitemTable)
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
        scmtransferbillitemList: allValues.tablesValue[0].tableData,
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