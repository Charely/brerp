<template>
  <a-card>
    <a-spin :spinning="confirmLoading">
      <div>
      </div>
      <div>
        <j-form-container :disabled="formDisabled">
          <!-- 主表单区域 -->
          <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
            <a-row>
              <a-col :span="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="业务日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="preqcode">
                  <j-date v-model="model.billdate" placeholder="请选择业务日期"></j-date>
                </a-form-model-item>
              </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="申请编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="preqcode">
                  <a-input v-model="model.preqcode" placeholder="请输入申请编号" disabled></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="申请部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="preqdept">
                  <!-- <a-input v-model="model.preqdept" placeholder="请输入申请部门" ></a-input> -->
                  <j-select-depart v-model="model.preqdept" 
                  :trigger-change="true" customReturnField="id"
                    :multi="false"
                    :pid="model.companyid"
                    >
                  </j-select-depart>
                </a-form-model-item>
              </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="申请人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="preqemp">
                  <!-- <a-input v-model="model.preqemp" placeholder="请输入申请人"></a-input> -->
                  <c-select-user v-model="model.preqemp" :buttons="false" :multi="false"></c-select-user>
                </a-form-model-item>
              </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
                  <a-input v-model="model.remarks" placeholder="请输入备注"></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :xs="24" :sm="6">
                <a-form-model-item label="申请总数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sumqty">
                  <a-input-number v-model="model.sumqty" placeholder="请输入申请总数量" style="width: 100%" disabled />
                </a-form-model-item>
              </a-col>
              <!-- <a-col :xs="24" :sm="6">
                <a-form-model-item label="申请总金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sumvalue">
                  <a-input-number v-model="model.sumvalue" placeholder="请输入申请总金额" style="width: 100%" disabled />
                </a-form-model-item>
              </a-col> -->
            </a-row>
            <a-row>
              <a-col :span="6" v-for="(item, index) in maincusList" :key="index">
                <a-form-model-item :label="item.customname" :labelCol="labelCol" :wrapperCol="wrapperCol"
                  prop="item.customcode">
                  <a-input v-model="maincusItems[item.customcode]" placeholder="请输入"></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>
          </a-form-model>
        </j-form-container>
        <!-- 子表单区域 -->
        <a-tabs v-model="activeKey" @change="handleChangeTabs">
          <a-tab-pane tab="采购申请表体" :key="refKeys[0]" :forceRender="true">
            <!-- <j-editable-table :ref="refKeys[0]" :loading="scmpreqitemTable.loading" :columns="scmpreqitemTable.columns"
              :dataSource="scmpreqitemTable.dataSource" :maxHeight="300" :disabled="formDisabled" :rowNumber="true"
              :rowSelection="true" :actionButton="true" 
              @valueChange="handleValueChange" /> -->

              <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmpreqitemTable.loading"
          :columns="scmpreqitemTable.columns" 
          :dataSource="scmpreqitemTable.dataSource" 
          :maxHeight="300"
          
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           @valueChange="handleValueChange"/>  
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-spin>
  </a-card>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
//import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue, getNowFormatDate } from '@/utils/util';
import { words } from 'lodash'
import { getAction } from '../../../../api/manage'
import { runInThisContext } from 'vm'
// import ActHandleBtn from "@views/flowable/components/ActHandleBtn";
import { JVXETypes } from '@/components/jeecg/JVxeTable'

export default {
  name: 'ScmpreqForm',
  mixins: [JVxeTableModelMixin],
  components: {
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
        preqcode: ""
      },
      validatorRules: {
          companyid: [
            { required: true, message: '请选择公司!' },
          ],
      },
      auditvisable: false,
      audittaskid: '',
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scmpreqitem',],
      tableKeys: ['scmpreqitem',],
      activeKey: 'scmpreqitem',
      itemTable: {},
      maincusList: [],
      maincusItems: {},
      itemcusObject: 'scmpreqitem',
      // 采购申请表体
      scmpreqitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: JVXETypes.materialSelect,
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
            type: JVXETypes.input,
            disabled: true,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
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
              title: '计量单位',
              key: 'uomid',
              type: JVXETypes.sel_search,
              width:"100px",
              dictCode:'scmuom,uomname,id',
              placeholder: '请输入${title}',
              defaultValue:'',
          },
          {
            title: '申请数量',
            key: 'qty',
            type: JVXETypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '1',
            statistics: ['sum']
          },
          {
            title: '计划单价',
            key: 'taxinprice',
            type: JVXETypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '0',
          },
          {
            title: '总金额',
            key: 'taxinvalue',
            type: JVXETypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            customRender: function (t, r, index) {
              console.log(r);
            },
            statistics: ['sum']
          },
          {
            title: '需求日期',
            key: 'preqdate',
            type: JVXETypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',

          },
        ]
      },
      url: {
        add: "/preq/scmpreq/add",
        edit: "/preq/scmpreq/edit",
        querybyId: "/preq/scmpreq/queryById",
        scmpreqitem: {
          list: '/preq/scmpreq/queryScmpreqitemByMainId'
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
    }
  },
  created () {
    //获取生成编号
    console.log("router.params:" + JSON.stringify(this.$route.params))
    let routeparams = this.$route.params;
    if (routeparams != undefined && (routeparams.id != '' && routeparams.id != undefined)) {
      let params = { id: routeparams.id }
      //获取主表信息
      this.getMainData(this.url.querybyId, params);
      //获取分录信息
      this.requestSubTableData(this.url.scmpreqitem.list, params, this.scmpreqitemTable)
      this.disabled = true;
      this.auditvisable = true;
      this.audittaskid = routeparams.taskId;
    }

    this.itemTable = this.scmpreqitemTable;

    if(this.model.id){

    }else{
      this.model.billdate=getNowFormatDate();
    }

  },
  methods: {
    //   const { type, row, column, value, target } = event
    //   if (type === FormTypes.inputNumber) {
    //     if (column.key === 'qty') {
    //       let currentPrice = row['price'].toString()
    //       target.setValues([{
    //         rowKey: row.id,
    //         values: { 'taxinvalue': (value * currentPrice).toFixed(2) }
    //       }])
    //     } else if (column.key === 'price') {
    //       let currentQty = row['qty'].toString()
    //       target.setValues([{
    //         rowKey: row.id,
    //         values: { 'taxinvalue': (value * currentQty).toFixed(2) }
    //       }])
    //     }
    //   }
    // },
    addBefore () {
      this.scmpreqitemTable.dataSource = []
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
        this.requestSubTableData(this.url.scmpreqitem.list, params, this.scmpreqitemTable)
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
        scmpreqitemList: allValues.tablesValue[0].tableData,
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

<style lang='postcss'>
.left {
  width: 80%;
  height: 100%;
}

.right {
  width: 20%;
  height: 100%;
  float: right;
}
</style>
