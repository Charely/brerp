package org.br.erp.po.service.impl;

import org.br.erp.po.entity.Scmlinkqty;
import org.br.erp.po.entity.Scmlinkqtyitem;
import org.br.erp.po.mapper.ScmlinkqtyitemMapper;
import org.br.erp.po.mapper.ScmlinkqtyMapper;
import org.br.erp.po.service.IScmlinkqtyService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 配额定义
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
@Service
public class ScmlinkqtyServiceImpl extends ServiceImpl<ScmlinkqtyMapper, Scmlinkqty> implements IScmlinkqtyService {

	@Autowired
	private ScmlinkqtyMapper scmlinkqtyMapper;
	@Autowired
	private ScmlinkqtyitemMapper scmlinkqtyitemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmlinkqty scmlinkqty, List<Scmlinkqtyitem> scmlinkqtyitemList) {
		scmlinkqtyMapper.insert(scmlinkqty);
		if(scmlinkqtyitemList!=null && scmlinkqtyitemList.size()>0) {
			for(Scmlinkqtyitem entity:scmlinkqtyitemList) {
				//外键设置
				entity.setParentid(scmlinkqty.getId());
				scmlinkqtyitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmlinkqty scmlinkqty,List<Scmlinkqtyitem> scmlinkqtyitemList) {
		scmlinkqtyMapper.updateById(scmlinkqty);
		
		//1.先删除子表数据
		scmlinkqtyitemMapper.deleteByMainId(scmlinkqty.getId());
		
		//2.子表数据重新插入
		if(scmlinkqtyitemList!=null && scmlinkqtyitemList.size()>0) {
			for(Scmlinkqtyitem entity:scmlinkqtyitemList) {
				//外键设置
				entity.setParentid(scmlinkqty.getId());
				scmlinkqtyitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmlinkqtyitemMapper.deleteByMainId(id);
		scmlinkqtyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmlinkqtyitemMapper.deleteByMainId(id.toString());
			scmlinkqtyMapper.deleteById(id);
		}
	}
	
}
