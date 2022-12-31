<template>
  <j-select-biz-component 
  :multiple="false" 
  :width="1000" 
  v-bind="configs" 
  v-on="$listeners" />
</template>

<script>
import JSelectBizComponent from '@/components/jeecgbiz/JSelectBizComponent'
import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'


export default {
  name: 'CVxeMaterialCell',
  components: { JSelectBizComponent },
  mixins:[JVxeCellMixins],
  props: ['value'],
  data () {
    return {
      settings: {
        name: '物料',
        displayKey: 'materialname',
        returnKeys: ['id', 'id', 'materialname'],
        listUrl: '/base/material/list',
        queryParamCode: 'materialname',
        queryParamText: '名称',
        columns: [
          { title: '物料编号', dataIndex: 'materialcode', align: 'center', width: '30%', widthRight: '70%' },
          { title: '物料名称', dataIndex: 'materialname', align: 'center', width: '30%' },
          { title: '物料型号', dataIndex: 'model', align: 'center', width: '20%' },
          { title: '物料规格', dataIndex: 'mspecs', align: 'center', width: '20%' }
        ]
      }
    }
  },
  computed: {
    configs () {
      return Object.assign({ value: this.value }, this.settings, this.$attrs)
    }
  }
}
</script>

<style lang="less" scoped>
</style>