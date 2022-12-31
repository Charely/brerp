package org.br.erp.im.service.impl;

import org.br.erp.im.entity.Scmiminvoucheritem;
import org.br.erp.im.mapper.ScmiminvoucheritemMapper;
import org.br.erp.im.service.IScmiminvoucheritemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 入库凭证分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Service
public class ScmiminvoucheritemServiceImpl extends ServiceImpl<ScmiminvoucheritemMapper, Scmiminvoucheritem> implements IScmiminvoucheritemService {
	
	@Autowired
	private ScmiminvoucheritemMapper scmiminvoucheritemMapper;
	
	@Override
	public List<Scmiminvoucheritem> selectByMainId(String mainId) {
		return scmiminvoucheritemMapper.selectByMainId(mainId);
	}
}
