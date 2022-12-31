<template>
  <a-spin :spinning="confirmLoading">
    <div>
      <act-handle-btn :type="0" :taskId="audittaskid" :dataId="model.id" v-if="auditDisabled" text="通过">
      </act-handle-btn>
      <act-handle-btn :type="1" :taskId="audittaskid" :dataId="model.id" v-if="auditDisabled" text="驳回">
      </act-handle-btn>
    </div>
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="计划编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prcode" >
              <a-input v-model="model.prcode" placeholder="请输入计划编号" disabled="true"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="计划日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prdate">
              <j-date placeholder="请选择计划日期" v-model="model.prdate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="采购部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prdept">
              <j-select-depart v-model="model.prdept" :pid="model.companyid"   :multi="false" placeholder="请输入采购部门"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="采购人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="prempid">
              <c-select-user :buttons="false" :multi="false" v-model="model.prempid" placeholder="请输入采购人员"></c-select-user>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="计划备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入计划备注"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6" v-for="(item,index) in maincusList" :key="index">
            <a-form-model-item :label="item.customname" :labelCol="labelCol" :wrapperCol="wrapperCol"
              prop="item.customcode">
              <a-input v-model="maincusItems[item.customcode]" placeholder="请输入"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <link-bill-list ref="linkBillList" @ok="linkBillListOk"></link-bill-list>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="采购计划分录" :key="refKeys[0]" :forceRender="true">
      
          <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmpritemTable.loading"
          :columns="scmpritemTable.columns" 
          :dataSource="scmpritemTable.dataSource" 
          :maxHeight="300"
          
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix">
            <a-button type="primary" @click="onSearchPreqitem">参照采购申请</a-button>
          </template>
          </j-vxe-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>

</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
// import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue,getNowFormatDate,addDays,format_date } from '@/utils/util'
import LinkBillList from '../../base/custom/LinkBillList.vue'
import { getAction } from '../../../../api/manage'
import json5 from 'json5'
//import JFormContainer from '@/components/jeecg/JFormContainer'
// import ActHandleBtn from "@views/flowable/components/ActHandleBtn";
import {queryMaterialVoInfoById} from "@/api/erp/commonapi"
// import { handleValueChange } from '@/utils/erputils/erpcommonutils'


export default {
  name: 'ScmprForm',
  mixins: [JVxeTableModelMixin],
  components: {
    LinkBillList,
    ActHandleBtn
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
            { required: true, message: '请选择公司!' },
          ],
      },
      refKeys: ['scmpritem',],
      tableKeys: ['scmpritem',],
      activeKey: 'scmpritem',
      auditvisable: false,
      audittaskid: '',
      maincusObject: 'scmpr',
      itemcusObject: 'scmpritem',
      maincusList: [],
      maincusItems: {},
      itemTable: {},
      // 采购计划分录
      scmpritemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: FormTypes.sel_material,
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
            type: FormTypes.input,
            disabled: true,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '物料名称',
            key: 'materialname',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
            {
              title: '计量单位',
              key: 'uomid',
              type: FormTypes.sel_search,
              width:"100px",
              dictCode:'scmuom,uomname,id',
              placeholder: '请输入${title}',
              defaultValue:'',
            },
        
          {
            title: '计划数量',
            key: 'qty',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '计划单价',
            key: 'taxinprice',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',

          },
          {
            title: '计划金额',
            key: 'taxinvalue',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum'],
          },
          {
            title: '需求日期',
            key: 'prdate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '订货日期',
            key: 'prorderDate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '建议供应商',
            key: 'prvendorid',
            type: FormTypes.sel_search,
            dictCode:"scmpartner ,partnername,id",
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            //title: 'fromitemid',
            key: 'fromitemid',
            colSpan: 0,
            type: FormTypes.hidden,
            customRender: (value) => {
              let obj = {
                children: value,
                attrs: {},
              }
              obj.attrs.colSpan = 0;
              return obj;
            }
          },
        ]
      },
      url: {
        add: "/pr/scmpr/add",
        edit: "/pr/scmpr/edit",
        queryById: "/pr/scmpr/queryById",
        scmpritem: {
          list: '/pr/scmpr/queryScmpritemByMainId'
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
    auditDisabled () {
      return this.auditvisable;
    },
  },
  created () {
    //this.model.prcode = Date.now().toString();

    this.itemTable = this.scmpritemTable;
    console.log("router.params:" + JSON.stringify(this.$route.params))
    let routeparams = this.$route.params;
    if (routeparams != undefined && (routeparams.id != '' && routeparams.id != undefined)) {
      let params = { id: routeparams.id }
      //获取主表信息
      this.getMainData("/pr/scmpr/queryById", params);
      //获取分录信息
      this.requestSubTableData(this.url.scmpritem.list, params, this.scmpritemTable)
      this.disabled = true;
      this.auditvisable = true;
      this.audittaskid = routeparams.taskId;
    }

    if(this.model.id){

    }else{
      this.model.prdate=getNowFormatDate();
    }

    //获取自定义字段信息，自动添加到当前页面上
  },
  methods: {
    linkBillListOk (selectedRowKeys) {
      let ids = "";
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      //console.log("currentids:"+selectedRowKeys[i])
      let url = "/preq/scmpreq/queryScmpreqitemById";
      let param = { ids: ids };
      getAction(url, param).then(async res => {
        if (res) {
          let resu = res.result.records || res.result;
          //console.log("resu" + resu);
          //todo 给新增行赋值
          let info = [];
          if (resu != null && resu.length > 0) {
            for (let i = 0; i < resu.length; i++) {

              let params={materialid:resu[i].materialid}
              let curmaterials=await queryMaterialVoInfoById(params)
              let curprice=0;
              let fixedPoDate=0;
              if(curmaterials.result === null || curmaterials.result === undefined){
              }else{
                if(curmaterials.result.poprice === null || curmaterials.result.poprice === undefined){
                }else{
                  curprice=curmaterials.result.poprice
                }
                if(!(curmaterials.result.fixedPoDate === null 
                    || curmaterials.result.fixedPoDate === undefined)){
                  fixedPoDate=curmaterials.result.fixedPoDate
                }
              }
              let needorderdate=resu[i].preqdate
              if(!(resu[i].preqdate === null || resu[i].preqdate === undefined)){
                  needorderdate=addDays('-'+fixedPoDate,new Date(resu[i].preqdate));
              }      
              //处理应订货日期
              const newData = {
                    materialid: resu[i]["materialid"],
                    materialcode:resu[i]["materialcode"],
                    materialname:resu[i]["materialname"],
                    qty: resu[i]["qty"],
                    uomid:resu[i]['uomid'],
                    taxinprice: curprice,
                    taxinvalue: (curprice * (resu[i]["qty"])).toFixed(2),
                    fromid: resu[i]["parentid"],
                    fromitemid: resu[i]["id"],
                    prdate: resu[i]["preqdate"],
                    prorderDate: needorderdate
                 }

              info.push(newData);
            }
            this.scmpritemTable.dataSource = info;
          }
        }
      })
    },
    onSearchPreqitem () {
      if(this.model.companyid === '' || this.model.companyid === undefined){
        this.$message.error("请先选择公司");
        return ;
      }
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.linkBillList.show('其它', '采购订单', "", "1,3",this.model.companyid)
      })
    },
    addBefore () {
      this.scmpritemTable.dataSource = []
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
        this.requestSubTableData(this.url.scmpritem.list, params, this.scmpritemTable)
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
     // allValues.formValue=Object.assign({},this.maincusItems);
      let main = Object.assign(this.model, allValues.formValue)

      main=Object.assign(this.model,this.maincusItems);
      
      return {
        ...main, // 展开
        scmpritemList: allValues.tablesValue[0].tableData,
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