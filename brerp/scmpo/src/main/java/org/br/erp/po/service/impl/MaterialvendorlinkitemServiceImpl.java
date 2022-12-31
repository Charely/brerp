package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.po.entity.Materialvendorlinkitem;
import org.br.erp.po.mapper.MaterialvendorlinkitemMapper;
import org.br.erp.po.service.IMaterialvendorlinkitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 货源清单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
@Service
public class MaterialvendorlinkitemServiceImpl extends ServiceImpl<MaterialvendorlinkitemMapper, Materialvendorlinkitem> implements IMaterialvendorlinkitemService {
	
	@Autowired
	private MaterialvendorlinkitemMapper materialvendorlinkitemMapper;
	
	@Override
	public List<Materialvendorlinkitem> selectByMainId(String mainId) {
		return materialvendorlinkitemMapper.selectByMainId(mainId);
	}
}
