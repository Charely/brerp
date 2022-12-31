package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.po.entity.Scmpurinvoiceitem;
import org.br.erp.po.mapper.ScmpurinvoiceitemMapper;
import org.br.erp.po.service.IScmpurinvoiceitemService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@Service
public class ScmpurinvoiceitemServiceImpl extends ServiceImpl<ScmpurinvoiceitemMapper, Scmpurinvoiceitem> implements IScmpurinvoiceitemService {
	
	@Autowired
	private ScmpurinvoiceitemMapper scmpurinvoiceitemMapper;
	
	@Override
	public List<Scmpurinvoiceitem> selectByMainId(String mainId) {
		return scmpurinvoiceitemMapper.selectByMainId(mainId);
	}

	@Override
	public List<Scmpurinvoiceitem> selectByFromItemId(String itemid) {
		QueryWrapper<Scmpurinvoiceitem> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("fromitemid",itemid);
		List<Scmpurinvoiceitem> scmpurinvoiceitems = scmpurinvoiceitemMapper.selectList(queryWrapper);
		return scmpurinvoiceitems;
	}
}
