package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmbillofmaterial;
import org.br.erp.base.entity.Scmbillofmaterialitem;
import org.br.erp.base.mapper.ScmbillofmaterialitemMapper;
import org.br.erp.base.mapper.ScmbillofmaterialMapper;
import org.br.erp.base.service.IScmbillofmaterialService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 物料清单
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Service
public class ScmbillofmaterialServiceImpl extends ServiceImpl<ScmbillofmaterialMapper, Scmbillofmaterial> implements IScmbillofmaterialService {

	@Autowired
	private ScmbillofmaterialMapper scmbillofmaterialMapper;
	@Autowired
	private ScmbillofmaterialitemMapper scmbillofmaterialitemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmbillofmaterial scmbillofmaterial, List<Scmbillofmaterialitem> scmbillofmaterialitemList) {
		scmbillofmaterialMapper.insert(scmbillofmaterial);
		if(scmbillofmaterialitemList!=null && scmbillofmaterialitemList.size()>0) {
			for(Scmbillofmaterialitem entity:scmbillofmaterialitemList) {
				//外键设置
				entity.setParentid(scmbillofmaterial.getId());
				scmbillofmaterialitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmbillofmaterial scmbillofmaterial,List<Scmbillofmaterialitem> scmbillofmaterialitemList) {
		scmbillofmaterialMapper.updateById(scmbillofmaterial);
		
		//1.先删除子表数据
		scmbillofmaterialitemMapper.deleteByMainId(scmbillofmaterial.getId());
		
		//2.子表数据重新插入
		if(scmbillofmaterialitemList!=null && scmbillofmaterialitemList.size()>0) {
			for(Scmbillofmaterialitem entity:scmbillofmaterialitemList) {
				//外键设置
				entity.setParentid(scmbillofmaterial.getId());
				scmbillofmaterialitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmbillofmaterialitemMapper.deleteByMainId(id);
		scmbillofmaterialMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmbillofmaterialitemMapper.deleteByMainId(id.toString());
			scmbillofmaterialMapper.deleteById(id);
		}
	}

	@Override
	public List<Scmbillofmaterialitem> getBomInfoByItemid(String partentMaterialid) {
		QueryWrapper<Scmbillofmaterial> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("materialid",partentMaterialid);
		List<Scmbillofmaterial> scmbillofmaterials = scmbillofmaterialMapper.selectList(queryWrapper);
		if(scmbillofmaterials!=null && scmbillofmaterials.size()>0){
			Scmbillofmaterial scmbillofmaterial = scmbillofmaterials.get(0);
			List<Scmbillofmaterialitem> scmbillofmaterialitems = scmbillofmaterialitemMapper.selectByMainId(scmbillofmaterial.getId());
			return scmbillofmaterialitems;
		}else{
			throw new RuntimeException("不存在物料清单，请重新配置！");
		}
	}

}
