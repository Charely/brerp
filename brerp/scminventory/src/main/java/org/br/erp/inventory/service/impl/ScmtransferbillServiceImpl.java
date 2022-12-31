package org.br.erp.inventory.service.impl;

import org.br.erp.inventory.entity.Scmtransferbill;
import org.br.erp.inventory.entity.Scmtransferbillitem;
import org.br.erp.inventory.mapper.ScmtransferbillitemMapper;
import org.br.erp.inventory.mapper.ScmtransferbillMapper;
import org.br.erp.inventory.service.IScmtransferbillService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@Service
public class ScmtransferbillServiceImpl extends ServiceImpl<ScmtransferbillMapper, Scmtransferbill> implements IScmtransferbillService {

	@Autowired
	private ScmtransferbillMapper scmtransferbillMapper;
	@Autowired
	private ScmtransferbillitemMapper scmtransferbillitemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmtransferbill scmtransferbill, List<Scmtransferbillitem> scmtransferbillitemList) {
		scmtransferbillMapper.insert(scmtransferbill);
		if(scmtransferbillitemList!=null && scmtransferbillitemList.size()>0) {
			for(Scmtransferbillitem entity:scmtransferbillitemList) {
				//外键设置
				entity.setParentid(scmtransferbill.getId());
				scmtransferbillitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmtransferbill scmtransferbill,List<Scmtransferbillitem> scmtransferbillitemList) {
		scmtransferbillMapper.updateById(scmtransferbill);
		
		//1.先删除子表数据
		scmtransferbillitemMapper.deleteByMainId(scmtransferbill.getId());
		
		//2.子表数据重新插入
		if(scmtransferbillitemList!=null && scmtransferbillitemList.size()>0) {
			for(Scmtransferbillitem entity:scmtransferbillitemList) {
				//外键设置
				entity.setParentid(scmtransferbill.getId());
				scmtransferbillitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmtransferbillitemMapper.deleteByMainId(id);
		scmtransferbillMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmtransferbillitemMapper.deleteByMainId(id.toString());
			scmtransferbillMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updatePoStatus(List<String> ids, String statusflag) {
		ids.forEach(item->{
			Scmtransferbill scmtransferbill = scmtransferbillMapper.selectById(item);
			scmtransferbill.setStatus(statusflag);
			scmtransferbillMapper.updateById(scmtransferbill);
		});
	}

}
