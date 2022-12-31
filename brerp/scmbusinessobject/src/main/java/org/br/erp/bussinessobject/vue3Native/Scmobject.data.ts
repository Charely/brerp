import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
//列表数据
export const columns: BasicColumn[] = [
  {
    title: '业务对象编号',
    align: 'left',
    dataIndex: 'objectcode'
  },
  {
    title: '业务对象名称',
    align: 'center',
    dataIndex: 'objectname'
  },
  {
    title: '对应数据库表',
    align: 'center',
    dataIndex: 'objecttable'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '业务对象编号',
    field: 'objectcode',
    component: 'Input',
  },
  {
    label: '业务对象名称',
    field: 'objectname',
    component: 'Input',
  },
  {
    label: '对应数据库表',
    field: 'objecttable',
    component: 'Input',
  },
  {
    label: '父级节点',
    field: 'pid',
    component: 'JTreeSelect',
    componentProps: {
      dict: "scmobject,objectcode,id",
      pidField: "pid",
      pidValue: "0",
      hasChildField: "has_child",
    },
  },
	// TODO 主键隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false,
  },
];
