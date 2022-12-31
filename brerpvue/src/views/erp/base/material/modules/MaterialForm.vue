<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="物料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialtypeid">
              <j-dict-select-tag type="list" v-model="model.materialtypeid" dictCode="materialtype,materialtypename,id"
                placeholder="请选择物料类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialcode">
              <a-input v-model="model.materialcode" placeholder="请输入编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="物料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialname">
              <a-input v-model="model.materialname" placeholder="请输入物料名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="安全库存" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="safestock">
              <a-input-number v-model="model.safestock" placeholder="请输入安全库存" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="model">
              <a-input v-model="model.model" placeholder="请输入型号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mspecs">
              <a-input v-model="model.mspecs" placeholder="请输入规格"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="基本计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uom">
              <j-search-select-tag v-model="model.uom" placeholder="请输入基本计量单位" dict="scmuom,uomname,id" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="可销售" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isale">
              <a-checkbox v-model="model.issale" placeholder="请选择"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="可采购" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ispo">
              <a-checkbox v-model="model.ispo" placeholder="请输入单位"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="可库存" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isgd">
              <a-checkbox v-model="model.isgd" placeholder="请输入单位"></a-checkbox>
            </a-form-model-item>
          </a-col>      
          <a-col :span="6">
            <a-form-model-item label="默认税率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taxrate">
              <a-input-number v-model="model.taxrate" placeholder="请输入安全库存" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <a-tabs v-model="activeKey">
      <a-tab-pane tab="销售" :key="refKeys[0]" :forceRender="true" >
       <material-sale-form ref="materialSaleForm"></material-sale-form>
        </a-tab-pane>
        <a-tab-pane tab="采购" :key="refKeys[1]" :forceRender="true">
          <material-po-form ref="materialPoForm"></material-po-form>
        </a-tab-pane>
        <a-tab-pane tab="库存" :key="refKeys[2]" :forceRender="true">
          <material-stock-form ref="materialStockForm"></material-stock-form>
        </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
import { validateDuplicateValue } from '@/utils/util'
import { getRefPromise,VALIDATE_FAILED} from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
import MaterialSaleForm  from './MaterialSaleForm.vue'
import MaterialPoForm from './MaterialPoForm.vue'
import  MaterialStockForm from './MaterialStockForm.vue'
export default {
  name: 'MaterialForm',
  mixins:[],
  components: {
    MaterialSaleForm,
    MaterialPoForm,
    MaterialStockForm
  },
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false
    }
  },
  data () {
    return {
      model: {
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      refKeys: ['materialsale','materialpo','materialstock'],
      tableKeys: ['materialsale','materialpo','materialstock'],
      activeKey: 'materialsale',
      validatorRules: {
        materialtypeid: [{ required: true, message: '请选择物料类型' }],
        materialcode:[{required:true,message:"请填写物料编号"}],
        uom:[{required:true,message:'请选择计量单位'}],
      },
      url: {
        add: "/base/material/add",
        edit: "/base/material/edit",
        queryById: "/base/material/queryById",
        materialsale:{
          list:"/base/material/querymaterialsalebyparentid"
        },
        materialpo:{
          list:"/base/material/querymaterialpobyparentid"
        },
        materialstock:{
          list:"/base/material/querymaterialstockbyparentid"
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
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods: {
    getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
    add () {
      this.edit(this.modelDefault);
    },
    edit (record) {
      this.model = Object.assign({}, record);
      this.visible = true;

      //给子表单赋值
      if(this.model.id){
      this.$nextTick(() => {
          this.$refs.materialSaleForm.initFormData(this.url.materialsale.list,this.model.id)
          this.$refs.materialPoForm.initFormData(this.url.materialpo.list,this.model.id);
          this.$refs.materialStockForm.initFormData(this.url.materialstock.list,this.model.id);
        })
      }

    },

     getSaveFormData(){
      let main=this.model;

      return{
        ...main,
         materialSaleList: this.$refs.materialSaleForm.getFormData(),
          materialPoList:this.$refs.materialPoForm.getFormData(),
          materialStockList:this.$refs.materialStockForm.getFormData(),
      }
     } ,
    submitForm () {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate(valid => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = '';
          let method = '';
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }

          httpAction(httpurl, that.getSaveFormData(), method).then((res) => {
            if (res.success) {
              that.$message.success(res.message);
              that.$emit('ok');
            } else {
              that.$message.warning(res.message);
            }
          }).finally(() => {
            that.confirmLoading = false;
          })
        }

      })
    },
  }
}
</script>