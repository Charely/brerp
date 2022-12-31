package org.br.erp.mv.service.impl;

import org.br.erp.mv.entity.Scmhsfwitem;
import org.br.erp.mv.mapper.ScmhsfwitemMapper;
import org.br.erp.mv.service.IScmhsfwitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 核算范围分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Service
public class ScmhsfwitemServiceImpl extends ServiceImpl<ScmhsfwitemMapper, Scmhsfwitem> implements IScmhsfwitemService {
	
	@Autowired
	private ScmhsfwitemMapper scmhsfwitemMapper;
	
	@Override
	public List<Scmhsfwitem> selectByMainId(String mainId) {
		return scmhsfwitemMapper.selectByMainId(mainId);
	}
}
