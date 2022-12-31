<template>
    <j-form-container :disabled="disabled">
        <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
          <a-row>
            <a-col :span="8">
              <a-form-model-item label="库存计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stockuomid">
                <j-search-select-tag v-model="model.stockuomid"  dict="scmuom,uomname,id" placeholder="请选择库存计量单位"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="默认仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="defwarehouseid">
                <j-search-select-tag v-model="model.defwarehouseid"  dict="warehouse,name,id" placeholder="请选择默认仓库"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
            <a-form-model-item label="批号管理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isbatch">
              <a-checkbox v-model="model.isbatch" placeholder="请输入批号管理">
              </a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="序列号管理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issn">
              <a-checkbox v-model="model.issn" placeholder="请输入批号管理">
              </a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="效期管理" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isvalidcontrol">
              <a-checkbox v-model="model.isvalidcontrol" placeholder="请输入效期管理">
              </a-checkbox>
            </a-form-model-item>
          </a-col>
          </a-row>
        </a-form-model>
    </j-form-container>
  </template>
  <script>
    import { getAction } from '@/api/manage'
    import { validateDuplicateValue } from '@/utils/util'
    import JFormContainer from '@/components/jeecg/JFormContainer'
   import { VALIDATE_FAILED } from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
    export default {
      name: 'MaterialStockForm',
      components: {
        JFormContainer
      },
      props:{
        disabled: {
          type: Boolean,
          default: false,
          required: false
        }
      },
      data () {
        return {
          model:{
          },
          labelCol: {
            xs: { span: 8 },
            sm: { span: 5 },
          },
          wrapperCol: {
            xs: { span: 8 },
            sm: { span: 16 },
          },
          validatorRules: {
          },
          confirmLoading: false,
        }
      },
      created () {
      //备份model原始值
        this.modelDefault = JSON.parse(JSON.stringify(this.model));
      },
      methods:{
        initFormData(url,id){
          this.clearFormData()
          if(!id){
            this.edit(this.modelDefault)
          }else{
            getAction(url,{id:id}).then(res=>{
              if(res.success){
                let records = res.result
                if(records && records.length>0){
                  this.edit(records[0])
                }
              }
            })
          }
        },
        edit(record){
          this.model = Object.assign({}, record)
        },
        getFormData(){
          let formdata_arr = []
          this.$refs.form.validate(valid => {
            if (valid) {
              let isNullObj = true
              Object.keys(this.model).forEach(key=>{
                if(this.model[key]){
                  isNullObj = false
                }
              })
              if(!isNullObj){
                formdata_arr.push(this.model)
              }
            }else{
              this.$emit("validateError","订单客户表单校验未通过");
            }
          })
          return formdata_arr;
        },
         validate(index){
          return new Promise((resolve, reject) => {
            // 验证主表表单
           this.$refs.form.validate(valid => {
              !valid ? reject({ error: VALIDATE_FAILED ,index}) : resolve()
            })
          }).then(values => {
            return Promise.resolve()
          }).catch(error => {
            return Promise.reject(error)
          })
  
        },
        popupCallback(value,row){
         this.model = Object.assign(this.model, row);
        },
        clearFormData(){
          this.$refs.form.clearValidate()
        },
      }
    }
  </script>
  