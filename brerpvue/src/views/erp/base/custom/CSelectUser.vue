<template>
  <j-select-biz-component :width="1000" v-bind="configs" v-on="$listeners" />
</template>

<script>
import JSelectBizComponent from '../../../../components/jeecgbiz/JSelectBizComponent'

export default {
  name: 'CSelectUser',
  components: { JSelectBizComponent },
  props: ['value'],
  data () {
    return {
      settings: {
        name: '人员',
        displayKey: 'realname',
        returnKeys: ['id', 'id', 'realname'],
        listUrl: '/sys/user/departUserList',
        queryParamCode: 'realname',
        queryParamText: '人员',
        columns: [
          { title: '人员编码', dataIndex: 'username', align: 'center', width: '40%', widthRight: '70%' },
          { title: '人员姓名', dataIndex: 'realname', align: 'center', width: '60%' }
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