package org.br.erp.so.service.impl;

import org.br.erp.so.entity.Scmsalesinvoiceitem;
import org.br.erp.so.mapper.ScmsalesinvoiceitemMapper;
import org.br.erp.so.service.IScmsalesinvoiceitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@Service
public class ScmsalesinvoiceitemServiceImpl extends ServiceImpl<ScmsalesinvoiceitemMapper, Scmsalesinvoiceitem> implements IScmsalesinvoiceitemService {
	
	@Autowired
	private ScmsalesinvoiceitemMapper scmsalesinvoiceitemMapper;
	
	@Override
	public List<Scmsalesinvoiceitem> selectByMainId(String mainId) {
		return scmsalesinvoiceitemMapper.selectByMainId(mainId);
	}
}
