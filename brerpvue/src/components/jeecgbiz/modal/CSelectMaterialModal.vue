<template>
  <j-modal :width="modalWidth" :height="500"   :visible="visible" :title="title" switchFullscreen
    wrapClassName="c-material-select-modal" @ok="handleSubmit" @cancel="close" style="top:10px" cancelText="关闭">
    <a-row :gutter="10" style="background-color: #ececec; padding: 1px; margin: -1px">
      <a-col :md="24" :sm="24">
        <a-card :bordered="false">
          <a-form-model>
            <a-form-model-item label="物料编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-row type="flex" :gutter="8">
                <a-col :span="18">
                  <a-input-search :style="{ width: '100%' }" placeholder="请输入物料编号" v-model="queryParam.materialname"
                    @search="onSearch"></a-input-search>
                </a-col>
                <a-col :span="6">
                  <a-button @click="searchReset(1)" icon="redo">重置</a-button>
                </a-col>
              </a-row>
            </a-form-model-item>
          </a-form-model>
          <!--用户列表-->
          <a-table ref="table" :scroll="{x:600,y:500}" size="middle" rowKey="id" :columns="columns"
            :dataSource="dataSource" :pagination="ipagination"
            :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: getType }"
            :loading="loading" @change="handleTableChange">
          </a-table>
        </a-card>
      </a-col>
    </a-row>
  </j-modal>
</template>

<script>
import { pushIfNotExist, filterObj } from '@/utils/util'
import { queryDepartTreeList, getUserList, queryUserByDepId, queryDepartTreeSync } from '@/api/api'
import { getAction } from '@/api/manage'

export default {
  name: 'CSelectMaterialModal',
  components: {},
  props: ['modalWidth', 
          'multi', 
          'materialid', 
          'store', 
          'text',
          'materialtype'],
  data () {
    return {
      queryParam: {
        materialname: "",
      },
      columns: [
        { title: '物料编号', dataIndex: 'materialcode', align: 'center', width: '30%', widthRight: '70%' },
        { title: '物料名称', dataIndex: 'materialname', align: 'center', width: '30%' },
        { title: '物料型号', dataIndex: 'model', align: 'center', width: '20%' },
        { title: '物料规格', dataIndex: 'mspecs', align: 'center', width: '20%' }
      ],
      scrollTrigger: {},
      dataSource: [],
      selectionRows: [],
      selectedRowKeys: [],
      selectUserRows: [],
      selectMaterialIds: [],
      title: '物料选择帮助',
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
    materialid: {
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
      if (this.materialid) {
        // 这里最后加一个 , 的原因是因为无论如何都要使用 in 查询，防止后台进行了模糊匹配，导致查询结果不准确
        let values = this.materialid.split(',');
        let param = { [this.store]: values[0] }
        getAction('/base/material/queryById', param).then((list) => {
          this.selectionRows = []
          let selectedRowKeys = []
          let textArray = []
          // if (list && list.length > 0) {
          //   for (let material of list) {
          //     textArray.push(user[this.text])
          //     selectedRowKeys.push(material['id'])
          //     this.selectionRows.push(material)
          //   }
          // }
          if(list.success){
            let material=list.result;
            textArray.push(material[this.text]);
            selectedRowKeys.push(material["id"]);
            this.selectionRows.push(material);
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
    async loadData (arg) {
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      let params = this.getQueryParams()//查询条件
      this.loading = true
      getAction('/base/material/list/'+this.materialtype, params).then(res => {
        if (res.success) {
          this.dataSource = res.result.records
          this.ipagination.total = res.result.total
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
        this.scrollTrigger = {};
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
      that.selectMaterialIds = [];
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
      for (let row of this.selectionRows) {
        if (this.selectedRowKeys.includes(row.id)) {
          this.selectUserRows.push(row)
        }
      }
      this.selectMaterialIds = this.selectUserRows.map(row => row.materialname).join(',')
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
