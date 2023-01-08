/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util';
import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID } from "@/store/mutation-types"
import store from '@/store'
// import { hiprint} from '../views/printmodule/hiprint/hiprint.bundle'

export const JeecgListMixin = {
  data(){
    return {
      /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
      queryParam: {},
      /* 数据源 */
      dataSource:[],
      /* 分页参数 */
      ipagination:{
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter:{
        column: 'createTime',
        order: 'desc',
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading:false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus:false,
      /* 高级查询条件生效状态 */
      superQueryFlag:false,
      /* 高级查询条件 */
      superQueryParams: '',
      /** 高级查询拼接方式 */
      superQueryMatchType: 'and',
    }
  },

  created() {
      if(!this.disableMixinCreated){
        console.log(' -- mixin created -- ')
        //todo 将上次选择的公司自动赋值到控件上
        let currentuser = this.$store.getters.userInfo
        var oldcompanyid = localStorage.getItem(currentuser.id+"_list_companyid")
        if(oldcompanyid!=null && oldcompanyid != undefined && oldcompanyid != ''){
          this.queryParam.companyid = oldcompanyid;
        }
        this.loadData();
        //初始化字典配置 在自己页面定义
        this.initDictConfig();

        let params={
          objectcode:this.objectcode
        };
        getAction("/base/scmcustomfields/getcustomfieldsbyobjectcode",params).then(res=>{
            if(res.success){
              let cusColumn=res.result;
              if(cusColumn){

                for(let i=0;i<cusColumn.length;i++){
                  let newColumn={
                    title:cusColumn[i]["customname"],
                    dataIndex:cusColumn[i]["customcode"],
                    align:'center',
                    width:150
                  };
                  this.columns.push(newColumn);
                }
              }
              var index=this.columns.findIndex(item=> item.dataIndex === "action");
              if(index != -1){
                this.columns.splice(index,1);
              }
              let constColumn={
                title: '操作',
                dataIndex: 'action',
                align: "center",
                fixed: "right",
                width: 147,
                scopedSlots: { customRender: 'action' },
              };
              this.columns.push(constColumn);
            }
         
          })
      }
  },
  computed: {
    //token header
    tokenHeader(){
      let head = {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)}
      let tenantid = Vue.ls.get(TENANT_ID)
      if(tenantid){
        head['tenant-id'] = tenantid
      }
      return head;
    }
  },
  watch:{
    'queryParam.companyid':{
      handler(val,old){
        // if(val.hasOwnProperty('companyid')){
        //   if(val.companyid != null && val.companyid != '' && val.companyid != undefined){
            
        //   }
        // }
        if(val!=null && val != ''){
          let currentuser = this.$store.getters.userInfo
          localStorage.setItem(currentuser.id+"_list_companyid",val)
          this.loadData();
        }
      }
    }
  },
  methods:{

    getprintData(url,params){
      return getAction(url,params);
    },

    /**打印操作 */
    async handlePrint(objectcode){
      //TODO 获取要打印的数据源
      //根据业务对象获取打印数据源
      let printurl="/base/scmprintformat/querybyobject"
      let params={
        objectcode:objectcode
      }
      
      let ids=''
      for(let i=0;i<this.selectedRowKeys.length;i++){
        ids+=this.selectedRowKeys[i]+','
      }

      let printdataurl="/base/scmprintdatasource/queryprintdatabyobjectcode"
      let dataparams={
        objectcode:objectcode,
        ids:ids
      }

      let printdata=await this.getprintData(printdataurl,dataparams);
    

      //TODO 1.1 先验证单页打印
      getAction(printurl,params).then(res=>{
        if(res.success){
           //TODO 获取到打印数据格式了，那么需要将打印格式和打印数据源对应起来
          let printformat=res.result.printformat;
          let hiprintTemplate=new hiprint.PrintTemplate({
            template: JSON.parse(printformat).aProviderModule,
            dataMode: 1, // 1:getJson 其他：getJsonTid 默认1
            history: false, // 是否需要 撤销重做功能
            onDataChanged: (type, json) => {
              console.log(type); // 新增、移动、删除、修改(参数调整)、大小、旋转
              console.log(json); // 返回 template
            },
            settingContainer: '#PrintElementOptionSetting',
            paginationContainer: '.hiprint-printPagination'
          });
          //TODO 获取打印数据
          if(printdata.success){
            this.$refs.preView.show(hiprintTemplate, printdata.result, 210)
          }else{
            this.$message.error("找不到打印数据源");
            return;
          }        
          
        }else{
          this.$message.error(res.result);
          return;
        }
      })
    },

    /**进行审批操作 */
    handleAudit(record,datacode,djtype){
      if(record.status === '1'){
        this.$message.error("单据正在审批中不允许再次提交审批");
        return;
      }else if(record.status === '2'){
        this.$message.error("单据已审批通过不允许再次提交审批");
        return;
      }
      let url="/flow/common/startprocess";
      let params={
        dataid:record.id,
        datacode:datacode,
        djtype:djtype
      };
      getAction(url,params).then((res)=>{
        if(res.success){
          this.$message.success("流程发起成功");
        }else{
          this.$message.error(res.result);
        }
      })
    },
    /**不使用审批流进行审批操作 */
    handleaudit(flag){
      let ids=''
      for(let i=0;i<this.selectedRowKeys.length;i++){
        ids+=this.selectedRowKeys[i]+','
      }
      let params={ids:ids,flag:flag}
      let that=this;
      getAction(this.url.auditurl,params).then(res=>{
        if(res.success){
          if(flag === '2'){
            this.$message.success("审批成功")
            that.loadData();
          }else{
            this.$message.success("取消审批成功")
            that.loadData();
          }
        }
      })
      //this.loadData();
    },
    loadData(arg) {
      if(!this.url.list){
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      // if(!params.hasOwnProperty("companyid")){
      //   return;
      // }
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          this.dataSource = res.result.records||res.result;
          if(res.result.total)
          {
            this.ipagination.total = res.result.total;
          }else{
            this.ipagination.total = 0;
          }
          //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
        }else{
          this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    initDictConfig(){
      console.log("--这是一个假的方法!")
    },
    handleSuperQuery(params, matchType) {
      //高级查询方法
      if(!params){
        this.superQueryParams=''
        this.superQueryFlag = false
      }else{
        this.superQueryFlag = true
        this.superQueryParams=JSON.stringify(params)
        this.superQueryMatchType = matchType
      }
      this.loadData(1)
    },
    getQueryParams() {
      //获取查询条件
      let sqp = {}
      if(this.superQueryParams){
        sqp['superQueryParams']=encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      var param = Object.assign(sqp, this.queryParam,  this.isorter ,this.filters);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      return filterObj(param);
    },
    getQueryField() {
      //TODO 字段权限控制
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    onClearSelected() {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    searchQuery() {
      this.loadData(1);
      // 点击查询清空列表选中行
      // https://gitee.com/jeecg/jeecg-boot/issues/I4KTU1
      this.selectedRowKeys = []
      this.selectionRows = []
    },
    superQuery() {
      this.$refs.superQueryModal.show();
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1);
    },
    batchDel: function () {
      if(!this.url.deleteBatch){
        this.$message.error("请设置url.deleteBatch属性!")
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return;
      } else {
        var ids = "";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var that = this;
         this.$confirm("确认是否删除所选单据？",{type:"error"}).then(()=>{
         // console.log("确认操作");
          that.loading = true;
            deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
              if (res.success) {
                //重新计算分页问题
                that.reCalculatePage(that.selectedRowKeys.length)
                that.$message.success(res.message);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.loading = false;
            });
        }).catch(()=>{
         // console.log("取消操作");
        })
      }
      }
    },
    handleDelete: function (id) {
      if(!this.url.delete){
        this.$message.error("请设置url.delete属性!")
        return
      }

        for(let i=0;i<this.dataSource.length;i++){
          let curitem=this.dataSource[i];
          if(curitem.id === id){
            if(curitem.hasOwnProperty("status")){
              if(curitem.status === '2'){
                this.$message.error("单据已审批通过，不允许删除！");
                return;
              }
            }
          }
        }

      var that = this;
      deleteAction(that.url.delete, {id: id}).then((res) => {
        if (res.success) {
          //重新计算分页问题
          that.reCalculatePage(1)
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    },
    reCalculatePage(count){
      //总数量-count
      let total=this.ipagination.total-count;
      //获取删除后的分页数
      let currentIndex=Math.ceil(total/this.ipagination.pageSize);
      //删除后的分页数<所在当前页
      if(currentIndex<this.ipagination.current){
        this.ipagination.current=currentIndex;
      }
      console.log('currentIndex',currentIndex)
    },
    handleEdit: function (record) {
      if(record.hasOwnProperty("status")){
        if(record.status !='0' && this.description!='这是菜单管理页面'){
          this.$message.error("单据不是制单，不允许进行编辑");
          return;
        }
    }
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleAdd: function () {
      this.$refs.modalForm.add();
      this.$refs.modalForm.title = "新增";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      console.log(pagination)
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.ipagination = pagination;
      this.loadData();
    },
    handleToggleSearch(){
      this.toggleSearchStatus = !this.toggleSearchStatus;
    },
    // 给popup查询使用(查询区域不支持回填多个字段，限制只返回一个字段)
    getPopupField(fields){
      return fields.split(',')[0]
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      this.loadData();
      //清空列表选中
      this.onClearSelected()
    },
    handleDetail:function(record){
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title="详情";
      this.$refs.modalForm.disableSubmit = true;
    },
    /* 导出 */
    handleExportXls2(){
      let paramsStr = encodeURI(JSON.stringify(this.getQueryParams()));
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?paramsStr=${paramsStr}`;
      window.location.href = url;
    },
    handleExportXls(fileName){
      if(!fileName || typeof fileName != "string"){
        fileName = "导出文件"
      }
      let param = this.getQueryParams();
      if(this.selectedRowKeys && this.selectedRowKeys.length>0){
        param['selections'] = this.selectedRowKeys.join(",")
      }
      console.log("导出参数",param)
      downFile(this.url.exportXlsUrl,param).then((data)=>{
        if (!data) {
          this.$message.warning("文件下载失败")
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
        }else{
          let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName+'.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      })
    },
    /* 导入 */
    handleImportExcel(info){
      this.loading = true;
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
        this.loading = false;
        if (info.file.response.success) {
          // this.$message.success(`${info.file.name} 文件上传成功`);
          if (info.file.response.code === 201) {
            let { message, result: { msg, fileUrl, fileName } } = info.file.response
            let href = window._CONFIG['domianURL'] + fileUrl
            this.$warning({
              title: message,
              content: (<div>
                  <span>{msg}</span><br/>
                  <span>具体详情请 <a href={href} target="_blank" download={fileName}>点击下载</a> </span>
                </div>
              )
            })
          } else {
            this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
          }
          this.loadData()
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.message}.`);
        }
      } else if (info.file.status === 'error') {
        this.loading = false;
        if (info.file.response.status === 500) {
          let data = info.file.response
          const token = Vue.ls.get(ACCESS_TOKEN)
          if (token && data.message.includes("Token失效")) {
            this.$error({
              title: '登录已过期',
              content: '很抱歉，登录已过期，请重新登录',
              okText: '重新登录',
              mask: false,
              onOk: () => {
                store.dispatch('Logout').then(() => {
                  Vue.ls.remove(ACCESS_TOKEN)
                  window.location.reload();
                })
              }
            })
          }
        } else {
          this.$message.error(`文件上传失败: ${info.file.msg} `);
        }
      }
    },
    /* 图片预览 */
    getImgView(text){
      if(text && text.indexOf(",")>0){
        text = text.substring(0,text.indexOf(","))
      }
      return getFileAccessHttpUrl(text)
    },
    /* 文件下载 */
    // update--autor:lvdandan-----date:20200630------for：修改下载文件方法名uploadFile改为downloadFile------
    downloadFile(text){
      if(!text){
        this.$message.warning("未知的文件")
        return;
      }
      if(text.indexOf(",")>0){
        text = text.substring(0,text.indexOf(","))
      }
      let url = getFileAccessHttpUrl(text)
      window.open(url);
    },
  }

}