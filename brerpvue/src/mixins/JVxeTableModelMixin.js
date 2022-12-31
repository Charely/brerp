import { VALIDATE_FAILED, getRefPromise, validateFormAndTables,validateFormModelAndTables} from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
import { httpAction, getAction } from '@/api/manage'
import { handleValueChange,handleJvxeValueChange } from '@/utils/erputils/erpcommonutils';
import { validateDuplicateValue, getNowFormatDate } from '@/utils/util'

export const JVxeTableModelMixin = {
  data() {
    return {
      title: '操作',
      visible: false,
      confirmLoading: false,
      scrolling: true,
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 }
      }
    }
  },
  created(){
    if(!this.model.id){
      this.model.billdate=getNowFormatDate();
    }
  },
  methods: {
    handleValueChange(event){
      handleJvxeValueChange(event)
    },


    /** 获取所有的JVxeTable实例 */
    getAllTable() {
      if (!(this.refKeys instanceof Array)) {
        throw this.throwNotArray('refKeys')
      }
      let values = this.refKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },

    /** 遍历所有的JVxeTable实例 */
    eachAllTable(callback) {
      // 开始遍历
      this.getAllTable().then(tables => {
        console.log("tables",tables)
        tables.forEach((item, index) => {
          if (typeof callback === 'function') {
            callback(item, index)
          }
        })
      })
    },

    /** 当点击新增按钮时调用此方法 */
    add() {
      if (typeof this.addBefore === 'function') this.addBefore()
      // 默认新增空数据
      let rowNum = this.addDefaultRowNum
      if (typeof rowNum !== 'number') {
        rowNum = 1
        console.warn('由于你没有在 data 中定义 addDefaultRowNum 或 addDefaultRowNum 不是数字，所以默认添加一条空数据，如果不想默认添加空数据，请将定义 addDefaultRowNum 为 0')
      }
      this.eachAllTable((item) => {
        //update-begin-author:taoyan date:20210315 for: 一对多jvex 默认几行不好使了 LOWCOD-1349
        setTimeout(()=>{
          item.addRows()
        }, 30)
        //update-end-author:taoyan date:20210315 for: 一对多jvex 默认几行不好使了 LOWCOD-1349
      })
      if (typeof this.addAfter === 'function') this.addAfter(this.model)
      this.edit(this.model)
    },
    /** 当点击了编辑（修改）按钮时调用此方法 */
    edit(record) {
      if (typeof this.editBefore === 'function') this.editBefore(record)
      this.visible = true
      this.activeKey = this.refKeys[0]
      this.$refs.form.resetFields()
      this.model = Object.assign({}, record)
      if(this.model.companyid === null || this.model.companyid === undefined ||
        this.model.companyid === ''){
        let currentuser = this.$store.getters.userInfo
        this.model.companyid = localStorage.getItem(currentuser.id+'_list_companyid');
      }
      if (typeof this.editAfter === 'function') this.editAfter(this.model)
    },
    /** 关闭弹窗，并将所有JVxeTable实例回归到初始状态 */
    close() {
      this.visible = false
      this.eachAllTable((item) => {
        item._remove()
      })
      this.$emit('close')
    },

    /** 查询某个tab的数据 */
    requestSubTableData(url, params, tab, success) {
      tab.loading = true
      getAction(url, params).then(res => {
        let { result } = res
        let dataSource = []
        if (result) {
          if (Array.isArray(result)) {
            dataSource = result
          } else if (Array.isArray(result.records)) {
            dataSource = result.records
          }
        }
        tab.dataSource = dataSource
        typeof success === 'function' ? success(res) : ''
      }).finally(() => {
        tab.loading = false
      })
    },
    /** 发起请求，自动判断是执行新增还是修改操作 */
    request(formData) {
      let url = this.url.add, method = 'post'
      if (this.model.id) {
        url = this.url.edit
        method = 'put'
      }
      this.confirmLoading = true
      console.log("formData===>",formData);
      httpAction(url, formData, method).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.$emit('ok')
          this.close()
        } else {
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.confirmLoading = false
      })
    },

    /* --- handle 事件 --- */

    /** ATab 选项卡切换事件 */
    handleChangeTabs(key) {
      // 自动重置scrollTop状态，防止出现白屏
      getRefPromise(this, key).then(vxeTable => {
        vxeTable.resetScrollTop()
      })
    },
    /** 关闭按钮点击事件 */
    handleCancel() {
      this.close()
    },
    /** 确定按钮点击事件 */
    handleOk() {
      /** 触发表单验证 */

      console.log("getAlltable():"+JSON.stringify(this.getAllTable()));

      this.getAllTable().then(tables => {
        /** 一次性验证主表和所有的次表 */
        return validateFormModelAndTables(this.$refs.form,this.model, tables)
      }).then(allValues => {
        /** 一次性验证一对一的所有子表 */
        return this.validateSubForm(allValues)
      }).then(allValues => {
        if (typeof this.classifyIntoFormData !== 'function') {
          throw this.throwNotFunction('classifyIntoFormData')
        }

        if(allValues.tablesValue.length > 0){
          let tabledata = allValues.tablesValue[0].tableData;
          if(tabledata.length > 0){
            tabledata.forEach(item=>{
              if(item.hasOwnProperty("id")){
                if(item.id.indexOf("row_") != -1 ){
                  delete item.id;
                }
              }
            })
          }
        }
        let formData = this.classifyIntoFormData(allValues)
        // 发起请求
        return this.request(formData)
      }).catch(e => {
        if (e.error === VALIDATE_FAILED) {
          // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
          this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
        } else {
          console.error(e)
        }
      })
    },
//校验所有子表表单
    validateSubForm(allValues){
      return new Promise((resolve) => {
        resolve(allValues)
      })
    },
    /* --- throw --- */

    /** not a function */
    throwNotFunction(name) {
      return `${name} 未定义或不是一个函数`
    },

    /** not a array */
    throwNotArray(name) {
      return `${name} 未定义或不是一个数组`
    }

  }
}