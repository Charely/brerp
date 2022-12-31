<template>
  <div>
    <a-input v-show="!materialid" @click="openSelect" placeholder="请选择物料" v-model="materialname" readOnly
      class="jvxe-select-input" :disabled="componentDisabled">
      <a-icon slot="prefix" type="user" title="物料选择控件" />
    </a-input>
    <select-material-modal ref="selectModal" 
    :modal-width="modalWidth" 
    :multi="false"
     :materialid="materialid"
     :materialtype="materialtype"
      @ok="selectOK" 
      :store="storeField" 
      :text="textField" 
      @initComp="initComp" />

    <span style="display: inline-block;height:100%;padding-left:14px" v-if="materialid">
      <span @click="openSelect" style="display: inline-block;vertical-align: middle">{{ materialname }}</span>
      <a-icon v-if="!componentDisabled" style="margin-left:5px;vertical-align: middle" type="close-circle"
        @click="handleEmpty" title="清空" />
    </span>
  </div>
</template>

<script>
import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'
import SelectMaterialModal from '@/components/jeecgbiz/modal/CSelectMaterialModal'

export default {
  name: 'CVxeMaterialSelectCell',
  mixins: [JVxeCellMixins],
  components: { SelectMaterialModal },
  data () {
    return {
      materialid: '',
      materialname: '',
      innerUserValue: '',
      selectedOptions: []
    }
  },
  computed: {
    custProps () {
      const { materialid, originColumn: col, caseId, cellProps } = this
      return {
        ...cellProps,
        value: materialid,
        field: col.field || col.key,
        groupId: caseId,
        class: 'jvxe-select'
      }
    },
    componentDisabled () {
      console.log('333', this.cellProps)
      if (this.cellProps.disabled == true) {
        return true
      }
      return false
    },
    modalWidth () {
      if (this.cellProps.modalWidth) {
        return this.cellProps.modalWidth
      } else {
        return 800
      }
    },
    materialtype(){
      if(this.cellProps.materialtype){
        return this.cellProps.materialtype
      }else{
        return ""
      }
    },
    multi () {
      if (this.cellProps.multi == false) {
        return false
      } else {
        return true
      }
    },
    storeField () {
      if (this.originColumn) {
        const str = this.originColumn.fieldExtendJson
        if (str) {
          let json = JSON.parse(str)
          if (json && json.store) {
            return json.store
          }
        }
      }
      return 'id'
    },
    textField () {
      if (this.originColumn) {
        const str = this.originColumn.fieldExtendJson
        if (str) {
          let json = JSON.parse(str)
          if (json && json.text) {
            return json.text
          }
        }
      }
      return 'materialname'
    }
  },
  watch: {
    innerValue: {
      immediate: true,
      handler (val) {
        if (val == null || val === '') {
          this.materialid = ''
        } else {
          this.materialid = val
        }
      }
    }
  },
  methods: {
    openSelect () {
      // disabled 不弹窗
      if (this.componentDisabled) {
        return
      }
      this.$refs.selectModal.showModal()
    },
    selectOK (rows, idstr) {
      // console.log("当前选中用户", rows)
      // console.log("当前选中用户ID", idstr)
      if (!rows) {
        this.materialname = ''
        this.materialid = ''
      } else {
        this.materialid = rows.map(row => row[this.storeField]).join(',');
        this.materialname = rows.map(row => row[this.textField]).join(',');
      }
      this.handleChangeCommon(this.materialid)
    },
    handleEmpty () {
      this.selectOK('')
    },
    initComp (materialname) {
      this.materialname = materialname
    },
  },
  enhanced: {
    switches: {
      visible: true
    },
    translate: {
      enabled: false
    }
  }
}
</script>

<style scoped>
/deep/ .jvxe-select-input .ant-input {
  border: none !important;
}
</style>