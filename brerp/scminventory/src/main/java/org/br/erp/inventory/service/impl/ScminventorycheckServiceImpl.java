package org.br.erp.inventory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.service.IScmbatchService;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.inventory.entity.Scminventorycheck;
import org.br.erp.inventory.entity.Scminventorycheckitem;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.entity.Scminventoy;
import org.br.erp.inventory.mapper.ScminventorycheckitemMapper;
import org.br.erp.inventory.mapper.ScminventorycheckMapper;
import org.br.erp.inventory.service.IScminventorycheckService;
import org.br.erp.inventory.service.IScminventoyService;
import org.br.erp.inventory.service.IScminventoyitemService;
import org.jeecg.common.util.FillRuleUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 库存盘点单
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Service
public class ScminventorycheckServiceImpl extends ServiceImpl<ScminventorycheckMapper, Scminventorycheck> implements IScminventorycheckService {

	@Autowired
	private ScminventorycheckMapper scminventorycheckMapper;
	@Autowired
	private ScminventorycheckitemMapper scminventorycheckitemMapper;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;
	@Autowired
	private IScminventoyitemService scminventoyitemService;
	@Autowired
	private IScminventoyService scminventoyService;
	@Autowired
	private IScmbatchService scmbatchService;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scminventorycheck scminventorycheck, List<Scminventorycheckitem> scminventorycheckitemList) {

		String billCode="";
		billCode = scmbillcoderuleService.getBillCode("scmstockcheck");
		Object pdjcode = FillRuleUtil.executeRule("pdjcode", null);
		scminventorycheck.setBillcode(pdjcode.toString());

		scminventorycheckMapper.insert(scminventorycheck);
		if(scminventorycheckitemList!=null && scminventorycheckitemList.size()>0) {
			for(Scminventorycheckitem entity:scminventorycheckitemList) {
				//外键设置
				entity.setParentid(scminventorycheck.getId());
				scminventorycheckitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scminventorycheck scminventorycheck,List<Scminventorycheckitem> scminventorycheckitemList) {
		scminventorycheckMapper.updateById(scminventorycheck);
		
		//1.先删除子表数据
		scminventorycheckitemMapper.deleteByMainId(scminventorycheck.getId());
		
		//2.子表数据重新插入
		if(scminventorycheckitemList!=null && scminventorycheckitemList.size()>0) {
			for(Scminventorycheckitem entity:scminventorycheckitemList) {
				//外键设置
				entity.setParentid(scminventorycheck.getId());
				scminventorycheckitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scminventorycheckitemMapper.deleteByMainId(id);
		scminventorycheckMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scminventorycheckitemMapper.deleteByMainId(id.toString());
			scminventorycheckMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updatePoStatus(List<String> ids, String statusflag) {
		QueryWrapper<Scminventorycheck> queryWrapper=new QueryWrapper<>();
		queryWrapper.ne("status",statusflag);
		queryWrapper.ne("checkflag","1");
		queryWrapper.in("id",ids);

		List<Scminventorycheck> scminventorychecks = scminventorycheckMapper.selectList(queryWrapper);

		scminventorychecks.forEach(item->{
			item.setStatus(statusflag);
			scminventorycheckMapper.updateById(item);
		});
	}

	/**
	 * 盘点确认
	 * 将盘点单生成对应的出入库单
	 * 实际数量超过库存数量生成盘盈单
	 * 实际数量小于库存数量生成盘亏但
	 * @param ids
	 */
	@Override
	@Transactional
	public void inventorycheck(List<String> ids) {
		for(int i=0;i<ids.size();i++){
			Scminventorycheck scminventorycheck = scminventorycheckMapper.selectById(ids.get(i));
			if(scminventorycheck.getCheckflag().equalsIgnoreCase("1")){
				continue;
			}
			List<Scminventorycheckitem> pyd = new ArrayList<>();
			List<Scminventorycheckitem> pkd = new ArrayList<>();
			List<Scminventorycheckitem> scminventorycheckitems = scminventorycheckitemMapper.selectByMainId(ids.get(i));
			if(scminventorycheckitems!=null && scminventorycheckitems.size()>0){
				//汇总分录盘点行信息
				for (Scminventorycheckitem scminventorycheckitem : scminventorycheckitems) {
					String materialid = scminventorycheckitem.getMaterialid();
					BigDecimal realityqty = scminventorycheckitem.getRealityqty();
					BigDecimal budgetqty = scminventorycheckitem.getBudgetqty();
					//如果账本数据和盘点数据相等，则不进行处理
					if(realityqty.compareTo(budgetqty) == 0){
						continue;
					}
					//如果账本数据多于实际数据，那么将生成盘亏单
					if(budgetqty.compareTo(realityqty) == 1){
						pkd.add(scminventorycheckitem);
					}else if(budgetqty.compareTo(realityqty) == -1){
						pyd.add(scminventorycheckitem);
					}
				}

				if(pyd.size()>0){
					//盘盈单存在
					Scminventoy scminventoy=new Scminventoy();
					scminventoy.setCompanyid(scminventorycheck.getCompanyid());
					scminventoy.setBilldate(ERPUtils.getNowDate());
					scminventoy.setInventorytype("104");
					scminventoy.setBillcode(scmbillcoderuleService.getBillCode("checkinstock"));
					scminventoy.setIsred(false);
					scminventoy.setStatus("0");
					scminventoy.setInvoiceflag("0");
					scminventoy.setWarehouseid(scminventorycheck.getWarehouseid());
					scminventoyService.save(scminventoy);
					List<Scminventoryitem> res=new ArrayList<>();
					for (Scminventorycheckitem scminventorycheckitem : pyd) {
						Scminventoryitem scminventoryitem=new Scminventoryitem();
						scminventoryitem.setMaterialid(scminventorycheckitem.getMaterialid());
						scminventoryitem.setMaterialcode(scminventorycheckitem.getMaterialcode());
						scminventoryitem.setMaterialname(scminventorycheckitem.getMaterialname());
						scminventoryitem.setUomid(scminventorycheckitem.getUomid());
						scminventoryitem.setInstock("true");
						scminventoryitem.setStocklocationid(scminventorycheckitem.getStocklocationid());
						scminventoryitem.setStocklocationname(scminventorycheckitem.getStocklocationname());
						scminventoryitem.setOutstock("false");
						scminventoryitem.setInventorykindid(scminventorycheckitem.getInventorykindid());
						scminventoryitem.setStocktypeid(scminventorycheckitem.getStocktypeid());
						if(scminventorycheckitem.getBatchid()!=null &&
								!scminventorycheckitem.getBatchid().equalsIgnoreCase("")) {
							Scmbatch scmbatch = scmbatchService.getById(scminventorycheckitem.getBatchid());
							scminventoryitem.setBatchid(scmbatch.getId());
							scminventoryitem.setBatchcode(scmbatch.getBatchcode());
							scminventoryitem.setBatchstartdate(ERPUtils.getTimestamp(scmbatch.getFromdate(),""));
							scminventoryitem.setBatchenddate(ERPUtils.getTimestamp(scmbatch.getEnddate(),""));
						}else{
							scminventoryitem.setBatchid("");
							scminventoryitem.setBatchcode("");
						}
						scminventoryitem.setFromid(scminventorycheck.getId());
						scminventoryitem.setFromid(scminventorycheckitem.getId());
						scminventoryitem.setQty(scminventorycheckitem.getRealityqty().subtract(scminventorycheckitem.getBudgetqty()).toString());
						scminventoryitem.setUomid(scminventoryitem.getUomid());
						scminventoryitem.setParentid(scminventoy.getId());
						res.add(scminventoryitem);
					}
					if(res.size()>0){
						scminventoyitemService.saveBatch(res);
					}
				}else if(pkd.size()>0){
					//盘亏单存在
					//盘盈单存在
					Scminventoy scminventoy=new Scminventoy();
					scminventoy.setCompanyid(scminventorycheck.getCompanyid());
					scminventoy.setBilldate(ERPUtils.getNowDate());
					scminventoy.setInventorytype("204");
					scminventoy.setBillcode(scmbillcoderuleService.getBillCode("checkinstock"));
					scminventoy.setIsred(false);
					scminventoy.setStatus("0");
					scminventoy.setInvoiceflag("0");
					scminventoy.setWarehouseid(scminventorycheck.getWarehouseid());
					scminventoyService.save(scminventoy);
					List<Scminventoryitem> res=new ArrayList<>();
					for (Scminventorycheckitem scminventorycheckitem : pkd) {
						Scminventoryitem scminventoryitem=new Scminventoryitem();
						scminventoryitem.setMaterialid(scminventorycheckitem.getMaterialid());
						scminventoryitem.setMaterialcode(scminventorycheckitem.getMaterialcode());
						scminventoryitem.setMaterialname(scminventorycheckitem.getMaterialname());
						scminventoryitem.setUomid(scminventorycheckitem.getUomid());
						scminventoryitem.setInstock("false");
						scminventoryitem.setStocklocationid(scminventorycheckitem.getStocklocationid());
						scminventoryitem.setStocklocationname(scminventorycheckitem.getStocklocationname());
						scminventoryitem.setOutstock("true");
						scminventoryitem.setInventorykindid(scminventorycheckitem.getInventorykindid());
						scminventoryitem.setStocktypeid(scminventorycheckitem.getStocktypeid());
						if(scminventorycheckitem.getBatchid()!=null &&
								!scminventorycheckitem.getBatchid().equalsIgnoreCase("")) {
							Scmbatch scmbatch = scmbatchService.getById(scminventorycheckitem.getBatchid());
							scminventoryitem.setBatchid(scmbatch.getId());
							scminventoryitem.setBatchcode(scmbatch.getBatchcode());
							scminventoryitem.setBatchstartdate(ERPUtils.getTimestamp(scmbatch.getFromdate(),""));
							scminventoryitem.setBatchenddate(ERPUtils.getTimestamp(scmbatch.getEnddate(),""));
						}else{
							scminventoryitem.setBatchid("");
							scminventoryitem.setBatchcode("");
						}
						scminventoryitem.setFromid(scminventorycheck.getId());
						scminventoryitem.setFromid(scminventorycheckitem.getId());
						scminventoryitem.setQty(scminventorycheckitem.getBudgetqty().subtract(scminventorycheckitem.getRealityqty()).toString());
						scminventoryitem.setUomid(scminventoryitem.getUomid());
						scminventoryitem.setParentid(scminventoy.getId());
						res.add(scminventoryitem);
					}
					if(res.size()>0){
						scminventoyitemService.saveBatch(res);
					}
				}
			}

			scminventorycheck.setCheckflag("1");
			scminventorycheckMapper.updateById(scminventorycheck);
		}
	}


}
