<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromunitid">
              <j-search-select-tag v-model="model.fromunitid" placeholder="请输入计量单位" dict="scmuom,uomname,id" />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tounitid">
              <j-search-select-tag v-model="model.tounitid" placeholder="请输入计量单位" dict="scmuom,uomname,id" />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="换算率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conversion">
              <a-input-number v-model="model.conversion" placeholder="请输入换算率" style="width: 100%" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'ScmuomconversionForm',
    components: {
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
        model:{
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
        validatorRules: {
        },
        url: {
          add: "/base/scmuomconversion/add",
          edit: "/base/scmuomconversion/edit",
          queryById: "/base/scmuomconversion/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
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