<template>
  <a-row :gutter="10">
    <a-col :md="8" :sm="24">
      <a-card :bordered="false">
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入业务对象" />
          <!-- 树-->

          <template>

            <!--业务对象列表-->
            <a-tree showLine :selectedKeys="selectedKeys" :checkStrictly="true" @select="onSelect"
              :dropdownStyle="{maxHeight:'100px',overflow:'auto'}" :treeData="objectTree"
              :expandedKeys="iExpandedKeys"
              defaultExpandAll />
          </template>
        </div>
      </a-card>
    </a-col>
    <a-col :md="16" :sm="24">
      <a-card :bordered="true">
        <a-button class="editable-add-btn" @click="handleAdd" style="margin-bottom: 8px">新增自定义字段</a-button>
        <a-table bordered :data-source="dataSource" :columns="columns">
          <template slot="customcode" slot-scope="text, record">
          <editable-cell :text="text" @change="onCellChange(record.key, 'customcode', $event)" />
         </template>
         <template slot="customname" slot-scope="text, record">
          <editable-cell :text="text" @change="onCellChange(record.key, 'customname', $event)" />
         </template>
          <template slot="operation" slot-scope="text, record">
        <a-popconfirm
          v-if="dataSource.length"
          title="是否删除?"
          @confirm="() => onDelete(record.key)"
        >
          <a href="javascript:;">删除</a>
        </a-popconfirm>
        <a-popconfirm
          v-if="dataSource.length"
          title="是否保存?"
          @confirm="() => onSave(record.key)"
        >
          <a href="javascript:;">保存</a>
        </a-popconfirm>
      </template>
    </a-table>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import { queryMyDepartTreeList, searchByKeywords } from '@/api/api'
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { queryscmobjectlist } from '@/api/erp/customfield'
import { getAction, postAction } from '../../../../api/manage';

const EditableCell = {
  template: `
      <div class="editable-cell">
        <div v-if="editable" class="editable-cell-input-wrapper">
          <a-input :value="value" @change="handleChange" @pressEnter="check" /><a-icon
            type="check"
            class="editable-cell-icon-check"
            @click="check"
          />
        </div>
        <div v-else class="editable-cell-text-wrapper">
          {{ value || ' ' }}
          <a-icon type="edit" class="editable-cell-icon" @click="edit" />
        </div>
      </div>
    `,
  props: {
    text: String,
  },
  data() {
    return {
      value: this.text,
      editable: false,
    };
  },
  methods: {
    handleChange(e) {
      const value = e.target.value;
      this.value = value;
    },
    check() {
      this.editable = false;
      this.$emit('change', this.value);
    },
    edit() {
      this.editable = true;
    },
  },
};

export default {
  name: 'Customfieldlist',
  mixins: [JeecgListMixin],
  components: {
    EditableCell,
  },
  data () {
    return {
      columns:[
        {
          dataIndex:'customcode',
          title:'自定义字段编号',
          align:'center',
          scopedSlots: { customRender: 'customcode' },
        },
        {
          title:"自定义字段名称",
          dataIndex:'customname',
          align:'center',
          scopedSlots:{customRender:'customname'},
        },
        {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: { customRender: 'operation' },
      },
      ],
      currentDeptId: '',
      iExpandedKeys: [],
      loading: false,
      autoExpandParent: true,
      currFlowId: '',
      currFlowName: '',
      disable: true,
      treeData: [],
      visible: false,
      objectTree: [],
      rightClickSelectedKey: '',
      hiding: true,
      model: {},
      dropTrigger: '',
      depart: {},
      disableSubmit: false,
      checkedKeys: [],
      selectedKeys: [],
      dataSource:[],
      autoIncr: 1,
      currSelected: {},
      form: this.$form.createForm(this),
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      graphDatasource: {
        nodes: [],
        edges: []
      },
      userIdentity: "",
      url:{
        queryById:'/base/scmobject/queryById',
      },
    }
  },
  methods: {

    //TODO 删除自定义字段，包括两个地方，
    //一个是在当前表中删除，一个是在后台表中给删除
    onDelete(record){

    },
    //自定义字段保存
    onSave(record){
      //TODO  获取所选择的业务对象字段
      var objectkey=this.selectedKeys[0];

      //TODO 获取界面的新增的自定义字段信息
      const dataSource = [...this.dataSource];
      const target = dataSource.find(item => item.key === record);
      if(target){
        let saveobject={
          id:target.key,
          customcode:target.customcode,
          customname:target.customname,
          scmobjectid:objectkey
        };
        console.log("saveobject:"+JSON.stringify(saveobject));
        postAction("/base/scmcustomfields/add",saveobject).then(res=>{
          if(res.success){
            this.$message.info("保存成功");
            return;
          }
        })
      }

    },
    onCellChange(key,dataIndex,value){
      const dataSource = [...this.dataSource];
      const target = dataSource.find(item => item.key === key);
      if (target) {
        target[dataIndex] = value;
        this.dataSource = dataSource;
      }
    },
    callback (key) {
      //console.log(key)
    },
    //右侧新增字段
    handleAdd(){
      if(this.selectedKeys==undefined || this.selectedKeys == ""){
        this.$message.error("请先选择业务对象");
        return;
      }
      let params={id:this.selectedKeys[0]};
       getAction(this.url.queryById,params).then(res=>{
        if(res.success){
         // console.log("res:"+JSON.stringify(res.result));
          let curobject=res.result;
         // console.log("12332:"+(curobject["objecttabel"] === undefined || curobject["objecttabel"] === ''));
          // if(curobject["objecttabel"] === undefined){
          //   this.$message.error("业务对象没有对应数据库表，请重新选择");
          //   return;
          // }else if(curobject["objecttable"] === ''){
          //   this.$message.error("业务对象没有对应数据库表，请重新选择");
          //   return;
          // }else{
          //   const newdata={
          //     customcode:'',
          //     customname:''
          //   };
          //    this.dataSource.push(newdata);
          // }

          getAction("/base/scmobject/getuuid").then(res=>{
            if(res.success){
              let uuid=res.result;
              const newData={
                key:uuid,
                customcode:'',
                customname:'',
              };
              this.dataSource.push(newData);
            }
          }) 
        }
      })
      
    },
    loadData () {
      this.refresh();
    },
    clearSelectedDepartKeys () {
      this.checkedKeys = [];
      this.selectedKeys = [];
      this.currentDeptId = '';
      this.$refs.DeptUserInfo.currentDeptId = '';
      this.$refs.DeptRoleInfo.currentDeptId = '';
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
    refresh () {
      this.loading = true
      this.loadTree()
    },

    onExpand (expandedKeys) {
      // console.log('onExpand', expandedKeys)
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.iExpandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    onSearch (value) {
      let that = this
      if (value) {
        searchByKeywords({ keyWord: value, myDeptSearch: '1' }).then((res) => {
          if (res.success) {
            that.departTree = []
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i]
              that.departTree.push(temp)
            }
          } else {
            that.$message.warning(res.message)
          }
        })
      } else {
        that.loadTree()
      }

    },
    onCheck (checkedKeys, e) {
      let record = e.node.dataRef;
      // console.log('onCheck', checkedKeys, e);
      this.checkedKeys = [];
      // if (e.checked === true) {
      this.currentDeptId = record.id;
      this.checkedKeys.push(record.id);

      this.$refs.DeptBaseInfo.open(record);
      this.$refs.DeptUserInfo.open(record);
      this.$refs.DeptRoleInfo.open(record);
      // }
      // else {
      //   this.checkedKeys = [];
      //   this.$refs.DeptBaseInfo.clearForm();
      //   this.$refs.DeptUserInfo.clearList();
      // }

      this.hiding = false;
      // this.checkedKeys = checkedKeys.checked
    },
    onSelect (selectedKeys, e) {
      if (this.selectedKeys[0] !== selectedKeys[0]) {
        this.selectedKeys = [selectedKeys[0]];
      }
      let record = e.node.dataRef;
      console.log("record:"+JSON.stringify(record));
      //todo 获取当前选中行的自定义项字段信息
      let searchobject={
        scmobjectid:this.selectedKeys[0]
      };
      
      getAction("/base/scmcustomfields/list",searchobject).then(res=>{
        if(res.success){
          this.dataSource=res.result.records;
        }
      })

      this.checkedKeys.push(record.id);
      this.$refs.DeptBaseInfo.open(record);
      this.$refs.DeptUserInfo.onClearSelected();
      this.$refs.DeptUserInfo.open(record);
      this.$refs.DeptRoleInfo.onClearSelected();
      this.$refs.DeptRoleInfo.open(record);
    },
  },
  created () {
    // this.loadTree()
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less'
.editable-cell {
  position: relative;
  .editable-cell-input-wrapper,
  .editable-cell-text-wrapper {
    padding-right: 24px;
  }

  .editable-cell-text-wrapper {
    padding: 5px 24px 5px 5px;
  }

  .editable-cell-icon,
  .editable-cell-icon-check {
    position: absolute;
    right: 0;
    width: 20px;
    cursor: pointer;
  }

  .editable-cell-icon {
    margin-top: 4px;
    display: none;
  }

  .editable-cell-icon-check {
    line-height: 28px;
  }

  .editable-cell-icon:hover,
  .editable-cell-icon-check:hover {
    color: #108ee9;
  }

  .editable-add-btn {
    margin-bottom: 8px;
  }
}
.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}
</style>