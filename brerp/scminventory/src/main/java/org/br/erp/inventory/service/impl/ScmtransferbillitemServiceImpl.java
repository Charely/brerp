package org.br.erp.inventory.service.impl;

import org.br.erp.inventory.entity.Scmtransferbillitem;
import org.br.erp.inventory.mapper.ScmtransferbillitemMapper;
import org.br.erp.inventory.service.IScmtransferbillitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 移库单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@Service
public class ScmtransferbillitemServiceImpl extends ServiceImpl<ScmtransferbillitemMapper, Scmtransferbillitem> implements IScmtransferbillitemService {
	
	@Autowired
	private ScmtransferbillitemMapper scmtransferbillitemMapper;
	
	@Override
	public List<Scmtransferbillitem> selectByMainId(String mainId) {
		return scmtransferbillitemMapper.selectByMainId(mainId);
	}
}
