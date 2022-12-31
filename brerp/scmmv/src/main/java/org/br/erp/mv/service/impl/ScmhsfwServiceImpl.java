package org.br.erp.mv.service.impl;

import org.br.erp.mv.entity.Scmhsfw;
import org.br.erp.mv.entity.Scmhsfwitem;
import org.br.erp.mv.mapper.ScmhsfwitemMapper;
import org.br.erp.mv.mapper.ScmhsfwMapper;
import org.br.erp.mv.service.IScmhsfwService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 存货核算范围
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Service
public class ScmhsfwServiceImpl extends ServiceImpl<ScmhsfwMapper, Scmhsfw> implements IScmhsfwService {

	@Autowired
	private ScmhsfwMapper scmhsfwMapper;
	@Autowired
	private ScmhsfwitemMapper scmhsfwitemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmhsfw scmhsfw, List<Scmhsfwitem> scmhsfwitemList) {
		scmhsfwMapper.insert(scmhsfw);
		if(scmhsfwitemList!=null && scmhsfwitemList.size()>0) {
			for(Scmhsfwitem entity:scmhsfwitemList) {
				//外键设置
				entity.setParentid(scmhsfw.getId());
				scmhsfwitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmhsfw scmhsfw,List<Scmhsfwitem> scmhsfwitemList) {
		scmhsfwMapper.updateById(scmhsfw);
		
		//1.先删除子表数据
		scmhsfwitemMapper.deleteByMainId(scmhsfw.getId());
		
		//2.子表数据重新插入
		if(scmhsfwitemList!=null && scmhsfwitemList.size()>0) {
			for(Scmhsfwitem entity:scmhsfwitemList) {
				//外键设置
				entity.setParentid(scmhsfw.getId());
				scmhsfwitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmhsfwitemMapper.deleteByMainId(id);
		scmhsfwMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmhsfwitemMapper.deleteByMainId(id.toString());
			scmhsfwMapper.deleteById(id);
		}
	}
	
}
