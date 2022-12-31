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
                dict="scminventorytype where instock='true',typename,typecode"></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseid">
              <j-search-select-tag ref="wfref" v-model="model.warehouseid" placeholder="请输入仓库" :buttons="false"
                dict= "warehouse,name,id" :async="true" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="红单" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isred">
              <a-checkbox v-model="model.isred" placeholder="请检查是否红单" :disabled="isreddisabled"></a-checkbox>
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
              <j-select-depart v-model="model.invdepartid" placeholder="请输入库存部门" :pid="model.companyid" />
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="保管员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="invemployid">
              <c-select-user v-model="model.invemployid" placeholder="请输入保管员" :buttons="false" />
            </a-form-model-item>
          </a-col>
          
          <a-col :xs="24" :sm="6">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <c-select-vendor v-model="model.vendorid" placeholder="请输入供应商" :buttons="false" />
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
        <j-vxe-table keep-source :ref="refKeys[0]" :loading="scminventoyitemTable.loading"
          :columns="scminventoyitemTable.columns" :dataSource="scminventoyitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" :toolbar="true" :bordered="true"
          :alwaysEdit="true" @valueChange="handleValueChange">
          <template slot="toolbarSuffix">
            <a-button type="primary" @click="getMatReceiptReqlist" v-if="!model.isred" :disabled="formDisabled">参照收货申请
            </a-button>
            <a-button type="primary" @click="getReturnPoDatalsit" v-if="model.isred" :disabled="formDisabled">参照退货订单
            </a-button>
          </template>
        </j-vxe-table>
      </a-tab-pane>
    </a-tabs>
    <instock-refer-receiptreq ref="instockreferreq" @ok="getSelectReq"></instock-refer-receiptreq>
    <refer-returnpo ref="referReturnpo" @ok="getSelectReturnPoData"></refer-returnpo>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
//import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
import { validateDuplicateValue, getNowFormatDate } from '@/utils/util'
import JSearchSelectTag from '../../../../../components/dict/JSearchSelectTag.vue'
import JSelectDepart from '../../../../../components/jeecgbiz/JSelectDepart.vue'
import InstockReferReceiptreq from '../../../base/custom/CInstockReferReceiptReq.vue'
import ReferReturnpo from '@/views/erp/base/custom/CReferReturnpo.vue'
import { getAction } from '../../../../../api/manage'
import { getStocktype, getInventoryKind } from '@/api/erp/commonapi';
import { JVXETypes } from '@/components/jeecg/JVxeTable/index';

export default {
  name: 'ScminventoyForm',
  mixins: [JVxeTableModelMixin],
  components: {
    JSearchSelectTag,
    JSelectDepart,
    InstockReferReceiptreq,
    ReferReturnpo
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
        companyid:[{required:true,message:'请选择公司'}],
        inventorytype: [{ required: true, message: '请选择单据类型' }],
        warehouseid: [{ required: true, message: '请选择仓库' }]
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      warehousedict:'',
      refKeys: ['scminventoyitem',],
      tableKeys: ['scminventoyitem',],
      activeKey: 'scminventoyitem',
      isreddisabled: false,
      islocationdisabled:false,
      // 库存单据分录
      scminventoyitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          // {
          //   title: '行号',
          //   key: 'itemcode',
          //   type: FormTypes.input,
          //   width: "100px",
          //   placeholder: '请输入${title}',
          //   defaultValue: '',
          //   disabled: true,
          // },
          {
              title:"",
              key:"expandcolumn",
              type:JVXETypes.rowExpand,
              width:'10px',
            },
          {
            title: '物料',
            key: 'materialid',
            type: FormTypes.sel_material,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [
              {
                required: true, // 必填
                message: '${title}不能为空' // 提示的文本
              },
            ],
            props: {
              materialtype: '2',
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
            width: "120px",
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
            title: '入库数量',
            key: 'qty',
            type: FormTypes.inputNumber,
            width: "120px",
            placeholder: '请输入${title}',
            defaultValue: '',
            validateRules: [{ required: true, message: '${title}不能为空' }],
            statistics: ['sum'],
          },
          {
            title: '批号',
            key: 'batchid',
            type: FormTypes.hidden,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '批号',
            key: 'batchcode',
            type: FormTypes.input,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '货位',
            key: 'stocklocationid',
            width: "200px",
            type: JVXETypes.popup,
            popupCode:'stocklocationhelp',
            field:'stocklocationname',
            orgFields:'id,locationname',
            destFields:'stocklocationid,stocklocationname',
            placeholder: '请输入${title}',
            defaultValue: '',
          },  
          {
            title: '供应商批号',
            key: 'vendorbatchcode',
            type: FormTypes.input,
            width: "150px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '批次生效日期',
            key: 'batchstartdate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '批次失效日期',
            key: 'batchenddate',
            type: FormTypes.date,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
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
            title: '库存类型',
            key: 'fromid',
            type: FormTypes.hidden,
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '库存类型',
            key: 'fromitemid',
            type: FormTypes.hidden,
            options: [],
            placeholder: '请输入${title}',
            defaultValue: '',
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
      }
    },
    'model.warehouseid':{
      handler(val){
        if(val){
          for(let i=0;i<this.scminventoyitemTable.columns.length;i++){
            if(this.scminventoyitemTable.columns[i].key === 'stocklocationid'){
              let curcolumn=this.scminventoyitemTable.columns[i];
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
  created () {
    if (this.model.id) {
      console.log("this.model.id:" + this.model.id)
      this.isreddisabled = true
    } else {
      this.model.billdate = getNowFormatDate();
     // this.$refs.wfref.newdict="warehouse where companyid = '"+this.model.companyid+"',name,id";
    }
  },
  methods: {
    getSelectReq (selectedRowKeys) {
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      if (ids === '') {
        return;
      }
      let sourl = '/po/scmmatreceiptreq/queryiteminfobyitemid';
      let params = { ids: ids }

      let __this = this;
      getAction(sourl, params).then(async res => {
        if (res.success) {
          let receptitems = res.result;
          this.scminventoyitemTable.dataSource = []

          let stocklist = await getStocktype();
          let currentStockid = ''
          let currentInventorykindid = ''
          if (stocklist.success) {
            let records = stocklist.result.records;
            currentStockid = records[0].id;
          }
          let kindidlist = await getInventoryKind();
          if (kindidlist.success) {
            let kindrecords = kindidlist.result.records;
            currentInventorykindid = kindrecords[0].id
          }

          for (let i = 0; i < receptitems.length; i++) {
            const newColumnData = {
              materialid: receptitems[i].materialid,
              materialcode: receptitems[i].materialcode,
              uomid: receptitems[i].uomid,
              qty: (receptitems[i].qty - receptitems[i].receiptqty).toFixed(2),
              batchcode: receptitems[i].batchcode,
              batchid: receptitems[i].batchid,
              batchstartdate: receptitems[i].batchstartdate,
              batchenddate: receptitems[i].batchenddate,
              vendorbatchcode: receptitems[i].vendorbatchcode,
              stocktypeid: currentStockid,
              inventorykindid: currentInventorykindid,
              taxinprice: receptitems[i].taxinprice,
              taxexprice: receptitems[i].taxexprice,
              taxrate: receptitems[i].taxrate,
              taxinvalue: (receptitems[i].taxinprice * (receptitems[i].qty - receptitems[i].receiptqty)).toFixed(2),
              taxexvalue: (receptitems[i].taxexprice * (receptitems[i].qty - receptitems[i].receiptqty)).toFixed(2),
              fromitemid: receptitems[i].id,
              fromid: receptitems[i].parentid,
            }
            this.scminventoyitemTable.dataSource.push(newColumnData);
          }
        }
      })
    },

    /*入库红单参照退货订单 */
    getSelectReturnPoData (selectedRowKeys) {
      let ids = '';
      for (let i = 0; i < selectedRowKeys.length; i++) {
        if (selectedRowKeys[i] != undefined) {
          ids += selectedRowKeys[i] + ",";
        }
      }
      if (ids === '') {
        return;
      }
      let sourl = '/po/scmpo/queryScmpoitemlistbyids';
      let params = { ids: ids }
      getAction(sourl, params).then(async res => {
        if (res.success) {
          let receptitems = res.result;
          this.scminventoyitemTable.dataSource = []
          let stocklist = await getStocktype();
          let currentStockid = ''
          let currentInventorykindid = ''
          if (stocklist.success) {
            let records = stocklist.result.records;
            currentStockid = records[0].id;
          }
          let kindidlist = await getInventoryKind();
          if (kindidlist.success) {
            let kindrecords = kindidlist.result.records;
            currentInventorykindid = kindrecords[0].id
          }
          for (let i = 0; i < receptitems.length; i++) {
            const newColumnData = {
              materialid: receptitems[i].materialid,
              materialcode: receptitems[i].materialcode,
              uomid: receptitems[i].uomid,
              qty: (receptitems[i].qty - receptitems[i].receiptqty).toFixed(2),
              stocktypeid: currentStockid,
              inventorykindid: currentInventorykindid,
              fromitemid: receptitems[i].id,
              fromid: receptitems[i].parentid,
            }
            this.scminventoyitemTable.dataSource.push(newColumnData);
          }
        }
      })
    },

    /**
     * 新增行之后的操作
     * @param {新增后的行} row 
     * @param {行id} id 
     */
    async added (event) {
      const { row, target } = event
      let stocklist = await getStocktype();
      let currentStockid = ''
      let currentInventorykindid = ''
      if (stocklist.success) {
        let records = stocklist.result.records;
        currentStockid = records[0].id;
      }
      let kindidlist = await getInventoryKind();
      if (kindidlist.success) {
        let kindrecords = kindidlist.result.records;
        currentInventorykindid = kindrecords[0].id
      }
      target.setValues([{
        rowKey: row.id,
        values: {
          'stocktypeid': currentStockid,
          'inventorykindid': currentInventorykindid,
        }
      }])
    },
    /**
     * 入库单参照采购收货单
     */
    getMatReceiptReqlist () {
      localStorage.setItem("receipt_companyid",this.model.companyid);
      localStorage.setItem("receipt_warehouseid",this.model.warehouseid);
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.instockreferreq.show('其它', '收获申请单', "", "1,3", this.model.vendorid)
      })
    },
    /*
    * 入库红单参照退货订单
    */
    getReturnPoDatalsit () {
      this.$nextTick(() => {
        //console.log(this.$refs.LinkBillList);
        this.$refs.referReturnpo.show('其它', '退货订单', "", "1,3", this.model.vendorid)
      })
    },
    addBefore () {
      // this.scminventoyitemTable.dataSource = []
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
        this.isreddisabled = true
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