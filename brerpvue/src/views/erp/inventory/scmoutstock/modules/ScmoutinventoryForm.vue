<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司"
                dict="sys_depart where org_type='1',depart_name,id"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inventorytype">
              <j-search-select-tag v-model="model.inventorytype" placeholder="请输入单据类型"
                dict="scminventorytype where outstock='true',typename,typecode"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="红单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isred">
              <a-checkbox v-model="model.isred" placeholder="请检查是否红单" :disabled="isreddisable"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billcode">
              <a-input v-model="model.billcode" placeholder="请输入单据编号" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="业务日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billdate">
              <j-date v-model="model.billdate" placeholder="请输入业务日期" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="库存部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="invdepartid">
              <j-select-depart v-model="model.invdepartid" placeholder="请输入库存部门" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="保管员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="invemployid">
              <c-select-user v-model="model.invemployid" placeholder="请输入保管员" :buttons="false" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseid">
              <j-search-select-tag ref="wfref" v-model="model.warehouseid" placeholder="请输入仓库" :buttons="false"
                dict="warehouse,name,id" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-customer v-model="model.customerid" placeholder="客户" :buttons="false" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="库存单据分录" :key="refKeys[0]" :forceRender="true">
        <!-- <j-editable-table :ref="refKeys[0]" :loading="scminventoyitemTable.loading"
          :columns="scminventoyitemTable.columns" :dataSource="scminventoyitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" :actionButton="true" :added="added"
          @valueChange="handleValueChange" @selectRowChange="handleSelectRowChange">

          <template slot="buttonAfter">
            <a-button type="primary" @click="getReferSoData" v-if="!model.isred">参照销售订单</a-button>
            <a-button type="primary" @click="getReturnPoDatalsit" v-if="model.isred">参照退货订单</a-button>
          </template>

        </j-editable-table> -->

        <j-vxe-table keep-source :ref="refKeys[0]" :loading="scminventoyitemTable.loading"
          :columns="scminventoyitemTable.columns" :dataSource="scminventoyitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" :toolbar="true" :bordered="true"
          :alwaysEdit="false" @valueChange="handleValueChange">
          <template slot="toolbarSuffix">
            <a-button type="primary" @click="getReferSoData" v-if="!model.isred">参照销售订单</a-button>
            <a-button type="primary" @click="getReturnPoDatalsit" v-if="model.isred">参照退货订单</a-button>
          </template>
        </j-vxe-table>

      </a-tab-pane>
    </a-tabs>
    <out-stock-refer-so ref="outstockreferso" @ok="getselectdata"></out-stock-refer-so>
    <outstock-refer-returnso ref="outstockreferreturnso" @ok="getselectReturnsoData"></outstock-refer-returnso>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
// import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue, getNowFormatDate } from '@/utils/util'
import JSearchSelectTag from '../../../../../components/dict/JSearchSelectTag.vue'
import JSelectDepart from '../../../../../components/jeecgbiz/JSelectDepart.vue'
import { max } from 'lodash'
import CSelectCustomer from '../../../base/custom/CSelectCustomer.vue'
import OutStockReferSo from '../../../base/custom/COutstockReferSo.vue'
import OutstockReferReturnso from '../../../base/custom/COutstockReferReturnso.vue'
import { getAction } from '../../../../../api/manage'
import { JVXETypes } from '@/components/jeecg/JVxeTable/index';
import { setStore, getStore, clearStore } from "@/utils/storage"
import { getscmbatchlist } from '@/api/erp/commonapi'

export default {
  name: 'ScmoutinventoryForm',
  mixins: [JVxeTableModelMixin],
  components: {
    JSearchSelectTag,
    JSelectDepart,
    CSelectCustomer,
    OutStockReferSo,
    OutstockReferReturnso
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
        inventorytype: [{ required: true, message: '请选择单据类型' }],
        warehouseid: [{ required: true, message: '请选择仓库' }],
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scminventoyitem',],
      tableKeys: ['scminventoyitem',],
      activeKey: 'scminventoyitem',
      isreddisable: false,
      // 库存单据分录
      scminventoyitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '物料',
            key: 'materialid',
            type: FormTypes.sel_material,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
            props: {
              materialtype: '2',
              isout: true,
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
            props: {
              isstock: true,
            }
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
            title: '仓库',
            key: 'warehouseid',
            type: FormTypes.hidden,
            dictTable: 'warehouse',
            dictText: 'name',
            dictCode: 'id',
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '批号',
            key: 'batchid',
            type: JVXETypes.batchSelect,
            dictCode: '',
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '出库数量',
            key: 'qty',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
            statistics: ['sum']
          },
          {
            title: '库存状态',
            key: 'stocktypeid',
            width: "120px",
            type: FormTypes.sel_search,
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
            type: FormTypes.sel_search,
            dictCode: 'scminventorykinds,kindname,kindcode',
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
          },
          {
            title: '销售含税单价',
            key: 'taxinprice',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '销售不含税单价',
            key: 'taxexprice',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '税率',
            key: 'taxrate',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '销售总价',
            key: 'taxinvalue',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum'],
          },
          {
            title: '销售不含税总价',
            key: 'taxexvalue',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            statistics: ['sum']
          },
          {
            title: 'fromid',
            key: 'fromid',
            type: FormTypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: 'fromitemid',
            key: 'fromitemid',
            type: FormTypes.hidden,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          }
        ]
      },
      url: {
        add: "/inventory/scminventoy/add",
        edit: "/inventory/scminventoy/edit",
        scminventoyitem: {
          list: '/inventory/scminventoy/queryScminventoyitemByMainId'
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
  },
  watch: {
    "model.companyid": {
      deep: true,
      handler (val, old) {
        let currentuser = this.$store.getters.userInfo
        localStorage.setItem(currentuser.id+"_outstock_companyid", val);
        if(!this.model.id){
        this.$refs.wfref.newdict="warehouse where companyid = '"+val+"',name,id";
        if(val != old){
          this.model.warehouseid=''
        }
      }
      }
    },
    "model.warehouseid": {
      deep: true,
      handler (val, old) {
        let currentuser = this.$store.getters.userInfo
        localStorage.setItem(currentuser.id+"_outstock_warehouseid", val);
      }
    }
  },
  created () {
    if (this.model.id) {

    } else {
      this.model.billdate = getNowFormatDate();
    }
  },
  methods: {
    // editmethod(event){
    //  const {row, rowIndex, column, columnIndex} = event;
    //  console.log("this column:"+JSON.stringify(column));
    //  return false;
    // },
    // handlecellclick(event){
    //   const { row, rowIndex, $rowIndex, column, columnIndex, $columnIndex, triggerRadio, triggerCheckbox, $event }=event;
    //   this.$message.error("请检查是否进行配置");
    //   return;
    // },
    /**获取选中的销售订单数据 */
    getselectdata (selectedRowKeys) {
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      if (ids === '') {
        return;
      }
      let sourl = '/so/scmso/queryitembyitemid';
      let params = { ids: ids }
      getAction(sourl, params).then(async res => {
        if (res.success) {
          let sores = res.result;
          this.scminventoyitemTable.dataSource = []
          for (let i = 0; i < sores.length; i++) {
            //获取最新批次信息
            let curQty = (sores[i].qty - sores[i].outqty).toFixed(2);
            let params = {
              companyid: this.model.companyid,
              warehouseid: this.model.warehouseid,
              materialid: sores[i].materialid,
            }
            let batchinfo = await getscmbatchlist(params)
            if (batchinfo.success) {
              if (batchinfo.result) {
                let curbatchinfo = batchinfo.result
                for (let j = 0; j < curbatchinfo.length; j++) {
                  if (parseFloat(curbatchinfo[j].batchqty) > parseFloat(curQty)) {
                    //todo 增加行

                    let newColumnData = {
                      materialid: sores[i].materialid,
                      materialcode: sores[i].materialcode,
                      uomid: sores[i].uomid,
                      batchid: curbatchinfo[j].id,
                      qty: curQty,
                      taxinprice: sores[i].taxinprice,
                      taxexprice: sores[i].taxexprice,
                      taxinvalue: (sores[i].taxinprice * (curQty)).toFixed(2),
                      taxexvalue: (sores[i].taxexprice * (curQty)).toFixed(2),
                      taxrate: sores[i].taxrate,
                      fromitemid: sores[i].id,
                      fromid: sores[i].parentid,
                    }
                    curQty = 0;
                    this.scminventoyitemTable.dataSource.push(newColumnData);
                    break;
                  } else {
                    //分批
                    curQty = parseFloat(curQty) - parseFloat(curbatchinfo[j].batchqty)
                    let newColumnData = {
                      materialid: sores[i].materialid,
                      materialcode: sores[i].materialcode,
                      uomid: sores[i].uomid,
                      batchid: curbatchinfo[j].id,
                      qty: curbatchinfo[j].batchqty,
                      taxinprice: sores[i].taxinprice,
                      taxexprice: sores[i].taxexprice,
                      taxinvalue: (sores[i].taxinprice * (curbatchinfo[j].batchqty)).toFixed(2),
                      taxexvalue: (sores[i].taxexprice * (curbatchinfo[j].batchqty)).toFixed(2),
                      taxrate: sores[i].taxrate,
                      fromitemid: sores[i].id,
                      fromid: sores[i].parentid,
                    }
                    this.scminventoyitemTable.dataSource.push(newColumnData);
                  }
                }
                if (curQty > 0) {
                  let newColumnData = {
                    materialid: sores[i].materialid,
                    materialcode: sores[i].materialcode,
                    uomid: sores[i].uomid,
                    batchid: '',
                    qty: curQty,
                    taxinprice: sores[i].taxinprice,
                    taxexprice: sores[i].taxexprice,
                    taxinvalue: (sores[i].taxinprice * (curQty)).toFixed(2),
                    taxexvalue: (sores[i].taxexprice * (curQty)).toFixed(2),
                    taxrate: sores[i].taxrate,
                    fromitemid: sores[i].id,
                    fromid: sores[i].parentid,
                  }
                  this.scminventoyitemTable.dataSource.push(newColumnData);
                }
              } else {
                let newColumnData = {
                  materialid: sores[i].materialid,
                  materialcode: sores[i].materialcode,
                  uomid: sores[i].uomid,
                  batchid: "",
                  qty: curQty,
                  taxinprice: sores[i].taxinprice,
                  taxexprice: sores[i].taxexprice,
                  taxinvalue: (sores[i].taxinprice * (curQty)).toFixed(2),
                  taxexvalue: (sores[i].taxexprice * (curQty)).toFixed(2),
                  taxrate: sores[i].taxrate,
                  fromitemid: sores[i].id,
                  fromid: sores[i].parentid,
                }
                this.scminventoyitemTable.dataSource.push(newColumnData);
              }
            }
          }
        }
      })
    },


    /**出库红单参照退货销售订单 */
    getselectReturnsoData (selectedRowKeys) {
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      if (ids === '') {
        return;
      }
      let sourl = '/so/scmso/queryitembyitemid';
      let params = { ids: ids }
      getAction(sourl, params).then(res => {
        if (res.success) {
          let sores = res.result;
          this.scminventoyitemTable.dataSource = []
          for (let i = 0; i < sores.length; i++) {
            const newColumnData = {
              materialid: sores[i].materialid,
              materialcode: sores[i].materialcode,
              uomid: sores[i].uomid,
              qty: (sores[i].qty - sores[i].outqty).toFixed(2),
              taxinprice: sores[i].taxinprice,
              taxexprice: sores[i].taxexprice,
              taxinvalue: (sores[i].taxinprice * (sores[i].qty - sores[i].outqty)).toFixed(2),
              taxexvalue: (sores[i].taxexprice * (sores[i].qty - sores[i].outqty)).toFixed(2),
              taxrate: sores[i].taxrate,
              fromitemid: sores[i].id,
              fromid: sores[i].parentid,
            }
            this.scminventoyitemTable.dataSource.push(newColumnData);
          }
        }
      })
    },
    getReturnPoDatalsit () {
      this.$nextTick(() => {
        this.$refs.outstockreferreturnso.show('其它', '退货订单', "", "1,3", this.model.customerid)
      })
    },
    /**出库蓝单参照销售订单 */
    getReferSoData () {
      this.$nextTick(() => {
        this.$refs.outstockreferso.show('其它', '销售订单', "", "1,3", this.model.customerid)
      })
    },
    /**行选中或者没选中的时候 */
    handleSelectRowChange (selectedRowIds) {
      if (selectedRowIds.length === 0) {
        return;
      }
      this.selectedRowIds = selectedRowIds;
      for (let i = 0; i < this.dataSource.length; i++) {
        if (this.dataSource[i].id === this.selectedRowIds) {
          console.log(this.selectedRowIds);
        }
      }
    },

    /**值发生改变的时候 */
    valueChange (event) {
      const { type, row, column, value, target } = event;
      if (type === 'sel_material') {
        //物料帮助
        if (value === '' || value === undefined) {
          row['batchid'] = ''
          return;
        }
        //
        for (let i = 0; i < this.scminventoyitemTable.columns.length; i++) {
          if (this.scminventoyitemTable.columns[i].key === 'batchid') {
            //this.scminventoyitemTable.columns[i].dictCode = "scmbatch where materialid ='"+value+"',batchcode,id";
            //this.scminventoyitemTable.columns[i].options=''
            let currentobj = {
              title: '批号',
              key: 'batchid',
              type: FormTypes.sel_search,
              dictCode: "scmbatch where materialid ='" + value + "',batchcode,id",
              width: "100px",
              placeholder: '请输入${title}',
              defaultValue: '',
            }
            this.$set(this.scminventoyitemTable.columns, i, currentobj);
          }
        }
      }
    },

    /**
     * 新增行之后的操作
     * @param {新增后的行} row 
     * @param {行id} id 
     */
    added (row) {

    },
    /**
     * 入库单参照采购收货单
     */
    getMatReceiptReqlist () {
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.instockreferreq.show('其它', '收获申请单', "", "1,3", this.model.vendorid)
      })
    },
    addBefore () {
      this.scminventoyitemTable.dataSource = []
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
        this.isreddisable = true
        let params = { id: this.model.id }
        this.requestSubTableData(this.url.scminventoyitem.list, params, this.scminventoyitemTable)
      }else{
        this.$refs.wfref.newdict="warehouse where companyid = '"+this.model.companyid+"',name,id";
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
        scminventoyitemList: allValues.tablesValue[0].tableData,
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