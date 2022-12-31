package org.br.erp.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.br.erp.base.Constant.ERPStatus;
import org.br.erp.base.entity.Material;
import org.br.erp.base.entity.Materialstock;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.entity.Warehouse;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.br.erp.base.service.*;
import org.br.erp.base.service.writebackapi.*;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.service.IScmimbalanceService;
import org.br.erp.inventory.base.service.IScminventorytypeService;
import org.br.erp.inventory.base.service.impl.ScmimbalanceServiceImpl;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.entity.Scminventoy;
import org.br.erp.inventory.mapper.ScminventoyitemMapper;
import org.br.erp.inventory.mapper.ScminventoyMapper;
import org.br.erp.inventory.service.IScminventoyService;
import org.br.erp.inventory.vo.ScmPoInvoiceReferInstock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Service
public class ScminventoyServiceImpl extends ServiceImpl<ScminventoyMapper, Scminventoy>
		implements IScminventoyService, ICreateOutStockFromOutSource, writebackinstockApi {

	@Autowired
	private ScminventoyMapper scminventoyMapper;
	@Autowired
	private ScminventoyitemMapper scminventoyitemMapper;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;
	@Autowired
	private IErpBaseService erpBaseService;
	@Autowired
	private IScmimbalanceService scmimbalanceService;
	@Autowired
	private IScmbatchService scmbatchService;
	@Autowired
	private writebackso writebackso;
	@Autowired
	private writebackpo writebackpo;
	@Autowired
	private IScminventorytypeService scminventorytypeService;
	@Autowired
	private IMaterialService materialService;
	@Autowired
	private IWarehouseService warehouseService;

	@Autowired
	private MvouncherApi mvouncherApi;
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Map saveMap) {
		Scminventoy scminventoy=new Scminventoy();
		Map scminstock = ERPUtils.mapToClass(saveMap, Scminventoy.class, "scminstock");
		if (scminstock != null && scminstock.containsKey("ret")) {
			Object ret = scminstock.get("ret");
			scminventoy = (Scminventoy) ret;
		}

		scminventoy.setStatus(ERPStatus.Create.getCode());

		Scminventorytype scminventorytype = scminventorytypeService.getInventoryTypeByTypecode(scminventoy.getInventorytype());
		String billCode="";
		if(scminventorytype.getInstock()!=null && scminventorytype.getInstock().equalsIgnoreCase("true")) {
			billCode = scmbillcoderuleService.getBillCode("scminstock");
		}else{
			billCode=scmbillcoderuleService.getBillCode("scmoutstock");
		}
		scminventoy.setBillcode(billCode);
		scminventoyMapper.insert(scminventoy);
		if(scminstock.containsKey("cusret")){
			Object cusret = scminstock.get("cusret");
			Map<Object, Object> customdata = (Map<Object, Object>) cusret;
			erpBaseService.saveCustomFieldValues(customdata, "scminventory", scminventoy.getId());
		}
		saveOrUpdateItemData(saveMap,scminventoy);
	}

	@Transactional
	private void saveOrUpdateItemData(Map pageMap, Scminventoy scminventoy) {
		String id = scminventoy.getId();
		String warehouseid = scminventoy.getWarehouseid();
		Warehouse warehouse = warehouseService.getById(warehouseid);
		List<Scminventoryitem> scminventoyitems = new ArrayList<>();
		for (Object o : pageMap.keySet()) {
			if (pageMap.get(o) !=null && pageMap.get(o).getClass().equals(ArrayList.class)) {
				ArrayList scminventoryitems = (ArrayList) pageMap.get(o);
				String inventoryType = scminventorytypeService.getInventoryType(scminventoy.getInventorytype());
				for (Object scminventoryitem : scminventoryitems) {
					Map scminventoyitem = ERPUtils.mapToClass((Map) scminventoryitem, Scminventoryitem.class, "scminventoyitem");
					if (scminventoyitem.containsKey("ret")) {
						Scminventoryitem curItem = (Scminventoryitem) scminventoyitem.get("ret");
						if (curItem != null) {
							curItem.setParentid(id);
							if((curItem.getBatchid()==null || curItem.getBatchid().trim().equalsIgnoreCase(""))
									&& curItem.getVendorbatchcode()!=null &&
									!curItem.getVendorbatchcode().equalsIgnoreCase("")) {
								List<Materialstock> materialstockByMaterialid = materialService.getMaterialstockByMaterialid(curItem.getMaterialid());
								if(materialstockByMaterialid!=null && materialstockByMaterialid.size()>0 && materialstockByMaterialid.get(0).getIsbatch()!=null &&
										materialstockByMaterialid.get(0).getIsbatch().equalsIgnoreCase("true")) {
									if(curItem.getBatchstartdate()==null){
										curItem.setBatchstartdate(new Date());
									}
									if(curItem.getBatchenddate()==null){
										curItem.setBatchenddate(new Date());
									}
									Map map = scmbatchService.saveBatchinfo(scminventoy.getCompanyid(),
											scminventoy.getWarehouseid(),
											curItem.getMaterialid(),
											curItem.getQty(),
											scminventoy.getVendorid(),
											curItem.getVendorbatchcode(),
											ERPUtils.getDateString(curItem.getBatchstartdate(), ""),
											ERPUtils.getDateString(curItem.getBatchenddate(), ""),
											"");

									curItem.setBatchid(map.get("batchid").toString());
									curItem.setBatchcode(map.get("batchcode").toString());
								}
							}
							if(inventoryType.equalsIgnoreCase("0")){
								curItem.setInstock("true");
								curItem.setOutstock("");
							}else{
								curItem.setOutstock("true");
								curItem.setInstock("");
							}
							if(warehouse.getIslocation()!=null && warehouse.getIslocation().equalsIgnoreCase("true")){
								//管理货位
								if(curItem.getStocklocationid()==null || curItem.getStocklocationid().equalsIgnoreCase("")){
									throw new RuntimeException("当前仓库:"+warehouse.getName()+"管理货位,请填写货位！");
								}
							}else{
								curItem.setStocklocationid("");
								curItem.setStocklocationname("");
							}
							scminventoyitemMapper.insert(curItem);
							if(scminventoy.getIsred()==null){
								scminventoy.setIsred(false);
							}
							//保存后更新订单信息
							if(curItem.getOutstock()!=null && curItem.getOutstock().equalsIgnoreCase("true")) {
								if(curItem.getFromitemid()!=null && !curItem.getFromitemid().equalsIgnoreCase("")) {
									writebackso.updatesoitemoutqty(curItem.getFromitemid(), curItem.getQty());
								}
							}else if(curItem.getInstock()!=null && curItem.getInstock().equalsIgnoreCase("true")){
								if(curItem.getFromitemid()!=null && !curItem.getFromitemid().equalsIgnoreCase("")) {
									//判断是否是红单
									if (scminventoy.getIsred().equals(false)) {
										writebackpo.updatematreceiptinstockqty(curItem.getFromitemid(), curItem.getQty());
									} else if (scminventoy.getIsred().equals(true)) {
										//红单，直接回写退货采购订
										writebackpo.updatepoitemreturnqty(curItem.getFromitemid(), curItem.getQty());
									}
								}
							}
							//保存后，自动将批号信息传递到批次档案中

							if (scminventoyitem.containsKey("cusret")) {
								erpBaseService.saveCustomFieldValues((Map<Object, Object>) scminventoyitem.get("cusret"), "scminventoyitem", curItem.getId());
							}
						}
					}

				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scminventoy scminventoy,List<Scminventoryitem> scminventoyitemList) {
		scminventoyMapper.updateById(scminventoy);

		scminventoyitemMapper.selectByMainId(scminventoy.getId()).stream().forEach(item->{
			if(item.getOutstock().equalsIgnoreCase("true")) {
				if (item.getFromitemid() != null && !item.getFromitemid().equalsIgnoreCase("")) {
					writebackso.updatesoitemoutqty(item.getFromitemid(), "-" + item.getQty());
				}
			}
			else{
				if (item.getFromitemid() != null && !item.getFromitemid().equalsIgnoreCase("")){
					writebackpo.updatematreceiptinstockqty(item.getFromitemid(),"-"+item.getQty());
				}
			}
		});
		String inventoryType = scminventorytypeService.getInventoryType(scminventoy.getInventorytype());
		//1.先删除子表数据
		scminventoyitemMapper.deleteByMainId(scminventoy.getId());
		//先去更新订单出库数量

		
		//2.子表数据重新插入
		if(scminventoyitemList!=null && scminventoyitemList.size()>0) {
			for(Scminventoryitem entity:scminventoyitemList) {
				//外键设置
				entity.setParentid(scminventoy.getId());
				if(inventoryType.equalsIgnoreCase("0")){
					entity.setInstock("true");
				}else if(inventoryType.equalsIgnoreCase("1")){
					entity.setOutstock("true");
				}

				if(entity.getOutstock().equalsIgnoreCase("true") &&
						entity.getFromitemid() != null && !entity.getFromitemid().equalsIgnoreCase("")) {
					writebackso.updatesoitemoutqty(entity.getFromitemid(), entity.getQty());
				}else if(entity.getInstock().equalsIgnoreCase("true") &&
						entity.getFromitemid() != null && !entity.getFromitemid().equalsIgnoreCase("")	){
					writebackpo.updatematreceiptinstockqty(entity.getFromitemid(),entity.getQty());
				}
				scminventoyitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		//先回写订单数量
		List<Scminventoryitem> scminventoyitems = scminventoyitemMapper.selectByMainId(id);
		Scminventoy scminventoy = scminventoyMapper.selectById(id);
		scminventoyitems.stream().forEach(item->{
			if(item.getOutstock().equalsIgnoreCase("true")) {
				if (item.getFromitemid() != null && !item.getFromitemid().equalsIgnoreCase("")) {
					writebackso.updatesoitemoutqty(item.getFromitemid(), "-" + item.getQty());
				}
			}else if(item.getInstock().equalsIgnoreCase("true")){
				if (item.getFromitemid() != null && !item.getFromitemid().equalsIgnoreCase("")) {
					if(scminventoy.getIsred().equals(false)) {
						writebackpo.updatematreceiptinstockqty(item.getFromitemid(), "-" + item.getQty());
					}else if(scminventoy.getIsred().equals(true)){
						writebackpo.updatepoitemreturnqty(item.getFromitemid(),"-"+item.getQty());
					}
				}
			}
		});
		scminventoyitemMapper.deleteByMainId(id);
		scminventoyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scminventoyitemMapper.deleteByMainId(id.toString());
			scminventoyMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updateInventoryFlag(String id, String inventoryFlag) {
		Scminventoy scminventoy = scminventoyMapper.selectById(id);
		scminventoy.setInventoryflag(inventoryFlag);
		scminventoyMapper.updateById(scminventoy);
	}

	@Override
	public void updatePoStatus(List<String> ids, String statusflag) {
		QueryWrapper<Scminventoy> queryWrapper=new QueryWrapper<>();
		queryWrapper.ne("status",statusflag);
		//queryWrapper.eq("receiptreqflag","0");
		queryWrapper.and(item->item.eq("invoiceflag","0").or(rq->rq.eq("invoiceflag",null)));
		//queryWrapper.eq("receiptflag","0");
//		queryWrapper.and(item->item.eq("receiptflag","0").or(rq->rq.eq("receiptflag",null)));
		queryWrapper.in("id",ids);
		List<Scminventoy> scmpos = scminventoyMapper.selectList(queryWrapper);
		scmpos.stream().forEach(item->{
			item.setStatus(statusflag);
			scminventoyMapper.updateById(item);
		});
	}

	@Override
	@Transactional
	public void create1000item(String id) {
		Scminventoy scminventoy = scminventoyMapper.selectById(id);
		if(scminventoy==null){
			throw new RuntimeException("不存在");
		}
		String origMainid=scminventoy.getId();
		scminventoy.setId("");
		String instock = scmbillcoderuleService.getBillCode("instock");
		scminventoy.setBillcode(instock);
		scminventoyMapper.insert(scminventoy);
		List<Scminventoryitem> scminventoryitems = scminventoyitemMapper.selectByMainId(origMainid);
		for(int i=0;i<100000;i++){
			Scminventoryitem scminventoryitem=new Scminventoryitem();
			BeanUtils.copyProperties(scminventoryitems.get(0),scminventoryitem);
			scminventoryitem.setParentid(scminventoy.getId());
			scminventoryitem.setId("");
			scminventoyitemMapper.insert(scminventoryitem);
		}
	}

	@Override
	public List<ScmPoInvoiceReferInstock> getReferInstockData(Map queryMap) {
		QueryWrapper<ScmPoInvoiceReferInstock> queryWrapper=new QueryWrapper<>();
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"companyid")){
			queryWrapper.eq("head.companyid",ERPUtils.getHttpReqParam(queryMap,"companyid"));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"billcode")){
			queryWrapper.eq("head.billcode",ERPUtils.getHttpReqParam(queryMap,"billcode"));
		}

		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"materialid")){
			queryWrapper.eq("item.materialid",ERPUtils.getHttpReqParam(queryMap,"materialid"));
		}

		queryWrapper.eq("head.status","2");
		queryWrapper.ne("item.invoiceflag","2");
		queryWrapper.eq("inventorytype","101");

		List<ScmPoInvoiceReferInstock> referInstockData = scminventoyMapper.getReferInstockData(new Page<>(), queryWrapper);
		return referInstockData;
	}


	@Override
	public boolean createOutStock(List<ScmOutSourceToOutStockModel> scmOutSourceToOutStockModels) {
		//组装生成对应的出库单
		if(scmOutSourceToOutStockModels==null || scmOutSourceToOutStockModels.size() == 0){
			return false;
		}
		//安装供应商组装对应的出库单
		Map<String, List<ScmOutSourceToOutStockModel>> collect =
				scmOutSourceToOutStockModels.stream().collect(Collectors.groupingBy(item->{
					return item.getCompanyid()+","+item.getVendorid()+","+item.getWarehouseid();
				}));
		for (String keyMap : collect.keySet()) {
			//根据供应商和公司进行分单操作
			if(keyMap!=null && keyMap!="" && !keyMap.equalsIgnoreCase("")){
				String[] split = keyMap.split(",");
				String companyid=split[0];
				String vendorid=split[1];
				String warehouseid=split[2];
				List<ScmOutSourceToOutStockModel> scmOutSourceToOutStockModels1 = collect.get(keyMap);
				Scminventoy scminventoy=new Scminventoy();
				scminventoy.setCompanyid(companyid);
				scminventoy.setVendorid(vendorid);
				scminventoy.setBilldate(ERPUtils.getNowDate());
				scminventoy.setBillcode(scmbillcoderuleService.getBillCode("instock"));
				scminventoy.setStatus(ERPStatus.Create.getCode());
				scminventoy.setInventorytype("203");//默认位委外出库
				scminventoy.setWarehouseid(warehouseid);
				scminventoy.setIsred(false);
				scminventoyMapper.insert(scminventoy);
				for (ScmOutSourceToOutStockModel scmOutSourceToOutStockModel : scmOutSourceToOutStockModels1) {
					String parentid = scminventoy.getId();
					Material materialbyMaterialId = materialService.getMaterialbyMaterialId(scmOutSourceToOutStockModel.getMaterialid());
					List<Materialstock> materialstockByMaterialid = materialService.getMaterialstockByMaterialid(scmOutSourceToOutStockModel.getMaterialid());
					if(materialstockByMaterialid == null || materialstockByMaterialid.size() ==0){
						throw new RuntimeException("存在物料不进行库存管理，请检查物料配置");
					}


					if(materialstockByMaterialid.get(0).getIsbatch()!=null &&
							materialstockByMaterialid.get(0).getIsbatch().equalsIgnoreCase("true")){
						//获取的部管理批次的信息
						List<Scmbatch> batchList = scmimbalanceService.getBatchList(9999,9999,companyid,
								warehouseid, scmOutSourceToOutStockModel.getMaterialid());
						BigDecimal outQty = ERPUtils.getBigDecimal(scmOutSourceToOutStockModel.getOutqty());
						if(batchList!=null && batchList.size()>0){
							for (Scmbatch scmbatch : batchList) {
								if(ERPUtils.getBigDecimal(scmbatch.getBatchqty()).compareTo(outQty) >=0){
									//如果批号数量大于等于当前数量，则直接默认赋值退出执行下一个
									Scminventoryitem scminventoryitem=new Scminventoryitem();
									scminventoryitem.setInstock("false");
									scminventoryitem.setOutstock("true");
									scminventoryitem.setParentid(parentid);
									scminventoryitem.setMaterialid(scmOutSourceToOutStockModel.getMaterialid());
									scminventoryitem.setMaterialcode(materialbyMaterialId.getMaterialcode());
									scminventoryitem.setMaterialname(materialbyMaterialId.getMaterialname());
									scminventoryitem.setQty(outQty.toString());
									scminventoryitem.setUomid(materialstockByMaterialid.get(0).getStockuomid());
									scminventoryitem.setFromid(scmOutSourceToOutStockModel.getFromid());
									scminventoryitem.setFromitemid(scmOutSourceToOutStockModel.getFromitemid());
									scminventoryitem.setStocktypeid("S01");
									scminventoryitem.setInventorykindid("01");
									scminventoryitem.setBatchid(scmbatch.getId());
									scminventoryitem.setBatchcode(scmbatch.getBatchcode());
									scminventoyitemMapper.insert(scminventoryitem);
									break;
								}
								else{
									//获取批号剩余数量
									BigDecimal batchQty = ERPUtils.getBigDecimal(scmbatch.getBatchqty());
									Scminventoryitem scminventoryitem=new Scminventoryitem();
									scminventoryitem.setInstock("false");
									scminventoryitem.setOutstock("true");
									scminventoryitem.setParentid(parentid);
									scminventoryitem.setMaterialid(scmOutSourceToOutStockModel.getMaterialid());
									scminventoryitem.setMaterialcode(materialbyMaterialId.getMaterialcode());
									scminventoryitem.setMaterialname(materialbyMaterialId.getMaterialname());
									scminventoryitem.setQty(batchQty.toString());
									scminventoryitem.setUomid(materialstockByMaterialid.get(0).getStockuomid());
									scminventoryitem.setFromid(scmOutSourceToOutStockModel.getFromid());
									scminventoryitem.setFromitemid(scmOutSourceToOutStockModel.getFromitemid());
									scminventoryitem.setBatchid(scmbatch.getId());
									scminventoryitem.setBatchcode(scmbatch.getBatchcode());
									scminventoryitem.setStocktypeid("S01");
									scminventoryitem.setInventorykindid("01");
									scminventoyitemMapper.insert(scminventoryitem);
									outQty = outQty.subtract(batchQty);
								}
							}
							if(outQty.compareTo(BigDecimal.ZERO) > 0){
								//当前批号不满足数量，仍需生成对应的空白批号行数据
								Scminventoryitem scminventoryitem=new Scminventoryitem();
								scminventoryitem.setInstock("false");
								scminventoryitem.setOutstock("true");
								scminventoryitem.setParentid(parentid);
								scminventoryitem.setMaterialid(scmOutSourceToOutStockModel.getMaterialid());
								scminventoryitem.setMaterialcode(materialbyMaterialId.getMaterialcode());
								scminventoryitem.setMaterialname(materialbyMaterialId.getMaterialname());
								scminventoryitem.setQty(outQty.toString());
								scminventoryitem.setUomid(materialstockByMaterialid.get(0).getStockuomid());
								scminventoryitem.setFromid(scmOutSourceToOutStockModel.getFromid());
								scminventoryitem.setFromitemid(scmOutSourceToOutStockModel.getFromitemid());
								scminventoryitem.setBatchid("");
								scminventoryitem.setBatchcode("");
								scminventoryitem.setStocktypeid("S01");
								scminventoryitem.setInventorykindid("01");
								scminventoyitemMapper.insert(scminventoryitem);
							}
						}else{
							//没有批号信息，则自动生成全部批号为空的数据
							Scminventoryitem scminventoryitem=new Scminventoryitem();
							scminventoryitem.setInstock("false");
							scminventoryitem.setOutstock("true");
							scminventoryitem.setParentid(parentid);
							scminventoryitem.setMaterialid(scmOutSourceToOutStockModel.getMaterialid());
							scminventoryitem.setMaterialcode(materialbyMaterialId.getMaterialcode());
							scminventoryitem.setMaterialname(materialbyMaterialId.getMaterialname());
							scminventoryitem.setQty(scmOutSourceToOutStockModel.getOutqty());
							scminventoryitem.setUomid(materialstockByMaterialid.get(0).getStockuomid());
							scminventoryitem.setFromid(scmOutSourceToOutStockModel.getFromid());
							scminventoryitem.setFromitemid(scmOutSourceToOutStockModel.getFromitemid());
							scminventoryitem.setBatchid("");
							scminventoryitem.setBatchcode("");
							scminventoryitem.setStocktypeid("S01");
							scminventoryitem.setInventorykindid("01");
							scminventoyitemMapper.insert(scminventoryitem);
						}
					}
					else{
						//不管理批次
						Scminventoryitem scminventoryitem=new Scminventoryitem();
						scminventoryitem.setInstock("false");
						scminventoryitem.setOutstock("true");
						scminventoryitem.setParentid(parentid);
						scminventoryitem.setMaterialid(scmOutSourceToOutStockModel.getMaterialid());
						scminventoryitem.setMaterialcode(materialbyMaterialId.getMaterialcode());
						scminventoryitem.setMaterialname(materialbyMaterialId.getMaterialname());
						scminventoryitem.setQty(scmOutSourceToOutStockModel.getOutqty());
						scminventoryitem.setUomid(materialstockByMaterialid.get(0).getStockuomid());
						scminventoryitem.setFromid(scmOutSourceToOutStockModel.getFromid());
						scminventoryitem.setFromitemid(scmOutSourceToOutStockModel.getFromitemid());
						scminventoryitem.setBatchid("");
						scminventoryitem.setBatchcode("");
						scminventoryitem.setStocktypeid("S01");
						scminventoryitem.setInventorykindid("01");
						scminventoyitemMapper.insert(scminventoryitem);
					}
				}

			}
		}
		return false;

	}

	@Override
	public void WriteBackInstockFromPurinvoice(JSONObject jsonObject) {
		//todo 先回写采购入库单的已开票数量
		if(jsonObject==null){
			throw  new RuntimeException("回写错误，请寻找管理员");
		}
		try {
			String itemid = (String) jsonObject.get("itemid");
			String invoiceqty = jsonObject.get("invoiceqty").toString();
			String taxinpice = jsonObject.get("taxinprice").toString();

			Scminventoryitem scminventoryitem = scminventoyitemMapper.selectById(itemid);
			String oldInvoiceQty = scminventoryitem.getInvoiceqty();
			BigDecimal add = ERPUtils.getBigDecimal(oldInvoiceQty).add(ERPUtils.getBigDecimal(invoiceqty));
			if(add.compareTo(ERPUtils.getBigDecimal(scminventoryitem.getQty())) == 1){
				throw new RuntimeException("不允许超量开票");
			}
			scminventoryitem.setInvoiceqty(add.toString());
			if(add.compareTo(BigDecimal.ZERO) == 0){
				scminventoryitem.setInvoiceflag("0");
			}
			if(add.compareTo(ERPUtils.getBigDecimal(scminventoryitem.getQty())) ==-1){
				scminventoryitem.setInvoiceflag("1");
			}else if(add.compareTo(ERPUtils.getBigDecimal(scminventoryitem.getQty())) == 0 ){
				scminventoryitem.setInvoiceflag("2");
			}
			scminventoyitemMapper.updateById(scminventoryitem);

			double v = scminventoyitemMapper.selectByMainId(scminventoryitem.getParentid()).stream().mapToInt(item -> {
				return Integer.parseInt(item.getInvoiceflag());
			}).average().orElse(0);

			Scminventoy scminventoy = scminventoyMapper.selectById(scminventoryitem.getParentid());
			scminventoy.setInvoiceflag(ERPUtils.convertDoubleToString(v));

			scminventoyMapper.updateById(scminventoy);

			//todo 处理存货凭证；
			if(scminventoy.getInvouncherflag().equalsIgnoreCase("1")){
				JSONObject vouncherObject=new JSONObject();
				vouncherObject.put("fromitemid",scminventoryitem.getId());
				vouncherObject.put("invoiceqty",scminventoryitem.getInvoiceqty());
				vouncherObject.put("taxinprice",taxinpice);
				mvouncherApi.writeBackMvouncherFromInstockApi(vouncherObject);
			}
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
}
