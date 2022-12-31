package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.br.erp.base.entity.Materialtype;
import org.br.erp.base.mapper.MaterialtypeMapper;
import org.br.erp.base.service.IMaterialtypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 物料类型
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Service
public class MaterialtypeServiceImpl extends ServiceImpl<MaterialtypeMapper, Materialtype> implements IMaterialtypeService {

	@Override
	public void addMaterialtype(Materialtype materialtype) {
	   //新增时设置hasChild为0
	    materialtype.setHasChild(IMaterialtypeService.NOCHILD);
		if(oConvertUtils.isEmpty(materialtype.getPid())){
			materialtype.setPid(IMaterialtypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			Materialtype parent = baseMapper.selectById(materialtype.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(materialtype);
	}
	
	@Override
	public void updateMaterialtype(Materialtype materialtype) {
		Materialtype entity = this.getById(materialtype.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = materialtype.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				materialtype.setPid(IMaterialtypeService.ROOT_PID_VALUE);
			}
			if(!IMaterialtypeService.ROOT_PID_VALUE.equals(materialtype.getPid())) {
				baseMapper.updateTreeNodeStatus(materialtype.getPid(), IMaterialtypeService.HASCHILD);
			}
		}
		baseMapper.updateById(materialtype);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMaterialtype(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    Materialtype materialtype = this.getById(idVal);
                    String pidVal = materialtype.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<Materialtype> dataList = baseMapper.selectList(new QueryWrapper<Materialtype>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    boolean flag = (dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal) && !sb.toString().contains(pidVal);
                    if(flag){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            Materialtype materialtype = this.getById(id);
            if(materialtype==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(materialtype.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<Materialtype> queryTreeListNoPage(QueryWrapper<Materialtype> queryWrapper) {
        List<Materialtype> dataList = baseMapper.selectList(queryWrapper);
        List<Materialtype> mapList = new ArrayList<>();
        for(Materialtype data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !IMaterialtypeService.NOCHILD.equals(pidVal)){
                Materialtype rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    @Override
    public List<SelectTreeModel> queryListByCode(String parentCode) {
        String pid = ROOT_PID_VALUE;
        List<SelectTreeModel> res=new ArrayList<>();
        if (oConvertUtils.isNotEmpty(parentCode)) {
            LambdaQueryWrapper<Materialtype> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Materialtype::getPid, parentCode);
            List<Materialtype> list = baseMapper.selectList(queryWrapper);
            if (list == null || list.size() == 0) {
                //throw new JeecgBootException("该编码【" + parentCode + "】不存在，请核实!");
                return null;
            }
//            if (list.size() > 1) {
//                throw new JeecgBootException("该编码【" + parentCode + "】存在多个，请核实!");
//            }
            for (Materialtype materialtype : list) {
                List<SelectTreeModel> selectTreeModels = queryListByPid(materialtype.getPid());
                if(res.size()==0) {
                    res.addAll(selectTreeModels);
                }else{
                    for (SelectTreeModel selectTreeModel : selectTreeModels) {
                        if(!res.contains(selectTreeModel)){
                            res.add(selectTreeModel);
                        }
                    }
                }
            }
//            pid = list.get(0).getId();
        }
        return res;
    }

    @Override
    public List<SelectTreeModel> queryListByPid(String pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IMaterialtypeService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<Materialtype>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IMaterialtypeService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private Materialtype getTreeRoot(String pidVal){
        Materialtype data =  baseMapper.selectById(pidVal);
        if(data != null && !IMaterialtypeService.ROOT_PID_VALUE.equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<Materialtype> dataList = baseMapper.selectList(new QueryWrapper<Materialtype>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(Materialtype tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
