package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.mapper.ScmpoitemMapper;
import org.br.erp.po.service.IScmpoitemService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 采购订单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@Service
public class ScmpoitemServiceImpl extends ServiceImpl<ScmpoitemMapper, Scmpoitem> implements IScmpoitemService {
	
	@Autowired
	private ScmpoitemMapper scmpoitemMapper;
	
	@Override
	public List<Scmpoitem> selectByMainId(String mainId) {
		return scmpoitemMapper.selectByMainId(mainId);
	}

	@Override
	@Transactional
	public void writebackfrompo(String itemid, BigDecimal qty,boolean isdelete) {
		Scmpoitem scmpoitem = scmpoitemMapper.selectById(itemid);
		if(scmpoitem==null){
			return;
		}

		if(isdelete)
		{
			scmpoitem.setReceiptreqqty(scmpoitem.getReceiptreqqty().subtract(qty));
		}else {
			if(scmpoitem.getReceiptreqqty() ==null){
				scmpoitem.setReceiptreqqty(new BigDecimal(0));
			}
			BigDecimal add = scmpoitem.getReceiptreqqty().add(qty);
			scmpoitem.setReceiptreqqty(add);
		}
		//
		if(scmpoitem.getReceiptreqqty().compareTo(scmpoitem.getQty()) >=1){
			scmpoitem.setReceiptreqflag("2");
		}else if(scmpoitem.getReceiptreqqty().equals(0)){
			scmpoitem.setReceiptreqflag("0");
		}else {
			scmpoitem.setReceiptreqflag("1");
		}
		scmpoitemMapper.updateById(scmpoitem);
	}
}
