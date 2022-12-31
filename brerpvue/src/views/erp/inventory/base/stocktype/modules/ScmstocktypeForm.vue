<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="类型编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typecode">
              <a-input v-model="model.typecode" placeholder="请输入类型编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类型名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typename">
              <a-input v-model="model.typename" placeholder="请输入类型名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否系统" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issys">
              <a-checkbox v-model="model.issys" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="入库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="instock">
              <a-checkbox  v-model="model.instock"  placeholder="请选择库存方向" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="outstock">
              <a-checkbox  v-model="model.outstock"  placeholder="请选择库存方向" />
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
    name: 'ScmstocktypeForm',
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
          add: "/inventory.base/scminventorytype/add",
          edit: "/inventory.base/scminventorytype/edit",
          queryById: "/inventory.base/scminventorytype/queryById"
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