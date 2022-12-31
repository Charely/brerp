<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="业务对象" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="objectid">
              <j-dict-select-tag  v-model="model.objectid" placeholder="请选择业务对象" dictCode="scmobject,objectname,id" ></j-dict-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="编号规则" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billrulecode">
              <a-input v-model="model.billrulecode" placeholder="请输入编号规则"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否包含年" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billruleyear">
              <j-switch v-model="model.billruleyear"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否包含月" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billrulemonth">
              <j-switch v-model="model.billrulemonth"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否包含日" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billruleday">
              <j-switch v-model="model.billruleday"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流水号位数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billrulenum">
              <a-input-number v-model="model.billrulenum" placeholder="请输入流水号位数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="断号重用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="billruledhsy">
              <j-switch v-model="model.billruledhsy"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="固定前缀" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixedcode">
              <a-input v-model="model.fixedcode" placeholder="请输入固定前缀"  ></a-input>
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
    name: 'ScmbillcoderuleForm',
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
          add: "/base/scmbillcoderule/add",
          edit: "/base/scmbillcoderule/edit",
          queryById: "/base/scmbillcoderule/queryById"
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