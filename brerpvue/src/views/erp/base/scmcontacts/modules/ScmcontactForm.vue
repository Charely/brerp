<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="联系人头像" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contactimg">
              <j-image-upload isMultiple  v-model="model.contactimg" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contactname">
              <a-input v-model="model.contactname" placeholder="请输入联系人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="所属公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid">
              <j-search-select-tag v-model="model.companyid" placeholder="请输入所属公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="关联往来单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainpartnerid">
              <j-search-select-tag v-model="model.mainpartnerid" placeholder="请输入关联往来单位" dict="scmpartner,partnername,id"  ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="固定电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fixedphone">
              <a-input v-model="model.fixedphone" placeholder="请输入固定电话"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="移动电话" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mobilephone">
              <a-input v-model="model.mobilephone" placeholder="请输入移动电话"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="邮箱" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="email">
              <a-input v-model="model.email" placeholder="请输入邮箱"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="打印" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fax">
              <a-input v-model="model.fax" placeholder="请输入打印"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">
              <a-input v-model="model.address" placeholder="请输入地址"  ></a-input>
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
    name: 'ScmcontactForm',
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
          add: "/base/scmcontact/add",
          edit: "/base/scmcontact/edit",
          queryById: "/base/scmcontact/queryById"
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