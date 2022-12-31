package org.br.erp.po.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.writebackinstockApi;
import org.br.erp.base.service.writebackapi.writebackpo;
import org.br.erp.po.entity.Scmpurinvoice;
import org.br.erp.po.entity.Scmpurinvoiceitem;
import org.br.erp.po.mapper.ScmpurinvoiceitemMapper;
import org.br.erp.po.mapper.ScmpurinvoiceMapper;
import org.br.erp.po.service.IScmpurinvoiceService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购发票
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@Service
public class ScmpurinvoiceServiceImpl extends ServiceImpl<ScmpurinvoiceMapper, Scmpurinvoice> implements IScmpurinvoiceService {

	@Autowired
	private ScmpurinvoiceMapper scmpurinvoiceMapper;
	@Autowired
	private ScmpurinvoiceitemMapper scmpurinvoiceitemMapper;

	@Autowired
	private writebackpo writebackpo;

	@Autowired
	private writebackinstockApi writebackinstockApi;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmpurinvoice scmpurinvoice, List<Scmpurinvoiceitem> scmpurinvoiceitemList) {

		String billCode = scmbillcoderuleService.getBillCode("scmpurinvoice");
		scmpurinvoice.setBillcode(billCode );
		scmpurinvoiceMapper.insert(scmpurinvoice);
		if(scmpurinvoiceitemList!=null && scmpurinvoiceitemList.size()>0) {
			for(Scmpurinvoiceitem entity:scmpurinvoiceitemList) {
				//外键设置
				entity.setParentid(scmpurinvoice.getId());
				scmpurinvoiceitemMapper.insert(entity);
				if(entity.getFromitemid()!=null && !entity.getFromitemid().equalsIgnoreCase("")
						&& entity.getFromtype()!=null && entity.getFromtype().equalsIgnoreCase("PO")){
					writebackpo.updatepoiteminvoiceqty(entity.getFromitemid(),entity.getQty().toString().replaceAll("-",""),false);
				}else if(entity.getFromitemid()!=null && !entity.getFromitemid().equalsIgnoreCase("")
						&&entity.getFromtype()!=null && entity.getFromtype().equalsIgnoreCase("INSTOCK")){
					//回写出库单并判断是否生成暂估冲销凭证
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("itemid",entity.getFromitemid());
					jsonObject.put("invoiceqty",entity.getQty());
					jsonObject.put("taxinprice",entity.getTaxinprice());
					writebackinstockApi.WriteBackInstockFromPurinvoice(jsonObject);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmpurinvoice scmpurinvoice,List<Scmpurinvoiceitem> scmpurinvoiceitemList) {
		scmpurinvoiceMapper.updateById(scmpurinvoice);
		
		//1.先删除子表数据
		//先删除参照数据
		scmpurinvoiceitemMapper.selectByMainId(scmpurinvoice.getId()).stream().forEach(item->{
			if(item.getFromitemid()!=null && !item.getFromitemid().equalsIgnoreCase("")){
				writebackpo.updatepoiteminvoiceqty(item.getFromitemid(),item.getQty().toString().replaceAll("-",""),true);
			}
		});
		scmpurinvoiceitemMapper.deleteByMainId(scmpurinvoice.getId());
		
		//2.子表数据重新插入
		if(scmpurinvoiceitemList!=null && scmpurinvoiceitemList.size()>0) {
			for(Scmpurinvoiceitem entity:scmpurinvoiceitemList) {
				//外键设置
				entity.setParentid(scmpurinvoice.getId());
				scmpurinvoiceitemMapper.insert(entity);
				if(entity.getFromitemid()!=null && !entity.getFromitemid().equalsIgnoreCase("")){
					writebackpo.updatepoiteminvoiceqty(entity.getFromitemid(),entity.getQty().toString().replaceAll("-",""),false);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmpurinvoiceitemMapper.selectByMainId(id).stream().forEach(item->{
			if(item.getFromitemid()!=null && !item.getFromitemid().equalsIgnoreCase("")){
				writebackpo.updatepoiteminvoiceqty(item.getFromitemid(),item.getQty().toString().replaceAll("-",""),true);
			}
		});
		scmpurinvoiceitemMapper.deleteByMainId(id);
		scmpurinvoiceMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmpurinvoiceitemMapper.deleteByMainId(id.toString());
			scmpurinvoiceMapper.deleteById(id);
		}
	}
	
}
