<template>
  <a-spin :spinning="confirmLoading">
    <a-form ref="formRef" class="antd-modal-form" :labelCol="labelCol" :wrapperCol="wrapperCol">
      <a-row>
        <a-col :span="24">
          <a-form-item label="业务对象编号" v-bind="validateInfos.objectcode">
            <a-input v-model:value="formData.objectcode" placeholder="请输入业务对象编号" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="业务对象名称" v-bind="validateInfos.objectname">
            <a-input v-model:value="formData.objectname" placeholder="请输入业务对象名称" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="对应数据库表" v-bind="validateInfos.objecttable">
            <a-input v-model:value="formData.objecttable" placeholder="请输入对应数据库表" :disabled="disabled"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label="父级节点" v-bind="validateInfos.pid">
            <j-tree-select
              placeholder="请选择父级节点"
              v-model:value="formData.pid"
              dict="scmobject,objectcode,id"
              pidField="pid"
              pidValue="0"
              hasChildField="has_child"
              :disabled="disabled">
            </j-tree-select>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-spin>
</template>

<script lang="ts" setup>
  import { ref, reactive, defineExpose, nextTick, unref, defineProps, computed } from 'vue';
  import { defHttp } from '/@/utils/http/axios';
  import { useMessage } from '/@/hooks/web/useMessage';
  import moment from 'moment';
  import JTreeSelect from '/@/components/Form/src/jeecg/components/JTreeSelect.vue';
  import { getValueType } from '/@/utils';
  import {loadTreeData, saveOrUpdateDict} from '../Scmobject.api';
  import { Form } from 'ant-design-vue';
  
  const useForm = Form.useForm;
  const formRef = ref();
  const isUpdate = ref(true);
  const expandedRowKeys = ref([]);
  const treeData = ref([]);
  const pidField = ref<string>('pid');
  const emit = defineEmits(['register', 'ok']);
  let model: Nullable<Recordable> = null;
  const formData = reactive<Record<string, any>>({
    id: '',
    objectcode: '',   
    id: '',
    objectname: '',   
    id: '',
    objecttable: '',   
    id: '',
    pid: '',   
  });
  const { createMessage } = useMessage();
  const labelCol = ref<any>({ xs: { span: 24 }, sm: { span: 5 } });
  const wrapperCol = ref<any>({ xs: { span: 24 }, sm: { span: 16 } });
  const confirmLoading = ref<boolean>(false);
  //表单验证
  const validatorRules = {
  };
  const { resetFields, validate, validateInfos } = useForm(formData, validatorRules, { immediate: true });
  const props = defineProps({
    disabled: { type: Boolean, default: false },
  });
  
  /**
   * 新增
   */
  function add(obj = {}) {
    edit(obj);
  }

  /**
   * 编辑
   */
  function edit(record) {
    nextTick(async () => {
      resetFields();
      expandedRowKeys.value = [];
      treeData.value = await loadTreeData({ async: false, pcode: '' });
      //赋值
      Object.assign(formData, record);
      model = record
    });
  }

  /**
   * 根据pid获取展开的节点
   * @param pid
   * @param arr
   */
  function getExpandKeysByPid(pid, arr) {
    if (pid && arr && arr.length > 0) {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].key == pid && unref(expandedRowKeys).indexOf(pid) < 0) {
          expandedRowKeys.value.push(arr[i].key);
          getExpandKeysByPid(arr[i]['parentId'], unref(treeData));
        } else {
          getExpandKeysByPid(pid, arr[i].children);
        }
      }
    }
  }

  /**
   * 提交数据
   */
  async function submitForm() {
    // 触发表单验证
    await validate();
    confirmLoading.value = true;
    const isUpdate = ref<boolean>(false);
    //时间格式化
    if (formData.id) {
      isUpdate.value = true;
    }
    //循环数据
    for (let data in formData) {
      //如果该数据是数组并且是字符串类型
      if (formData[data] instanceof Array) {
        let valueType = getValueType(formRef.value.getProps, data);
        //如果是字符串类型的需要变成以逗号分割的字符串
        if (valueType === 'string') {
          formData[data] = formData[data].join(',');
        }
      }
    }
    await saveOrUpdateDict(formData, isUpdate.value)
      .then(async (res) => {
        if (res.success) {
          await getExpandKeysByPid(formData['pid'], unref(treeData));
          emit('ok', {
            isUpdate: unref(isUpdate),
            values: { ...formData },
            expandedArr: unref(expandedRowKeys).reverse(),
            // 是否更改了父级节点
            changeParent: model != null && model['pid'] != formData['pid'],
          });
          createMessage.success(res.message);
        } else {
          createMessage.warning(res.message);
        }
      })
      .finally(() => {
        confirmLoading.value = false;
      });
  }


  /**
   * 值改变事件触发
   * @param key
   * @param value
   */
  function handleFormChange(key, value) {
    formData[key] = value;
  }

  defineExpose({
    add,
    edit,
    submitForm,
  });
</script>

<style lang="less" scoped>
  .antd-modal-form {
    height: 500px !important;
    overflow-y: auto;
    padding: 24px 24px 24px 24px;
  }
</style>
