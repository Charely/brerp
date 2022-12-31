package org.br.erp.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.service.writebackapi.writebackso;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.so.entity.Scmso;
import org.br.erp.so.entity.Scmsoitem;
import org.br.erp.so.mapper.ScmsoMapper;
import org.br.erp.so.mapper.ScmsoitemMapper;
import org.br.erp.so.service.IScmsoitemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 销售订单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
@Service
public class ScmsoitemServiceImpl extends ServiceImpl<ScmsoitemMapper, Scmsoitem>
		implements IScmsoitemService,
		writebackso {
	
	@Autowired
	private ScmsoitemMapper scmsoitemMapper;

	@Autowired
	private ScmsoMapper scmsoMapper;
	
	@Override
	public List<Scmsoitem> selectByMainId(String mainId) {
		return scmsoitemMapper.selectByMainId(mainId);
	}

	@Override
	@Transactional
	public void updateSoitemOutFlag(String itemid, String outqty) {
		Scmsoitem scmsoitem = scmsoitemMapper.selectById(itemid);
		if(scmsoitem.getOutqty().compareTo(BigDecimal.ZERO) == 0){
			scmsoitem.setOutqty(new BigDecimal(0));
		}
		BigDecimal oldqty = scmsoitem.getOutqty();
		BigDecimal add = oldqty.add(new BigDecimal(outqty));
		if(add.compareTo(scmsoitem.getQty())==1){
			throw  new RuntimeException("出库数量大于订单数量，不允许出库");
		}
		scmsoitem.setOutqty(add);
		//scmsoitem.setOutqty(outqty);
		BigDecimal qty= scmsoitem.getQty();
		if (scmsoitem.getOutqty().compareTo(scmsoitem.getQty()) == 0) {
			scmsoitem.setOutflag("2");
		}else{
			scmsoitem.setOutflag("1");
		}
		scmsoitemMapper.updateById(scmsoitem);
		QueryWrapper<Scmsoitem> queryWrapper=new QueryWrapper<>();

		double v = scmsoitemMapper.selectByMainId(scmsoitem.getParentid()).stream().map(item -> {
			return Integer.parseInt(item.getOutflag());
		}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);

		Scmso scmso = scmsoMapper.selectById(scmsoitem.getParentid());
		scmso.setOutflag(ERPUtils.convertDoubleToString(v));
		scmsoMapper.updateById(scmso);
	}

	@Override
	@Transactional
	public void updatesoitemoutqty(String soitemid, String outqty) {
		this.updateSoitemOutFlag(soitemid,outqty);
	}

	@Override
	public void updatesoiteminvoiceqty(String soitemid, String invoiceqty,Boolean isdelete) {
		Scmsoitem scmsoitem = scmsoitemMapper.selectById(soitemid);
		if(scmsoitem!=null){
			BigDecimal curinvoiceqty = scmsoitem.getInvoiceqty();
			BigDecimal add=new BigDecimal(0);
			if(isdelete){
				add=curinvoiceqty.subtract(new BigDecimal(invoiceqty));
			}else{
				add=curinvoiceqty.add(new BigDecimal(invoiceqty));
			}
			if(add.compareTo(scmsoitem.getQty()) > 0){
				throw new RuntimeException("不允许超量开票");
			} else if(add.compareTo(scmsoitem.getQty()) == 0){
				scmsoitem.setInvoiceflag("2");
			} else{
				scmsoitem.setInvoiceflag("1");
			}
			if(add.compareTo(BigDecimal.ZERO) == 0){
				scmsoitem.setInvoiceflag("0");
			}
			scmsoitem.setInvoiceqty(add);

			scmsoitemMapper.updateById(scmsoitem);
			double v = scmsoitemMapper.selectByMainId(scmsoitem.getParentid()).stream().map(item -> {
				return Integer.parseInt(item.getInvoiceflag());
			}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);

			Scmso scmso = scmsoMapper.selectById(scmsoitem.getParentid());
			scmso.setInvoiceflag(ERPUtils.convertDoubleToString(v));
			scmsoMapper.updateById(scmso);

		}
	}
}
