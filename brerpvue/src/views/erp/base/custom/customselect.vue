<template>
  <j-select-biz-component :width="1000" v-bind="configs" v-on="$listeners" />
</template>

<script>
import JSelectBizComponent from '../../../../components/jeecgbiz/JSelectBizComponent'

export default {
  name: 'customSelect',
  components: { JSelectBizComponent },
  props: {
    value: {
      type: String,
      default: null
    },
    listUrl: {
      type: String,
      default: null
    },
    displayKey: {
      type: Array,
      default: () => []
    },
    selectNmae: {
      type: Array,
      default: () => []
    },
    returnKeys: {
      type: Array,
      default: () => []
    },
    columns:{
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      settings: {
        name: this.selectNmae,
        displayKey: this.displayKey,
        returnKeys: this.returnKeys,
        listUrl: this.listUrl,
        queryParamCode: 'name',
        queryParamText: '职务名称',
        columns: [
          // { title: '职务名称', dataIndex: 'name', align: 'center', width: '30%', widthRight: '70%' },
          // { title: '职务编码', dataIndex: 'code', align: 'center', width: '35%' },
          // { title: '职级', dataIndex: 'rank_dictText', align: 'center', width: '25%' }
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