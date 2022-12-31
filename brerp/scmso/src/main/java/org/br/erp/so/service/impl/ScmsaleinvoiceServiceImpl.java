package org.br.erp.so.service.impl;

import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.writebackso;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.so.entity.Scmsaleinvoice;
import org.br.erp.so.entity.Scmsalesinvoiceitem;
import org.br.erp.so.mapper.ScmsalesinvoiceitemMapper;
import org.br.erp.so.mapper.ScmsaleinvoiceMapper;
import org.br.erp.so.service.IScmsaleinvoiceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Random;

/**
 * @Description: 销售发票
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@Service
public class ScmsaleinvoiceServiceImpl extends ServiceImpl<ScmsaleinvoiceMapper, Scmsaleinvoice> implements IScmsaleinvoiceService {

	@Autowired
	private ScmsaleinvoiceMapper scmsaleinvoiceMapper;
	@Autowired
	private ScmsalesinvoiceitemMapper scmsalesinvoiceitemMapper;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;

	@Autowired
	writebackso writebackso;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmsaleinvoice scmsaleinvoice, List<Scmsalesinvoiceitem> scmsalesinvoiceitemList) {

		String billCode = scmbillcoderuleService.getBillCode("saleinvoice");
		scmsaleinvoice.setBillcode(billCode);
		scmsaleinvoiceMapper.insert(scmsaleinvoice);
		if(scmsalesinvoiceitemList!=null && scmsalesinvoiceitemList.size()>0) {
			for(Scmsalesinvoiceitem entity:scmsalesinvoiceitemList) {
				//外键设置
				entity.setParentid(scmsaleinvoice.getId());
				scmsalesinvoiceitemMapper.insert(entity);
				if(entity.getFromitemid()!=null && !entity.getFromitemid().equalsIgnoreCase("")){
					writebackso.updatesoiteminvoiceqty(entity.getFromitemid(),entity.getQty().replaceAll("-",""),false);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmsaleinvoice scmsaleinvoice,List<Scmsalesinvoiceitem> scmsalesinvoiceitemList) {
		scmsaleinvoiceMapper.updateById(scmsaleinvoice);
		
		//1.先删除子表数据
		//删除前先回写单据信息
		scmsalesinvoiceitemMapper.selectByMainId(scmsaleinvoice.getId()).stream().forEach(item->{
			if(item.getFromitemid()!=null && !item.getFromitemid().equalsIgnoreCase("")){
				writebackso.updatesoiteminvoiceqty(item.getFromitemid(),item.getQty().replaceAll("-",""),true);
			}
		});
		scmsalesinvoiceitemMapper.deleteByMainId(scmsaleinvoice.getId());
		
		//2.子表数据重新插入
		if(scmsalesinvoiceitemList!=null && scmsalesinvoiceitemList.size()>0) {
			for(Scmsalesinvoiceitem entity:scmsalesinvoiceitemList) {
				//外键设置
				entity.setParentid(scmsaleinvoice.getId());
				scmsalesinvoiceitemMapper.insert(entity);
				if(entity.getFromitemid()!=null && !entity.getFromitemid().equalsIgnoreCase("")){
					writebackso.updatesoiteminvoiceqty(entity.getFromitemid(),entity.getQty().replaceAll("-",""),false);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {

		scmsalesinvoiceitemMapper.selectByMainId(id).stream().forEach(item->{
			if(item.getFromitemid()!=null && !item.getFromitemid().equalsIgnoreCase("")){
				writebackso.updatesoiteminvoiceqty(item.getFromitemid(),item.getQty().replaceAll("-",""),true);
			}
		});
		scmsalesinvoiceitemMapper.deleteByMainId(id);
		scmsaleinvoiceMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmsalesinvoiceitemMapper.deleteByMainId(id.toString());
			scmsaleinvoiceMapper.deleteById(id);
		}
	}
	
}
