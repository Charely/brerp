<template>
    <div>
      <a-input v-show="!batchid" @click="openSelect" placeholder="请选择货位" v-model="batchid" readOnly
        class="jvxe-select-input" :disabled="componentDisabled">
        <a-icon slot="prefix" type="user" title="货位选择控件" />
      </a-input>
      <select-batch-modal ref="selectModal" :modal-width="modalWidth" :multi="false" :batchid="batchid"
        :materialid="materialid" :companyid="companyid" @ok="selectOK" :store="storeField" :text="textField"
        @initComp="initComp" />
        
  
      <span style="display: inline-block;height:100%;padding-left:14px" v-if="batchid">
        <span @click="openSelect" style="display: inline-block;vertical-align: middle">{{ batchcode }}</span>
        <a-icon v-if="!componentDisabled" style="margin-left:5px;vertical-align: middle" type="close-circle"
          @click="handleEmpty" title="清空" />
      </span>
    </div>
  </template>
  
  <script>
  import JVxeCellMixins, { dispatchEvent } from '@/components/jeecg/JVxeTable/mixins/JVxeCellMixins'
  import SelectBatchModal from '@/components/jeecgbiz/modal/CSelectBatchModal'
  import { queryMaterialVoInfoById} from '@/api/erp/commonapi'
  export default {
    name: 'VxeStockLocationSelectCell',
    mixins: [JVxeCellMixins],
    components: { SelectBatchModal },
    data () {
      return {
        materialid: '',
        batchid: '',
        batchcode: '',
        companyid: '',
        innerUserValue: '',
        selectedOptions: []
      }
    },
    computed: {
      custProps () {
        const { batchid, originColumn: col, caseId, cellProps } = this
        return {
          ...cellProps,
          value: batchid,
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
        return 'batchcode'
      }
    },
    watch: {
      innerValue: {
        immediate: true,
        handler (val) {
          if (val == null || val === '') {
            this.batchid = ''
          } else {
            this.batchid = val
          }
        }
      }
    },
    methods: {
      async openSelect () {
        //判断物料是否管理批号
       // console.log("this._props.row:"+JSON.stringify(this._props.row))
        let currow=this._props.row;
        if(currow.materialid === "" || currow.materialid === undefined){
          this.$message.error("请先选择物料！");
          return;
        }
        let curmaterialid=currow.materialid;
        this.materialid=curmaterialid;
        let materialres=await queryMaterialVoInfoById({materialid:curmaterialid});
        if(materialres){
          if(materialres.success){
            let material=materialres.result;
            if(material!= null && material.isbatch!=null && material.isbatch =='true'){
            }else{
              this.$message.error("当前物料不管理批次");
              return;
            }
          }
        }
        
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
          this.batchcode = ''
          this.batchid = ''
        } else {
          this.batchid = rows.map(row => row[this.storeField]).join(',');
          this.batchcode = rows.map(row => row[this.textField]).join(',');
        }
        this.handleChangeCommon(this.batchid)
      },
      handleEmpty () {
        this.selectOK('')
      },
      initComp (batchcode) {
        this.batchcode = batchcode
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