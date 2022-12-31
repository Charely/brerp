package org.br.erp.po.service.impl;

import org.br.erp.po.entity.Scmlinkqtyitem;
import org.br.erp.po.mapper.ScmlinkqtyitemMapper;
import org.br.erp.po.service.IScmlinkqtyitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 配额协议分录
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
@Service
public class ScmlinkqtyitemServiceImpl extends ServiceImpl<ScmlinkqtyitemMapper, Scmlinkqtyitem> implements IScmlinkqtyitemService {
	
	@Autowired
	private ScmlinkqtyitemMapper scmlinkqtyitemMapper;
	
	@Override
	public List<Scmlinkqtyitem> selectByMainId(String mainId) {
		return scmlinkqtyitemMapper.selectByMainId(mainId);
	}
}
