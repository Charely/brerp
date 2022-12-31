package org.br.erp.mv.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.MvouncherApi;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.im.entity.Scmiminvoucher;
import org.br.erp.im.entity.Scmiminvoucheritem;
import org.br.erp.im.mapper.ScmiminvoucheritemMapper;
import org.br.erp.im.mapper.ScmiminvoucherMapper;
import org.br.erp.im.service.IScmiminvoucherService;
import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.service.IScminventorytypeService;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.entity.Scminventoy;
import org.br.erp.inventory.service.IScminventoyService;
import org.br.erp.inventory.service.IScminventoyitemService;
import org.br.erp.po.entity.Scmpurinvoiceitem;
import org.br.erp.po.service.IScmpurinvoiceitemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 入库凭证
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Service
public class ScmiminvoucherServiceImpl extends ServiceImpl<ScmiminvoucherMapper, Scmiminvoucher> implements IScmiminvoucherService, MvouncherApi {

	@Autowired
	private ScmiminvoucherMapper scmiminvoucherMapper;
	@Autowired
	private ScmiminvoucheritemMapper scmiminvoucheritemMapper;

	@Autowired
	private IScminventoyService scminventoyService;

	@Autowired
	private IScminventoyitemService scminventoyitemService;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;

	@Autowired
	private IScminventorytypeService scminventorytypeService;

	@Autowired
	private IScmpurinvoiceitemService scmpurinvoiceitemService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmiminvoucher scmiminvoucher, List<Scmiminvoucheritem> scmiminvoucheritemList) {
		scmiminvoucherMapper.insert(scmiminvoucher);
		if(scmiminvoucheritemList!=null && scmiminvoucheritemList.size()>0) {
			for(Scmiminvoucheritem entity:scmiminvoucheritemList) {
				//外键设置
				entity.setParentid(scmiminvoucher.getId());
				scmiminvoucheritemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmiminvoucher scmiminvoucher,List<Scmiminvoucheritem> scmiminvoucheritemList) {
		scmiminvoucherMapper.updateById(scmiminvoucher);
		
		//1.先删除子表数据
		scmiminvoucheritemMapper.deleteByMainId(scmiminvoucher.getId());
		
		//2.子表数据重新插入
		if(scmiminvoucheritemList!=null && scmiminvoucheritemList.size()>0) {
			for(Scmiminvoucheritem entity:scmiminvoucheritemList) {
				//外键设置
				entity.setParentid(scmiminvoucher.getId());
				scmiminvoucheritemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmiminvoucheritemMapper.deleteByMainId(id);
		scmiminvoucherMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmiminvoucheritemMapper.deleteByMainId(id.toString());
			scmiminvoucherMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void createMvouncher(List<String> ids,String flag) throws ParseException {
		for (String id : ids) {
			//获取每一个入库单
			Scminventoy scminventoy = scminventoyService.getById(id);
			if(!scminventoy.getStatus().equalsIgnoreCase("2")){
				continue;
			}
			if(scminventoy.getInvouncherflag().equalsIgnoreCase("1")){
				throw new RuntimeException("单据已生成存货凭证，不允许重复生成！");
			}
			//将入库单生成凭证
			List<Scminventoryitem> scminventoryitems = scminventoyitemService.selectByMainId(id);

			Scmiminvoucher scmiminvoucher=new Scmiminvoucher();
			scmiminvoucher.setBillcode(scmbillcoderuleService.getBillCode("invouncher"));
			scmiminvoucher.setBilldate(ERPUtils.getNowDate());
			Scminventorytype scminventorytype = scminventorytypeService.getInventoryTypeByTypecode(scminventoy.getInventorytype());
			if(scminventorytype.getInstock()!=null && scminventorytype.getInstock().equalsIgnoreCase("true")) {
				//入库库存生成的入库凭证
				scmiminvoucher.setVoucherkind("0");
			}else{
				//出库单生成出库凭证
				scmiminvoucher.setVoucherkind("1");
			}

			scmiminvoucher.setVdeptid(scminventoy.getInvdepartid());
			scmiminvoucher.setVemptid(scminventoy.getInvemployid());
			scmiminvoucher.setWarehouseid(scminventoy.getWarehouseid());
			scmiminvoucher.setRemarks(scminventoy.getRemark());
			scmiminvoucher.setCompanyid(scminventoy.getCompanyid());
			if(flag.equalsIgnoreCase("0")) {
				if (scminventoy.getInvoiceflag() != null && !scminventoy.getInvoiceflag().equalsIgnoreCase("0")) {
					scmiminvoucher.setIszg("2");
				} else {
					scmiminvoucher.setIszg("0");
				}
			}

			scmiminvoucherMapper.insert(scmiminvoucher);

			scminventoy.setInvouncherflag("1");
			scminventoyService.updateById(scminventoy);

			List<Scmiminvoucheritem> itemRes=new ArrayList<>();
			for (Scminventoryitem scminventoryitem : scminventoryitems) {
				Scmiminvoucheritem scmiminvoucheritem=new Scmiminvoucheritem();
				scmiminvoucheritem.setParentid(scmiminvoucher.getId());
				scmiminvoucheritem.setMaterialid(scminventoryitem.getMaterialid());
				scmiminvoucheritem.setMaterialcode(scminventoryitem.getMaterialcode());
				scmiminvoucheritem.setMaterialname(scminventoryitem.getMaterialname());
				if(flag.equalsIgnoreCase("0")) {
					if (scminventoryitem.getInvoiceflag() != null && !scminventoryitem.getInvoiceflag().equalsIgnoreCase("0")) {
						//存在开票价，那就按照开票价格来生成存货入库凭证
						scmiminvoucheritem.setIszg("2");//存在开票价直接显示核销
						List<Scmpurinvoiceitem> scmpurinvoiceitems = scmpurinvoiceitemService.selectByFromItemId(scminventoryitem.getId());
						if (scmpurinvoiceitems != null && scmpurinvoiceitems.size() > 0) {
							Scmpurinvoiceitem scmpurinvoiceitem = scmpurinvoiceitems.get(0);
							scmiminvoucheritem.setTaxinprice(scmpurinvoiceitem.getTaxinprice().toString());
							scmiminvoucheritem.setQty(scminventoryitem.getInvoiceqty());
							BigDecimal multiply = ERPUtils.getBigDecimal(scmiminvoucheritem.getTaxinprice()).multiply(ERPUtils.getBigDecimal(scmiminvoucheritem.getQty())).setScale(2);
							scmiminvoucheritem.setTaxinvalue(multiply.toString());
							if (scminventoryitem.getInvoiceflag() != null && scminventoryitem.getInvoiceflag().equalsIgnoreCase("1")) {
								scmiminvoucheritem.setStocklocationid(scminventoryitem.getStocklocationid());
								scmiminvoucheritem.setIszg("2");
								scmiminvoucheritem.setStocklocationname(scminventoryitem.getStocklocationname());
								scmiminvoucheritem.setBatchid(scminventoryitem.getBatchid());
								scmiminvoucheritem.setInventorykindid(scminventoryitem.getInventorykindid());
								scmiminvoucheritem.setFromid(scminventoryitem.getParentid());
								scmiminvoucheritem.setFromitemid(scminventoryitem.getId());
								scmiminvoucheritemMapper.insert(scmiminvoucheritem);

								Scmiminvoucheritem bluescmiminvoucheritem = new Scmiminvoucheritem();
								bluescmiminvoucheritem.setParentid(scmiminvoucher.getId());
								bluescmiminvoucheritem.setMaterialid(scminventoryitem.getMaterialid());
								bluescmiminvoucheritem.setMaterialcode(scminventoryitem.getMaterialcode());
								bluescmiminvoucheritem.setMaterialname(scminventoryitem.getMaterialname());

								bluescmiminvoucheritem.setTaxinprice(scminventoryitem.getTaxinprice().toString());
								bluescmiminvoucheritem.setQty(ERPUtils.getBigDecimal(scminventoryitem.getQty())
										.subtract(ERPUtils.getBigDecimal(scminventoryitem.getInvoiceqty())).setScale(2).toString());
								bluescmiminvoucheritem.setTaxinvalue(ERPUtils.getBigDecimal(bluescmiminvoucheritem.getTaxinprice())
										.multiply(ERPUtils.getBigDecimal(bluescmiminvoucheritem.getQty())).setScale(2).toString());
								bluescmiminvoucheritem.setStocklocationid(scminventoryitem.getStocklocationid());
								bluescmiminvoucheritem.setStocklocationname(scminventoryitem.getStocklocationname());
								bluescmiminvoucheritem.setBatchid(scminventoryitem.getBatchid());
								bluescmiminvoucheritem.setInventorykindid(scminventoryitem.getInventorykindid());
								bluescmiminvoucheritem.setFromid(scminventoryitem.getParentid());
								bluescmiminvoucheritem.setFromitemid(scminventoryitem.getId());
								bluescmiminvoucheritem.setIszg("0");
								scmiminvoucheritemMapper.insert(bluescmiminvoucheritem);
								continue;
							}
						}
					} else {
						scmiminvoucheritem.setIszg("0");
						scmiminvoucheritem.setTaxinprice(scminventoryitem.getTaxinprice() == null ? "0" : scminventoryitem.getTaxinprice().toString());
						scmiminvoucheritem.setQty(scminventoryitem.getQty() == null ? "0" : scminventoryitem.getQty());
						scmiminvoucheritem.setTaxinvalue(scminventoryitem.getTaxinvalue() == null ? "0" : scminventoryitem.getTaxinvalue().toString());
					}
				}else if(flag.equalsIgnoreCase("1")){
					scmiminvoucheritem.setTaxinprice("0");
					scmiminvoucheritem.setQty(scminventoryitem.getQty());
					scmiminvoucheritem.setTaxinvalue("0");
				}
				scmiminvoucheritem.setStocklocationid(scminventoryitem.getStocklocationid());
				scmiminvoucheritem.setStocklocationname(scminventoryitem.getStocklocationname());
				scmiminvoucheritem.setBatchid(scminventoryitem.getBatchid());
				scmiminvoucheritem.setInventorykindid(scminventoryitem.getInventorykindid());
				/**
				 * 冗余参照单据的分录ID和id
				 */
				scmiminvoucheritem.setFromid(scminventoryitem.getParentid());
				scmiminvoucheritem.setFromitemid(scminventoryitem.getId());
				scmiminvoucheritemMapper.insert(scmiminvoucheritem);
			}
		}
	}

	@Override
	@Transactional
	public void updateStatus(List<String> ids, String flag) {
		ids.forEach(item->{
			Scmiminvoucher scmiminvoucher = scmiminvoucherMapper.selectById(item);
			if(!scmiminvoucher.getStatus().equalsIgnoreCase("flag")){
				scmiminvoucher.setStatus(flag);
				scmiminvoucherMapper.updateById(scmiminvoucher);
			}
		});
	}

	@Override
	public void createMvouncher(JSONArray jsonArray) {
	}

	@Override
	public void writeBackMvouncherFromInstockApi(JSONObject jsonObject) {
		if(jsonObject==null){
			throw new RuntimeException("存货生成错误，请检查入口参数!");
		}

		//TODO 查找当前的入库单所对应的存货凭证
		String itemid = jsonObject.get("fromitemid").toString();
		String invoiceqty = jsonObject.get("invoiceqty").toString();
		String taxinprice = jsonObject.get("taxinprice").toString();

		QueryWrapper<Scmiminvoucheritem> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("fromitemid",itemid);
		queryWrapper.eq("iszg","0");
		List<Scmiminvoucheritem> scmiminvoucheritems = scmiminvoucheritemMapper.selectList(queryWrapper);

		for (org.br.erp.im.entity.Scmiminvoucheritem scmiminvoucheritem : scmiminvoucheritems) {
			if(ERPUtils.getBigDecimal(scmiminvoucheritem.getTaxinprice()).compareTo(ERPUtils.getBigDecimal(taxinprice)) == 0){
				continue;
			}else{

				Scmiminvoucher scmiminvoucher = scmiminvoucherMapper.selectById(scmiminvoucheritem.getParentid());
				Scmiminvoucher redScmiminvoucher=new Scmiminvoucher();
				BeanUtils.copyProperties(scmiminvoucher,redScmiminvoucher);
				redScmiminvoucher.setId("");
				redScmiminvoucher.setBillcode(redScmiminvoucher.getBillcode()+"_V");
				redScmiminvoucher.setStatus("0");
				redScmiminvoucher.setBilldate(ERPUtils.getNowDate());
				redScmiminvoucher.setIszg("1");
				scmiminvoucherMapper.insert(redScmiminvoucher);
				//需要产生冲销凭证的信息
				Scmiminvoucheritem redScminvouncherItem=new Scmiminvoucheritem();
				BeanUtils.copyProperties(scmiminvoucheritem,redScminvouncherItem);
				redScminvouncherItem.setId("");
				redScminvouncherItem.setQty("-"+invoiceqty);
				BigDecimal taxinvalue = ERPUtils.getBigDecimal(redScminvouncherItem.getTaxinprice()).multiply(ERPUtils.getBigDecimal(redScminvouncherItem.getQty())).setScale(2);
				redScminvouncherItem.setTaxinvalue(taxinvalue.toString());
				redScminvouncherItem.setParentid(redScmiminvoucher.getId());
				scmiminvoucheritemMapper.insert(redScminvouncherItem);

				//TODO 开始生成蓝单

				Scmiminvoucher blueScmiminvouncher=new Scmiminvoucher();
				BeanUtils.copyProperties(scmiminvoucher,blueScmiminvouncher);
				blueScmiminvouncher.setId("");
				blueScmiminvouncher.setBillcode(blueScmiminvouncher.getBillcode()+"_X");
				blueScmiminvouncher.setBilldate(ERPUtils.getNowDate());
				blueScmiminvouncher.setIszg("2");
				scmiminvoucherMapper.insert(blueScmiminvouncher);

				//生成蓝单
				Scmiminvoucheritem blueScmiminvouncherItem=new Scmiminvoucheritem();
				BeanUtils.copyProperties(scmiminvoucheritem,blueScmiminvouncherItem);
				blueScmiminvouncherItem.setId("");
				blueScmiminvouncherItem.setParentid(blueScmiminvouncher.getId());
				blueScmiminvouncherItem.setTaxinprice(taxinprice);
				BigDecimal multiply = ERPUtils.getBigDecimal(taxinprice).multiply(ERPUtils.getBigDecimal(invoiceqty));
				BigDecimal bigDecimal = multiply.setScale(2);
				blueScmiminvouncherItem.setQty(invoiceqty);
				blueScmiminvouncherItem.setTaxinvalue(bigDecimal.toString());
				scmiminvoucheritemMapper.insert(blueScmiminvouncherItem);

			}
		}


	}
}
