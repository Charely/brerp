<template>
  <j-popup
    v-bind="popupProps"
    @input="handlePopupInput"
  />
</template>

<script>
  import JVxeCellMixins, { dispatchEvent, vModel } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'

  export default {
    name: 'JVxePopupCell',
    mixins: [JVxeCellMixins],
    computed: {
      popupProps() {
        const {innerValue, originColumn: col, caseId, cellProps} = this

        // let newValue=''

        // if(innerValue === null || innerValue === undefined){
        //   let showTextCol = col.field
        //   newValue = this.row[showTextCol]
        // }
        return {
          ...cellProps,
          value: innerValue,
          field: col.field,
          code: col.popupCode,
          orgFields: col.orgFields,
          destFields: col.destFields,
          groupId: caseId,
          param: col.param,
          sorter: col.sorter,
        }
      },
    },
    methods: {
      /** popup回调 */
      handlePopupInput(value, others) {
        const {row, originColumn: col} = this
        // 存储输入的值
        let popupValue = value
        if (others && Object.keys(others).length > 0) {
          Object.keys(others).forEach(key => {
            let currentValue = others[key]
            // 当前列直接赋值，其他列通过vModel赋值
            // if (key === col.key) {
            //   popupValue = currentValue
            // } else {
            //   vModel.call(this, currentValue, row, key)
            // }
            if(key === col.field){
              popupValue=currentValue
            }else{
              vModel.call(this,currentValue,row,key)
            }
          })
        }
        this.handleChangeCommon(popupValue)
      },
    },
    // 【组件增强】注释详见：JVxeCellMixins.js
    enhanced: {
      aopEvents: {
        editActived(event) {
          dispatchEvent.call(this, event, 'ant-input')
        },
      },
    },
  }
</script>

<style scoped>

</style>