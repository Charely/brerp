package org.br.erp.inventory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.mapper.ScminventoyitemMapper;
import org.br.erp.inventory.service.IScminventoyitemService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description: 库存单据分录
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Service
public class ScminventoyitemServiceImpl extends ServiceImpl<ScminventoyitemMapper, Scminventoryitem>
		implements IScminventoyitemService {
	
	@Autowired
	private ScminventoyitemMapper scminventoyitemMapper;
	
	@Override
	public List<Scminventoryitem> selectByMainId(String mainId) {
		return scminventoyitemMapper.selectByMainId(mainId);
	}
}
