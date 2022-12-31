package org.br.erp.base.service.impl;

import org.br.erp.base.entity.Scmcustomformatitem;
import org.br.erp.base.mapper.ScmcustomformatitemMapper;
import org.br.erp.base.service.IScmcustomformatitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 自定义格式表分录
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@Service
public class ScmcustomformatitemServiceImpl extends ServiceImpl<ScmcustomformatitemMapper, Scmcustomformatitem> implements IScmcustomformatitemService {
	
	@Autowired
	private ScmcustomformatitemMapper scmcustomformatitemMapper;
	
	@Override
	public List<Scmcustomformatitem> selectByMainId(String mainId) {
		return scmcustomformatitemMapper.selectByMainId(mainId);
	}
}
