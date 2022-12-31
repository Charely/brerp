<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="格式编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="formatcode">
              <a-input v-model="model.formatcode" placeholder="请输入格式编号"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="格式名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="formatname">
              <a-input v-model="model.formatname" placeholder="请输入格式名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="系统配置" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="issys">
              <a-checkbox v-model="model.issys" placeholder="请输入系统配置"></a-checkbox>
            </a-form-model-item>
          </a-col>
          <a-col :xs="24" :sm="12">
            <a-form-model-item label="业务对象" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="objectid">
              <j-search-select-tag v-model="model.objectid" placeholder="请输入业务对象" dict="scmobject,objectname,id"
                disabled="true">
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
    <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="自定义格式表分录" :key="refKeys[0]" :forceRender="true">
        <j-editable-table :ref="refKeys[0]" :loading="scmcustomformatitemTable.loading"
          :columns="scmcustomformatitemTable.columns" :dataSource="scmcustomformatitemTable.dataSource" :maxHeight="300"
          :disabled="formDisabled" :rowNumber="true" :rowSelection="true" :actionButton="true" />
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

import { FormTypes, getRefPromise, VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { validateDuplicateValue } from '@/utils/util'

export default {
  name: 'ScmcustomformatForm',
  mixins: [JEditableTableModelMixin],
  components: {
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
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      refKeys: ['scmcustomformatitem',],
      tableKeys: ['scmcustomformatitem',],
      activeKey: 'scmcustomformatitem',
      objectid: '',
      // 自定义格式表分录
      scmcustomformatitemTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '控件列id',
            key: 'colid',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件显示名称',
            key: 'colname',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件列类型',
            key: 'coltype',
            type: FormTypes.sel_search,
            width: "150px",
            dictCode: 'scmformtypes,formtypename,formtypecode',
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件列宽度',
            key: 'colwidth',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件列是否可见',
            key: 'colisvisable',
            type: FormTypes.checkbox,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件是否只读',
            key: 'onlyread',
            type: FormTypes.checkbox,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件列顺序',
            key: 'colorder',
            type: FormTypes.inputNumber,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
          {
            title: '控件列字典',
            key: 'coldictcode',
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
          },
        ]
      },
      url: {
        add: "/base/scmcustomformat/add",
        edit: "/base/scmcustomformat/edit",
        scmcustomformatitem: {
          list: '/base/scmcustomformat/queryScmcustomformatitemByMainId'
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
    },
    objectid: {
      type: String,
      required: true,
    }
  },
  computed: {
    formDisabled () {
      return this.disabled
    },
  },
  created () {
    console.log("this.objectid:" + this.objectid);
    this.model.objectid = this.objectid;
  },
  methods: {
    addBefore () {
      this.scmcustomformatitemTable.dataSource = []
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
        let params = { id: this.model.id }
        this.requestSubTableData(this.url.scmcustomformatitem.list, params, this.scmcustomformatitemTable)
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
        scmcustomformatitemList: allValues.tablesValue[0].values,
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