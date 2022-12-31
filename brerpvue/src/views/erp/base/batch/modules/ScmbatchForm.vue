<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="库存组织" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imorgid">
              <a-input v-model="model.imorgid" placeholder="请输入库存组织"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warehouseid">
              <a-input v-model="model.warehouseid" placeholder="请输入仓库"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="物料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialid">
              <a-input v-model="model.materialid" placeholder="请输入物料"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="批号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batchcode">
              <a-input v-model="model.batchcode" placeholder="请输入批号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="批号数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batchqty">
              <a-input v-model="model.batchqty" placeholder="请输入批号数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原始批号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgbatchcode">
              <a-input v-model="model.orgbatchcode" placeholder="请输入原始批号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">
              <a-input v-model="model.vendorid" placeholder="请输入供应商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="有效期从" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fromdate">
              <j-date placeholder="请选择有效期从" v-model="model.fromdate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="有效期止" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enddate">
              <a-input v-model="model.enddate" placeholder="请输入有效期止"  ></a-input>
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
    name: 'ScmbatchForm',
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
          add: "/base/scmbatch/add",
          edit: "/base/scmbatch/edit",
          queryById: "/base/scmbatch/queryById"
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