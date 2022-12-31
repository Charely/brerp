package org.br.erp.base.service.impl;

import org.br.erp.base.entity.Scmbillofmaterialitem;
import org.br.erp.base.mapper.ScmbillofmaterialitemMapper;
import org.br.erp.base.service.IScmbillofmaterialitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物料清单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
@Service
public class ScmbillofmaterialitemServiceImpl extends ServiceImpl<ScmbillofmaterialitemMapper, Scmbillofmaterialitem> implements IScmbillofmaterialitemService {
	
	@Autowired
	private ScmbillofmaterialitemMapper scmbillofmaterialitemMapper;
	
	@Override
	public List<Scmbillofmaterialitem> selectByMainId(String mainId) {
		return scmbillofmaterialitemMapper.selectByMainId(mainId);
	}
}
