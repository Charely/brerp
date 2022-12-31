<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="公司" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="companyid" >
              <j-search-select-tag v-model="model.companyid" placeholder="请选择公司" dict="sys_depart where org_type='1',depart_name,id" ></j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属仓库" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ownerwhid">
              <j-search-select-tag  ref="wfref" v-model="model.ownerwhid" placeholder="请输入仓库" :buttons="false"
                dict="warehouse,name,id" async="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="货位编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="locationcode">
              <a-input v-model="model.locationcode" placeholder="请输入货位编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="货位名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="locationname">
              <a-input v-model="model.locationname" placeholder="请输入货位名称"  ></a-input>
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
    name: 'ScmstocklocationForm',
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
          add: "/base/scmstocklocation/add",
          edit: "/base/scmstocklocation/edit",
          queryById: "/base/scmstocklocation/queryById"
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
    watch:{
      'model.companyid':{
        handler(val,old){
       // this.warehousedict="warehouse where companyid ="+val+',name,code';
        if(!this.model.id){
          this.$refs.wfref.newdict="warehouse where companyid = '"+val+"' and islocation='true',name,id";
          if(val != old){
            this.model.warehouseid=''
          }
          }
        }
      },
      'model.ownerwhid':{
        handler(val){
          this.$refs.wfref.newdict="warehouse where companyid = '"+this.model.companyid+"' and islocation='true',name,id";
        }
      }
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