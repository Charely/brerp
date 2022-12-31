package org.br.erp.outsource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.outsource.entity.Scmoutsourceitem;
import org.br.erp.outsource.entity.Scmoutsourceitembom;
import org.br.erp.outsource.mapper.ScmoutsourcebomMapper;
import org.br.erp.outsource.mapper.ScmoutsourceitemMapper;
import org.br.erp.outsource.service.IScmoutsourceitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 委外订单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Service
public class ScmoutsourceitemServiceImpl
		extends ServiceImpl<ScmoutsourceitemMapper, Scmoutsourceitem> implements IScmoutsourceitemService {
	
	@Autowired
	private ScmoutsourceitemMapper scmoutsourceitemMapper;

	@Autowired
	private ScmoutsourcebomMapper scmoutsourcebomMapper;
	
	@Override
	public List<Scmoutsourceitem> selectByMainId(String mainId) {
		return scmoutsourceitemMapper.selectByMainId(mainId);
	}

	@Override
	public List<Scmoutsourceitembom> selectBomInfoByMainId(String itemid) {
		QueryWrapper<Scmoutsourceitembom> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("parentid",itemid);
		List<Scmoutsourceitembom> scmoutsourceitemboms = scmoutsourcebomMapper.selectList(queryWrapper);
		return scmoutsourceitemboms;
	}
}
