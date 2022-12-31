<template>

  <a-row>
    <a-card>

      <a-col :md="6" :sm="24">
        <a-card :bordered="false">
          <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
            <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入业务对象" />
            <!-- 树-->

            <template>

              <!--业务对象列表-->
              <a-tree showLine :selectedKeys="selectedKeys" :checkStrictly="true" @select="onSelect"
                :dropdownStyle="{ maxHeight: '100px', overflow: 'auto' }" :treeData="objectTree"
                :expandedKeys="iExpandedKeys" defaultExpandAll />
            </template>
          </div>
        </a-card>
      </a-col>

      <a-col :md="18" :sm="24">
        <a-card class="j-inner-table-wrapper" :bordered="false">

          <!-- 查询区域 begin -->
          <div class="table-page-search-wrapper">
            <a-form layout="inline">
              <a-row :gutter="24">
              </a-row>
            </a-form>
          </div>
          <!-- 查询区域 end -->

          <!-- 操作按钮区域 begin -->
          <div class="table-operator">
            <a-button type="primary" icon="plus" @click="handleAdd">新增</a-button>
            <a-button type="primary" @click="importformatdata">导入系统格式</a-button>
            <a-button type="primary" icon="download" @click="handleExportXls('自定义格式表')">导出</a-button>
            <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
              :action="importExcelUrl" @change="handleImportExcel">
              <a-button type="primary" icon="import">导入</a-button>
            </a-upload>
            <!-- 高级查询区域 -->
            <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery">
            </j-super-query>
            <a-dropdown v-if="selectedRowKeys.length > 0">
              <a-menu slot="overlay">
                <a-menu-item key="1" @click="batchDel">
                  <a-icon type="delete" />
                  <span>删除</span>
                </a-menu-item>
              </a-menu>
              <a-button>
                <span>批量操作</span>
                <a-icon type="down" />
              </a-button>
            </a-dropdown>
          </div>
          <!-- 操作按钮区域 end -->

          <!-- table区域 begin -->
          <div>

            <a-alert type="info" showIcon style="margin-bottom: 16px;">
              <template slot="message">
                <span>已选择</span>
                <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
                <span>项</span>
                <a style="margin-left: 24px" @click="onClearSelected">清空</a>
              </template>
            </a-alert>

            <a-table ref="table" size="middle" bordered rowKey="id" class="j-table-force-nowrap" :scroll="{ x: true }"
              :loading="loading" :columns="columns" :dataSource="dataSource" :pagination="ipagination"
              :expandedRowKeys="expandedRowKeys" :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
              @expand="handleExpand" @change="handleTableChange">

              <!-- 内嵌table区域 begin -->
              <template slot="expandedRowRender" slot-scope="record">
                <a-tabs tabPosition="top">
                  <a-tab-pane tab="自定义格式表分录" key="scmcustomformatitem" forceRender>
                    <scmcustomformatitem-sub-table :record="record" />
                  </a-tab-pane>
                </a-tabs>
              </template>
              <!-- 内嵌table区域 end -->

              <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
              </template>

              <template slot="imgSlot" slot-scope="text,record">
                <div style="font-size: 12px;font-style: italic;">
                  <span v-if="!text">无图片</span>
                  <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;" />
                </div>
              </template>


              <template slot="fileSlot" slot-scope="text">
                <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
                <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
                  <span>下载</span>
                </a-button>
              </template>

              <template slot="action" slot-scope="text, record">
                <a @click="handleEdit(record)">编辑</a>
                <a-divider type="vertical" />
                <a-dropdown>
                  <a class="ant-dropdown-link">更多
                    <a-icon type="down" />
                  </a>
                  <a-menu slot="overlay">
                    <a-menu-item>
                      <a-popconfirm title="确定删除吗?" @confirm="handleDelete(record.id)">
                        <a>删除</a>
                      </a-popconfirm>
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>

              </template>

            </a-table>
          </div>
          <!-- table区域 end -->

          <!-- 表单区域 -->
          <scmcustomformat-modal ref="modalForm" @ok="modalFormOk" :objectid=this.selectedKeys[0] />

        </a-card>
      </a-col>

    </a-card>
  </a-row>

</template>

<script>

import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import ScmcustomformatModal from './modules/ScmcustomformatModal'
import ScmcustomformatitemSubTable from './subTables/ScmcustomformatitemSubTable'
import '@/assets/less/TableExpand.less'
import { queryscmobjectlist } from '@/api/erp/customfield'
import { getAction } from '../../../../api/manage'

export default {
  name: 'ScmcustomformatList',
  mixins: [JeecgListMixin],
  components: {
    ScmcustomformatModal,
    ScmcustomformatitemSubTable,
  },
  data () {
    return {
      description: '自定义格式表列表管理页面',
      // 表头
      columns: [
        {
          title: '#',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: (t, r, index) => parseInt(index) + 1
        },
        {
          title: '系统配置',
          align: 'center',
          dataIndex: 'issys',
        },
        {
          title: '格式编号',
          align: 'center',
          dataIndex: 'formatcode',
        },
        {
          title: '格式名称',
          align: 'center',
          dataIndex: 'formatname',
        },
        {
          title: '业务对象',
          align: 'center',
          dataIndex: 'objectid',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      // 字典选项
      dictOptions: {},
      // 展开的行test
      expandedRowKeys: [],
      iExpandedKeys: [],
      treeData: [],
      objectTree: [],
      selectedKeys: [],
      url: {
        list: '/base/scmcustomformat/list',
        delete: '/base/scmcustomformat/delete',
        deleteBatch: '/base/scmcustomformat/deleteBatch',
        exportXlsUrl: '/base/scmcustomformat/exportXls',
        importExcelUrl: '/base/scmcustomformat/importExcel',
      },
      superFieldList: [],
    }
  },
  created () {
    this.getSuperFieldList();
    this.loadTree();
  },
  computed: {
    importExcelUrl () {
      return window._CONFIG['domianURL'] + this.url.importExcelUrl
    }
  },
  methods: {
    /**
     * 导入系统格式
     */
    importformatdata(){
      if(this.selectedKeys[0]===null || this.selectedKeys.length === 0){
        this.$message.error("请选择业务对象");
        return;
      }
      let importUrl="/base/scmcustomformat/importsysformat";
      let param={
        objectid:this.selectedKeys[0]
      };
      getAction(importUrl,param).then(res=>{
        if(res.success){
          this.$message.success("系统格式导入成功");
          this.loadData();
        }
      })
    },
    loadTree () {
      var that = this
      that.treeData = []
      that.objectTree = [];
      console.log("loadtree");
      queryscmobjectlist().then((res) => {
        if (res.success && res.result) {
          for (let i = 0; i < res.result.length; i++) {
            let temp = res.result[i]
            that.treeData.push(temp)
            that.objectTree.push(temp)
            that.setThisExpandedKeys(temp)
          }
          this.loading = false;
        }
      });
    },
    setThisExpandedKeys (node) {
      //只展开一级目录
      if (node.children && node.children.length > 0) {
        this.iExpandedKeys.push(node.key)
        //下方代码放开注释则默认展开所有节点

        for (let a = 0; a < node.children.length; a++) {
          this.setThisExpandedKeys(node.children[a])
        }

      }
    },
    onSelect (selectedKeys, e) {
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]];
      }
      let record = e.node.dataRef;
      console.log("record:" + JSON.stringify(record));
      //todo 获取当前选中行的自定义项字段信息
      let searchobject = {
        scmobjectid: this.selectedKeys[0]
      };
    },
    initDictConfig () {
    },

    handleExpand (expanded, record) {
      this.expandedRowKeys = []
      if (expanded === true) {
        this.expandedRowKeys.push(record.id)
      }
    },
    getSuperFieldList () {
      let fieldList = [];
      fieldList.push({ type: 'string', value: 'issys', text: '系统配置', dictCode: '' })
      fieldList.push({ type: 'string', value: 'formatcode', text: '格式编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'formatname', text: '格式名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'objectid', text: '业务对象', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
</style>