package org.br.erp.pr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.pr.entity.Scmpritem;
import org.br.erp.pr.mapper.ScmpritemMapper;
import org.br.erp.pr.service.IScmpritemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 采购计划分录
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Service
public class ScmpritemServiceImpl extends ServiceImpl<ScmpritemMapper, Scmpritem> implements IScmpritemService {
	
	@Autowired
	private ScmpritemMapper scmpritemMapper;
	
	@Override
	public List<Scmpritem> selectByMainId(String mainId) {
		return scmpritemMapper.selectByMainId(mainId);
	}
}
