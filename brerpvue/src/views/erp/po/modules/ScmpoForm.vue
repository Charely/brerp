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
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="采购部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="podeptid">
              <j-select-depart v-model="model.podeptid" 
                placeholder="请输入采购部门" 
                :pid="model.companyid"></j-select-depart>
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
      <a-tab-pane tab="采购订单分录" :key="refKeys[0]" :forceRender="true">


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
           @valueChange="handleValueChange">
           <template slot="toolbarSuffix" :disabled="formDisabled">
            <a-button type="primary" @click="getPrData" :disabled="formDisabled">参照采购计划</a-button>
          </template>
          </j-vxe-table>


      </a-tab-pane>
    </a-tabs>

    <po-refer-pr ref="PoReferPr" @ok="getSelectPr"></po-refer-pr>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
//import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue, getNowFormatDate } from '@/utils/util'
import { getAction } from '../../../../api/manage'
import PoReferPr from '../../base/custom/PoReferPr.vue'
import { toFixedNumber } from 'xe-utils/methods'

import { queryMaterialById,queryMaterialVoInfoById } from '@/api/erp/commonapi'

export default {
  name: 'ScmpoForm',
  mixins: [JVxeTableModelMixin],
  components: {
    PoReferPr
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
            width: "100px",
            dictCode: 'scmuom,uomname,id',
            placeholder: '请输入${title}',
            defaultValue: '',
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
            width: "100px",
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
          {
            title: '需求时间',
            key: 'preqdate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '预计到货时间',
            key: 'deliverydate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
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
    if (this.model.id) {

    } else {
      this.model.podate = getNowFormatDate()
    }
  },
  methods: {
    getSelectPr (selectedRowKeys) {
      let ids = "";
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      let url = "/pr/scmpr/queryScmpritemListByIds";
      let param = { ids: ids };
      getAction(url, param).then(async (res) => {
        if (res) {
          let resu = res.result.records || res.result;
          //console.log("resu" + resu);
          //todo 给新增行赋值
          let info = [];
          if (resu != null && resu.length > 0) {
            for (let i = 0; i < resu.length; i++) {
              let currentValue = parseFloat(resu[i]["qty"], 2) * parseFloat(resu[i]["taxinprice"], 2);
              currentValue = toFixedNumber(currentValue, 2);
              let materials = await queryMaterialVoInfoById({ materialid: resu[i]["materialid"] });
              let taxrate = 0;
              if (materials.result ==null || materials.result.taxrate === '' ||
                materials.result.taxrate === null ||
                materials.result.taxrate === undefined) {
                taxrate = 0;
              } else {
                taxrate = materials.result.taxrate;
              }
              const newData = {
                materialid: resu[i]["materialid"],
                materialcode: resu[i]["materialcode"],
                materialname:resu[i]["materialname"],
                qty: resu[i]["qty"],
                taxinprice: resu[i]["taxinprice"],
                uomid: resu[i]["uomid"],
                taxrate: taxrate,
                taxexprice: toFixedNumber(parseFloat(resu[i]["taxinprice"], 2) * (100 - taxrate) / 100, 2),
                taxexvalue: toFixedNumber(parseFloat(resu[i]["taxinprice"], 2) * (100 - taxrate) / 100 * resu[i]['qty'], 2),
                fromid: resu[i]["parentid"],
                fromitemid: resu[i]["id"],
                taxinvalue: currentValue,
                preqdate:resu[i].prdate,
                // deliverydate: resu[i]["prorder_date"]
              }
              info.push(newData);
            }
            this.scmpoitemTable.dataSource = info;
          }
        }
      })
    },
    getPrData () {
      if (!this.model.vendorid) {
        this.$message.warning("请先选择供应商");
        return;
      }
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.PoReferPr.show('其它', '采购计划', "", "1,3", this.model.vendorid,this.model.companyid)
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