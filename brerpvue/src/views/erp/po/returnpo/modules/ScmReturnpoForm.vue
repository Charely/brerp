<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="采购日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="podate">
              <j-date placeholder="请选择采购日期" v-model="model.podate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="红单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="podate">
              <a-checkbox placeholder="请选择采购日期" v-model="model.isred" style="width: 100%" disabled></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="采购部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="podeptid">
              <j-select-depart v-model="model.podeptid" placeholder="请输入采购部门" :pid="model.companyid"></j-select-depart>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="采购人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="poemptid">
              <c-select-user :buttons="false" :multi="false" v-model="model.poemptid" placeholder="请输入采购人员">
              </c-select-user>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-vendor v-model="model.vendorid" :buttons="false" placehodler="请选择供应商" required>
              </c-select-vendor>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :sm="6" v-for="(item, index) in maincusList" :key="index">
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
      <a-tab-pane tab="退货订单分录" :key="refKeys[0]" :forceRender="true">
        <!-- <j-editable-table :ref="refKeys[0]" :loading="scmpoitemTable.loading" :columns="scmpoitemTable.columns"
          :dataSource="scmpoitemTable.dataSource" :maxHeight="300" :disabled="formDisabled" :rowNumber="true"
          :rowSelection="true" :actionButton="true" @valueChange="handleValueChange">
          <template slot="buttonAfter">
            <a-button type="primary" @click="getBluepoData">参照采购订单</a-button>
          </template>
        </j-editable-table> -->

        <j-vxe-table keep-source :ref="refKeys[0]" 
        :loading="scmpoitemTable.loading"
          :columns="scmpoitemTable.columns" 
          :dataSource="scmpoitemTable.dataSource" 
          :maxHeight="300"
          
          :disabled="formDisabled" 
          :rowNumber="true"
           :rowSelection="true" 
           :toolbar="true" 
           :bordered="true"
           :alwaysEdit="true"
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix">
            <a-button type="primary" @click="getBluepoData">参照采购订单</a-button>
          </template>
          </j-vxe-table>
      </a-tab-pane>
    </a-tabs>
    <returnpo-referpo ref="ReturnpoReferpo" @ok="ReturnpoReferpo"></returnpo-referpo>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'

import { validateDuplicateValue, getNowFormatDate } from '@/utils/util'
import { getAction } from '../../../../../api/manage'
import { toFixedNumber } from 'xe-utils/methods'

import ReturnpoReferpo from '../../../base/custom/CReturnpoReferpo.vue'

export default {
  name: 'ScmReturnpoForm',
  mixins: [JVxeTableModelMixin],
  components: {
    ReturnpoReferpo
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
        companyid: [
            { required: true, message: '请选择公司!' },
          ],
          vendorid:[
          { required: true, message: '' },
          ]
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scmpoitem',],
      tableKeys: ['scmpoitem',],
      activeKey: 'scmpoitem',
      maincusObject: 'scmpo',
      itemcusObject: 'scmpoitem',
      maincusList: [],
      maincusItems: {},
      itemTable: {},
      // 采购订单分录
      scmpoitemTable: {
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
            title: '计量单位',
            key: 'uomid',
            type: FormTypes.sel_search,
            dictCode: 'scmuom,uomname,id',
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          {
            title: '采购数量',
            key: 'qty',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '含税单价',
            key: 'taxinprice',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '税率',
            key: 'taxrate',
            type: FormTypes.inputNumber,
            width: "150px",
            placeholder: "请输入${title}",
            defaultValue: 0,
          },
          {
            title: '不含税单价',
            key: 'taxexprice',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '不含税总价',
            key: 'taxexvalue',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: '含税总价',
            key: 'taxinvalue',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          // {
          //   title: '交货日期',
          //   key: 'deliverydate',
          //   type: FormTypes.date,
          //   width:"100px",
          //   placeholder: '请输入${title}',
          //   defaultValue:'',
          // },
          {
            title: 'fromid',
            key: 'fromid',
            type: FormTypes.hidden
          },
          {
            title: 'fromitemid',
            key: 'fromitemid',
            type: FormTypes.hidden
          },
        ]
      },
      url: {
        add: "/po/scmpo/add",
        edit: "/po/scmpo/edit",
        scmpoitem: {
          list: '/po/scmpo/queryScmpoitemByMainId'
        },
      }
    }
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: true,
      required: false
    }
  },
  computed: {
    formDisabled () {
      return this.disabled
    },
  },
  created () {
    this.itemTable = this.scmpoitemTable
    // this.model.isred='1'
    if (this.model.id) {

    } else {
      this.model.podate = getNowFormatDate()
      this.model.isred = '1'
    }
  },
  methods: {
    ReturnpoReferpo (selectedRowKeys) {
      let ids = "";
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      let url = "/po/scmpo/queryScmpoitemlistbyids";
      let param = { ids: ids };
      getAction(url, param).then((res) => {
        if (res) {
          let resu = res.result.records || res.result;
          //console.log("resu" + resu);
          //todo 给新增行赋值
          let info = [];
          if (resu != null && resu.length > 0) {
            for (let i = 0; i < resu.length; i++) {
              const newData = {
                itemcode: '0001',
                materialid: resu[i]["materialid"],
                materialcode: resu[i]['materialcode'],
                qty: (resu[i]["qty"] - resu[i]["returnqty"]).toFixed(2),
                taxrate: resu[i]['taxrate'],
                taxinprice: resu[i]['taxinprice'],
                taxexprice: resu[i]['taxexprice'],
                taxinvalue: (resu[i]['taxinprice'] * (resu[i]['qty'] - resu[i]['returnqty'])).toFixed(2),
                taxexvalue: (resu[i]['taxexprice'] * (resu[i]['qty'] - resu[i]['returnqty'])).toFixed(2),
                fromid: resu[i]["parentid"],
                fromitemid: resu[i]["id"],
              }
              info.push(newData);
            }
            this.scmpoitemTable.dataSource = info;
          }
        }
      })
    },
    getBluepoData () {
      if (!this.model.vendorid) {
        this.$message.warning("请先选择供应商");
        return;
      }
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.ReturnpoReferpo.show('其它', '采购订单', "", "1,3", this.model.vendorid)
      })
    },
    addBefore () {
      this.scmpoitemTable.dataSource = []
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
        this.requestSubTableData(this.url.scmpoitem.list, params, this.scmpoitemTable)
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
        scmpoitemList: allValues.tablesValue[0].tableData,
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