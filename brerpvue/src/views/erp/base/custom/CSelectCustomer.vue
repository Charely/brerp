<template>
    <j-select-biz-component :multiple="false" :width="1000" v-bind="configs" v-on="$listeners" />
  </template>
  
  <script>
  import JSelectBizComponent from '../../../../components/jeecgbiz/JSelectBizComponent'
  
  export default {
    name: 'CSelectCustomer',
    components: { JSelectBizComponent },
    props: ['value'],
    data () {
      return {
        settings: {
          name: '客户名称',
          displayKey: 'partnername',
          returnKeys: ['id', 'id', 'partnercode'],
          listUrl: '/base/scmpartner/vendorlist/customer',
          queryParamCode: 'partnercode',
          queryParamText: '客户名称',
          columns: [
            { title: '客户编号', dataIndex: 'partnercode', align: 'center', width: '40%', widthRight: '70%' },
            { title: '客户名称', dataIndex: 'partnername', align: 'center', width: '30%' },
            { title: '备注', dataIndex: 'remark', align: 'center', width: '30%' }
          ]
        }
      }
    },
    computed: {
      configs () {
        return Object.assign({ value: this.value },this.settings, this.$attrs)
      }
    }
  }
  </script>
  
  <style lang="less" scoped>
  </style>