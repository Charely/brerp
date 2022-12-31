package org.br.erp.inventory.service.impl;

import org.br.erp.inventory.entity.Scminventorycheckitem;
import org.br.erp.inventory.mapper.ScminventorycheckitemMapper;
import org.br.erp.inventory.service.IScminventorycheckitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 盘点单分录
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
public class ScminventorycheckitemServiceImpl extends ServiceImpl<ScminventorycheckitemMapper, Scminventorycheckitem> implements IScminventorycheckitemService {
	
	@Autowired
	private ScminventorycheckitemMapper scminventorycheckitemMapper;
	
	@Override
	public List<Scminventorycheckitem> selectByMainId(String mainId) {
		return scminventorycheckitemMapper.selectByMainId(mainId);
	}
}
