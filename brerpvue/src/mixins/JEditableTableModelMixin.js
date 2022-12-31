import JEditableTable from '@/components/jeecg/JEditableTable'
import { VALIDATE_NO_PASSED, getRefPromise,validateFormModelAndTables} from '@/utils/JEditableTableUtil'
import { httpAction, getAction } from '@/api/manage'
import { FormTypes } from '@/utils/JEditableTableUtil'
import { handleValueChange } from '@/utils/erputils/erpcommonutils';

export const JEditableTableModelMixin = {
  components: {
    JEditableTable
  },
  data() {
    return {
      title: '操作',
      visible: false,
      confirmLoading: false,
      model:{},
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
   // console.log("created:"+this.model.id);
  },
  mounted(){
    console.log("mounted:"+JSON.stringify(this.model));
    if(this.itemTable){
      //分录列的信息
    }
  },
  beforeMount (){
  
  },
  methods: {

    handleValueChange(event){
      handleValueChange(event)
    },
    //获取自定义字段信息
    getModelItems(item){
      return "model."+item.customcode;
    },
    getMainData(url,params){
      getAction(url,params).then((res)=>{
        if(res.success){
          this.model=res.result;
        }
      })
    },
    /** 获取所有的editableTable实例 */
    getAllTable() {
      if (!(this.refKeys instanceof Array)) {
        throw this.throwNotArray('refKeys')
      }
      let values = this.refKeys.map(key => getRefPromise(this, key))
      return Promise.all(values)
    },

    /** 遍历所有的JEditableTable实例 */
    eachAllTable(callback) {
      // 开始遍历
      this.getAllTable().then(tables => {
        tables.forEach((item, index) => {
          if (typeof callback === 'function') {
            callback(item, index)
          }
        })
      })
    },
    /** 当点击新增按钮时调用此方法 */
    add() {
      //update-begin-author:lvdandan date:20201113 for:LOWCOD-1049 JEditaTable,子表默认添加一条数据，addDefaultRowNum设置无效 #1930
      return new Promise((resolve) => {
        this.tableReset();
        resolve();
      }).then(() => {
        if (typeof this.addBefore === 'function') this.addBefore()
        // 默认新增空数据
        let rowNum = this.addDefaultRowNum
        if (typeof rowNum !== 'number') {
          rowNum = 1
          console.warn('由于你没有在 data 中定义 addDefaultRowNum 或 addDefaultRowNum 不是数字，所以默认添加一条空数据，如果不想默认添加空数据，请将定义 addDefaultRowNum 为 0')
        }
        this.eachAllTable((item) => {
          item.add(rowNum)
        })
        if (typeof this.addAfter === 'function') this.addAfter(this.model)

        let params={
          objectcode:this.maincusObject,
        }
        getAction("/base/scmcustomfields/getcustomfieldsbyobjectcode",params).then(res=>{
          if(res.success){
            let cusret=res.result;
            this.maincusList=cusret;
            for(let i=0;i<cusret.length;i++){
              this.$set(this.maincusItems,cusret[i]["customcode"],"");
            }
    
            console.log("get the mainmodelifno....--->"+JSON.stringify(this.maincusItems));
          }
        })
        //获取分录自定义字段
        let itemParams={
          objectcode: this.itemcusObject,
        }
        this.requestTableCustomCode("/base/scmcustomfields/getcustomfieldsbyobjectcode",itemParams,this.itemTable);

        this.edit(this.model)
      })
      //update-end-author:lvdandan date:20201113 for:LOWCOD-1049 JEditaTable,子表默认添加一条数据，addDefaultRowNum设置无效 #1930
    },
    /** 当点击了编辑（修改）按钮时调用此方法 */
    edit(record) {
      if(record && '{}'!=JSON.stringify(record)&&record.id){
        this.tableReset();
      }
      if (typeof this.editBefore === 'function') this.editBefore(record)
      this.visible = true
      this.activeKey = this.refKeys[0]
      this.$refs.form.resetFields()
      this.model = Object.assign({}, record);
      
      let params={
        objectcode:this.maincusObject,
      }
      getAction("/base/scmcustomfields/getcustomfieldsbyobjectcode",params).then(res=>{
        if(res.success){
          let cusret=res.result;
          this.maincusList=cusret;
          for(let i=0;i<cusret.length;i++){
            this.$set(this.maincusItems,cusret[i]["customcode"],record[cusret[i]["customcode"]]);
          }
        }
      })

       //获取分录自定义字段
       let itemParams={
        objectcode: this.itemcusObject,
      }
      this.requestTableCustomCode("/base/scmcustomfields/getcustomfieldsbyobjectcode",itemParams,this.itemTable);

      if (typeof this.editAfter === 'function') this.editAfter(this.model)
    },
    /** 关闭弹窗，并将所有JEditableTable实例回归到初始状态 */
    close() {
      this.visible = false
      this.$emit('close')
    },
    //清空子表table的数据
    tableReset(){
      this.eachAllTable((item) => {
        item.clearRow()
      })
    },
    /**
     * 获取分录自定义字段的信息
     * @param {*} url 
     * @param {*} params 
     * @param {*} tab 
     */
    requestTableCustomCode(url,params,tab){
      if(tab){
      tab.loading=true
      //获取自定义的格式信息
      let customformaturl="/base/scmcustomformat/getcolumnlist";
      getAction(customformaturl,params).then(res=>{
        if(res.success){
          const cusret=res.result;
          console.log("columns:"+JSON.stringify(cusret));
          let newColumns=[];
          for(let i=0;i<cusret.length;i++){
            let currentitemtype=cusret[i]

            let newColumn={
              title: currentitemtype.colname,
              key:currentitemtype.colid,
              width:currentitemtype.colwidth,
              placeholder: '请输入${title}',
              defaultValue: '',
            }
            if(currentitemtype.colvisable === 'false'){
              this.$set(newColumn,"type",FormTypes.hidden);
              newColumns.push(newColumn);
              continue
            }

            if(currentitemtype.onlyread === 'true'){
              this.$set(newColumn,'disabled',"true")
            }
            if(currentitemtype.formtype === 'sel_material'){
              this.$set(newColumn,'type',FormTypes.sel_material);
            }else if(currentitemtype.formtype === 'input'){
              this.$set(newColumn,'type',FormTypes.input);
            } else if(currentitemtype.formtype === 'input_number'){
              this.$set(newColumn,'type',FormTypes.inputNumber)
            } else if(currentitemtype.formtype === 'date'){
              this.$set(newColumn,'type',FormTypes.date)
            } else if(currentitemtype.formtype === 'sel_vendor'){
              this.$set(newColumn,'type',FormTypes.sel_vendor)
            } else if(currentitemtype.formtype === 'search'){
              this.$set(newColumn,'type',FormTypes.sel_search)
              this.$set(newColumn,'dictCode',currentitemtype.coldict)
            } 
            newColumns.push(newColumn);
          }

          tab.columns=newColumns;

        }
      })

      getAction(url,params).then(res=>{
        const cusret=res.result;
        let origcolumn=tab.columns;
        if(cusret){
          for(let i=0;i<cusret.length;i++){
            let curItem=cusret[i];
            let newColumn={
              title: curItem.customname,
            key: curItem.customcode,
            type: FormTypes.input,
            width: "100px",
            placeholder: '请输入${title}',
            defaultValue: '',
            }
            var result = origcolumn.some(item=>{
              if(item.key === curItem.customcode){
                return true
              }
            })
            if(!result){
              origcolumn.push(newColumn);
            }
          }
        }
        tab.columns=origcolumn;
      }).finally(()=>{
        tab.loading=false
      })
    }
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
      getRefPromise(this, key).then(editableTable => {
        editableTable.resetScrollTop()
      })
    },
    /** 关闭按钮点击事件 */
    handleCancel() {
      this.close()
    },
    /** 确定按钮点击事件 */
    handleOk() {

      console.log("this is aaa>......"+JSON.stringify(this.maincusItems));
      /** 触发表单验证 */
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
        let formData = this.classifyIntoFormData(allValues)
        if(this.maincusItems != null && this.maincusItems != undefined){
          const newformData=Object.assign(this.maincusItems,formData);
          return this.request(newformData)
        }else{
          return this.request(formData);
        }
       // formData.customdata=this.maincusItems;
        // 发起请求
       
      }).catch(e => {
        if (e.error === VALIDATE_NO_PASSED) {
          // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
          //update--begin--autor:liusq-----date:20210316------for：未通过表单验证跳转tab问题------
          this.activeKey = e.index == null ? this.activeKey : (e.paneKey?e.paneKey:this.refKeys[e.index])
          //update--end--autor:liusq-----date:20210316------for：未通过表单验证跳转tab问题------
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