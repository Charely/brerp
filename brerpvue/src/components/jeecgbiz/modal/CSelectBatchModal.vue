<template>
  <j-modal :width="1200" :height="100" :visible="visible" :title="title" wrapClassName="c-batch-select-modal"
    @ok="handleSubmit" @cancel="close" style="top:10px" cancelText="关闭">
    <a-row :gutter="10" style="background-color: #ececec; padding: 1px; margin: -1px">
      <a-col :md="24" :sm="24">
        <a-form-model>
          <a-form-model-item label="批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-row type="flex" :gutter="8">
              <a-col :span="18">
                <a-input-search :style="{ width: '100%' }" placeholder="请输入批号" v-model="queryParam.batchcode"
                  @search="onSearch"></a-input-search>
              </a-col>
              <a-col :span="6">
                <a-button @click="searchReset(1)" icon="redo">重置</a-button>
              </a-col>
            </a-row>
          </a-form-model-item>
        </a-form-model>
        <!--批号列表-->
        <a-table ref="table" :scroll="{ x: 600, y: 500 }" size="middle" rowKey="id" :columns="columns"
          :dataSource="dataSource" :pagination="ipagination"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: getType }"
          :loading="loading" @change="handleTableChange">
        </a-table>
      </a-col>
    </a-row>
  </j-modal>
</template>

<script>
import { pushIfNotExist, filterObj } from '@/utils/util'
import { queryDepartTreeList, getUserList, queryUserByDepId, queryDepartTreeSync } from '@/api/api'
import { getAction } from '@/api/manage'
import { setStore, getStore, clearStore } from "@/utils/storage"
import { locat } from 'xe-utils/methods'

export default {
  name: 'SelectBatchModal',
  components: {},
  props: ['modalWidth',
    'multi',
    'companyid',
    'materialid',
    'batchid',
    'text',],
  data () {
    return {
      queryParam: {
        batchcode: "",
      },
      columns: [
        { title: '批次编号', dataIndex: 'batchcode', align: 'center', width: '100px', widthRight: '70%' },
        // { title: '公司', dataIndex: 'companyid', align: 'center', width: '100px' },
        // { title: '仓库编号', dataIndex: 'warehouseid', align: 'center', width: '100px' },
        { title: '供应商原始批号', dataIndex: 'orgbatchcode', align: 'center', width: '70px' },
        { title: '批号数量', dataIndex: 'batchqty', align: 'center', width: '70px' },
        { title: '批号生效日期', dataIndex: 'fromdate', align: 'center', width: '70px' },
        { title: '批号失效日期', dataIndex: 'enddate', align: 'center', width: '70px' }
      ],
      scrollTrigger: {},
      dataSource: [],
      selectionRows: [],
      selectedRowKeys: [],
      selectUserRows: [],
      selectBatchIds: [],
      title: '批次选择帮助',
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      isorter: {
        column: 'createTime',
        order: 'desc'
      },
      selectedDepIds: [],
      departTree: [],
      visible: false,
      form: this.$form.createForm(this),
      loading: false,
      expandedKeys: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 10 },
      },
    }
  },
  computed: {
    // 计算属性的 getter
    getType: function () {
      return this.multi == true ? 'checkbox' : 'radio';
    }
  },
  watch: {
    batchid: {
      immediate: true,
      handler () {
        this.initMaterialName()
      }
    },
  },
  created () {
    // 该方法触发屏幕自适应
    this.resetScreenSize();
    this.loadData()
  },
  methods: {
    initMaterialName () {
      if (this.batchid) {
        // 这里最后加一个 , 的原因是因为无论如何都要使用 in 查询，防止后台进行了模糊匹配，导致查询结果不准确
        let values = this.batchid.split(',');
        let param = { id: values[0] }
        getAction('/base/scmbatch/queryById', param).then((list) => {
          this.selectionRows = []
          let selectedRowKeys = []
          let textArray = []
          if (list.success) {
            let scmbatch = list.result;
            textArray.push(scmbatch[this.text]);
            selectedRowKeys.push(scmbatch["id"]);
            this.selectionRows.push(scmbatch);
          }
          this.selectedRowKeys = selectedRowKeys
          this.$emit('initComp', textArray.join(','))
        })

      } else {
        // JSelectUserByDep组件bug issues/I16634
        this.$emit('initComp', '')
        // 前端用户选择单选无法置空的问题 #2610
        this.selectedRowKeys = []
      }
    },
    loadData (arg) {
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      let params = this.getQueryParams()//查询条件
      this.loading = true
      let currentuser = this.$store.getters.userInfo
      let outstock_companyid = localStorage.getItem(currentuser.id+"_outstock_companyid");
      let outstock_warehouseid = localStorage.getItem(currentuser.id+"_outstock_warehouseid");
      let outstock_stocklocationid = localStorage.getItem(currentuser.id+"_outstock_stocklocationid");
      if (outstock_companyid === 'undefined') {
        return;
      }
      if (outstock_warehouseid === 'undefined') {
        return;
      }
      params.companyid = outstock_companyid
      params.warehouseid = outstock_warehouseid
      if(outstock_stocklocationid != '' && outstock_stocklocationid !=null && outstock_stocklocationid!=undefined){
        params.stocklocationid=outstock_stocklocationid;
      }
      params.materialid = this.materialid

      getAction('/inventory.base/scmimbalance/batchlist', params).then(res => {
        if (res.success) {
          this.dataSource = res.result.records || res.result
          // this.ipagination.total = res.result.total
        }
      }).finally(() => {
        this.loading = false
      })
    },
    // 触发屏幕自适应
    resetScreenSize () {
      let screenWidth = document.body.clientWidth;
      if (screenWidth < 500) {
        this.scrollTrigger = { x: 800 };
      } else {
        this.scrollTrigger = { x: 900 };
      }
    },
    showModal () {
      this.visible = true;
      this.queryDepartTree();
      // this.initUserNames()
      this.initMaterialName();
      this.loadData();
      this.form.resetFields();
    },
    getQueryParams () {
      let param = Object.assign({}, this.queryParam, this.isorter);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      param.departId = this.selectedDepIds.join(',')
      return filterObj(param);
    },
    getQueryField () {
      let str = 'id,';
      for (let a = 0; a < this.columns.length; a++) {
        str += ',' + this.columns[a].dataIndex;
      }
      return str;
    },
    searchReset (num) {
      let that = this;
      that.selectedRowKeys = [];
      that.selectBatchIds = [];
      that.selectedDepIds = [];
      if (num !== 0) {
        that.queryParam = {};
        that.loadData(1);
      }
    },
    close () {
      this.searchReset(0);
      this.visible = false;
    },
    handleTableChange (pagination, filters, sorter) {
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = 'ascend' === sorter.order ? 'asc' : 'desc';
      }
      this.ipagination = pagination;
      this.loadData();
    },
    handleSubmit () {
      let that = this;
      this.getSelectUserRows();
      that.$emit('ok', that.selectUserRows);
      that.searchReset(0)
      that.close();
    },
    //获取选择用户信息
    getSelectUserRows () {
      this.selectUserRows = []
      // for (let row of this.selectionRows) {
      //   if (this.selectedRowKeys.includes(row.id)) {
      //     this.selectUserRows.push(row)
      //   }
      // }

      for(let row of this.dataSource){
        if(this.selectedRowKeys.includes(row.id)){
          this.selectUserRows.push(row);
        }
      }
      this.selectBatchIds = this.selectUserRows.map(row => row.batchcode).join(',')
    },
    // 点击树节点,筛选出对应的用户
    onDepSelect (selectedDepIds) {
      if (selectedDepIds[0] != null) {
        if (this.selectedDepIds[0] !== selectedDepIds[0]) {
          this.selectedDepIds = [selectedDepIds[0]];
        }
        this.loadData(1);
      }
    },
    onSelectChange (selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      selectionRows.forEach(row => pushIfNotExist(this.selectionRows, row, 'id'))
    },
    onSearch () {
      this.loadData(1);
    },
    // 根据选择的id来查询用户信息
    initQueryUserByDepId (selectedDepIds) {
      this.loading = true
      return queryUserByDepId({ id: selectedDepIds.toString() }).then((res) => {
        if (res.success) {
          this.dataSource = res.result;
          this.ipagination.total = res.result.length;
        }
      }).finally(() => {
        this.loading = false
      })
    },
    queryDepartTree () {
      //update-begin-author:taoyan date:20211202 for: 异步加载部门树 https://github.com/jeecgboot/jeecg-boot/issues/3196
      this.expandedKeys = []
      this.departTree = []
      queryDepartTreeSync().then((res) => {
        if (res.success) {
          for (let i = 0; i < res.result.length; i++) {
            let temp = res.result[i]
            this.departTree.push(temp)
          }
        }
      })
    },
    onLoadDepartment (treeNode) {
      return new Promise(resolve => {
        queryDepartTreeSync({ pid: treeNode.dataRef.id }).then((res) => {
          if (res.success) {
            //判断chidlren是否为空，并修改isLeaf属性值
            if (res.result.length == 0) {
              treeNode.dataRef['isLeaf'] = true
              return;
            } else {
              treeNode.dataRef['children'] = res.result;
            }
          }
        })
        resolve();
      });
    },
    //update-end-author:taoyan date:20211202 for: 异步加载部门树 https://github.com/jeecgboot/jeecg-boot/issues/3196
    modalFormOk () {
      this.loadData();
    }
  }
}
</script>

<style scoped>
.ant-table-tbody .ant-table-row td {
  padding-top: 10px;
  padding-bottom: 10px;
}

.ant-modal-content {
  position: relative;
  height: 500px;
  background-color: #fff;
  background-clip: padding-box;
  border: 0;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgb(0 0 0 / 15%);
  pointer-events: auto;
}

.ant-modal-body {
  padding: 0px;
  font-size: 11px;
  line-height: 1.5;
  word-wrap: break-word;
}

#components-layout-demo-custom-trigger .trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color .3s;
}
</style>
