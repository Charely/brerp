<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partnercode">
              <a-input v-model="model.partnercode" placeholder="请输入编码"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partnername">
              <a-input v-model="model.partnername" placeholder="请输入名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="简称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partnerjc">
              <a-input v-model="model.partnerjc" placeholder="请输入简称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="往来性质" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partnertype">
              <j-multi-select-tag type="list_multi" v-model="model.partnertype" dictCode="wlxz" placeholder="请选择往来性质" />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="内部单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isinternalpartner">
              <a-checkbox  v-model="model.isinternalpartner"  placeholder="内部单位"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="运输商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iscarrier">
              <a-checkbox  v-model="model.iscarrier"  placeholder="内部单位"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partnerlinkman">
              <j-search-select-tag v-model="model.partnerlinkman" placeholder="请输入联系人" dict="scmcontact,contactname,id"></j-search-select-tag>
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
import JMultiSelectTag from '@/components/dict/JMultiSelectTag'
export default {
  name: 'ScmpartnerForm',
  components: {
    JMultiSelectTag
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
      validatorRules: {
      },
      url: {
        add: "/base/scmpartner/add",
        edit: "/base/scmpartner/edit",
        queryById: "/base/scmpartner/queryById"
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
          if (!this.model.id) {
            httpurl += this.url.add;
            method = 'post';
          } else {
            httpurl += this.url.edit;
            method = 'put';
          }
          httpAction(httpurl, this.model, method).then((res) => {
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