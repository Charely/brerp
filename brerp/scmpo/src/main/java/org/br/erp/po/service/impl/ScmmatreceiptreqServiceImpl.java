package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.base.Config.EventHandler;
import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;
import org.br.erp.base.Config.MyGreenEventBusHandler;
import org.br.erp.base.entity.Scmbarcode;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.service.IScmbarcodeService;
import org.br.erp.base.service.IScmbatchService;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.writebackpo;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.base.vo.MaterialVo;
import org.br.erp.po.entity.Scmmatreceiptreq;
import org.br.erp.po.entity.Scmmatreceiptreqitem;
import org.br.erp.po.entity.Scmpo;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.mapper.ScmmatreceiptreqitemMapper;
import org.br.erp.po.mapper.ScmmatreceiptreqMapper;
import org.br.erp.po.service.IScmmatreceiptreqService;
import org.br.erp.po.service.IScmpoService;
import org.br.erp.po.service.IScmpoitemService;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmReferMatreceiptreqVo;
import org.checkerframework.checker.units.qual.A;
import org.greenrobot.eventbus.EventBus;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.base.objectEntity.Scmbillcoderule;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 收料申请单
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@Service
public class ScmmatreceiptreqServiceImpl extends ServiceImpl<ScmmatreceiptreqMapper, Scmmatreceiptreq>
		implements IScmmatreceiptreqService, writebackpo {

	@Autowired
	private ScmmatreceiptreqMapper scmmatreceiptreqMapper;
	@Autowired
	private ScmmatreceiptreqitemMapper scmmatreceiptreqitemMapper;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;

	@Autowired
	private IScmpoitemService scmpoitemService;

	@Autowired
	private IScmpoService scmpoService;

	@Autowired
	private IScmbarcodeService scmbarcodeService;

	@Autowired
	IScmbatchService scmbatchService;

	@Autowired
	IMaterialService materialService;

	@Autowired
	MyGreenEventBusHandler eventBusHandler;

	@Autowired
	RedisUtil redisUtil;

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmmatreceiptreq scmmatreceiptreq, List<Scmmatreceiptreqitem> scmmatreceiptreqitemList) {
		String billCode = scmbillcoderuleService.getBillCode("scmmatreceiptreq");
		scmmatreceiptreq.setBillcode(billCode);
		scmmatreceiptreqMapper.insert(scmmatreceiptreq);
		if(scmmatreceiptreqitemList!=null && scmmatreceiptreqitemList.size()>0) {
			for(Scmmatreceiptreqitem entity:scmmatreceiptreqitemList) {
				//外键设置
				entity.setParentid(scmmatreceiptreq.getId());


				//todo 处理批次信息
				//如果管理批次，并且已经录入了批次信息，则将批次信息同步到批次档案里，
				MaterialVo materialVoInfoByMaterialID = materialService.getMaterialVoInfoByMaterialID(entity.getMaterialid());
				if(materialVoInfoByMaterialID.getIsbatch()!=null && materialVoInfoByMaterialID.getIsbatch().equalsIgnoreCase("true")){
					//进行批次管理
					String batchCode="";
					if(entity.getBatchcode()!=null && !entity.getBatchcode().equalsIgnoreCase("")){
						batchCode = entity.getBatchcode();
					}
					if(entity.getVendorbatchcode()!=null && !entity.getVendorbatchcode().equalsIgnoreCase("")){
						if(entity.getBatchenddate() == null || entity.getBatchstartdate() == null){
							throw new RuntimeException("物料进行批次管理，请输入批次有效期");
						}
						if(entity.getWarehouseid()==null || entity.getWarehouseid().equalsIgnoreCase("")){
							throw new RuntimeException("该物料管理批次，请输入仓库");
						}
						Date batchstartdate = entity.getBatchstartdate();
						Date batchenddate = entity.getBatchenddate();

						Map map = scmbatchService.saveBatchinfo(scmmatreceiptreq.getCompanyid(),
								entity.getWarehouseid(),
								entity.getMaterialid(), entity.getQty().toString(), scmmatreceiptreq.getVendorid(), entity.getVendorbatchcode(),
								ERPUtils.getDateString(entity.getBatchstartdate(),""),
								ERPUtils.getDateString(entity.getBatchenddate(),""),
								batchCode);
						if(map!=null){
							if(map.containsKey("batchid")){
								entity.setBatchid(map.get("batchid").toString());
							}
							if(map.containsKey("batchcode")){
								entity.setBatchcode(map.get("batchcode").toString());
							}
						}
					}
				}
				scmmatreceiptreqitemMapper.insert(entity);
				writeBackReferBillitem(entity,false);
			}
		}
	}

	private void writeBackReferBillitem(Scmmatreceiptreqitem entity,Boolean isdeleteflag) {
		if (entity.getFromtype() != null && entity.getFromtype().equalsIgnoreCase("po")) {
			afterSaveToPo(entity.getFromitemid(), entity.getQty(), isdeleteflag);
		} else if (entity.getFromtype() != null && entity.getFromtype().equalsIgnoreCase("outsource")) {
			afterSaveToOutSource(entity.getFromitemid(), entity.getQty(), isdeleteflag);
		}
	}


	void afterSaveToPo(String fromitemid, BigDecimal reqqty,boolean isdeletflag){
		//如果是参照采购订单来的，回写采购订单
		scmpoitemService.writebackfrompo(fromitemid,reqqty,isdeletflag);
	}


	void afterSaveToOutSource(String fromitemid,BigDecimal reqQty,boolean isdeletfalg){
//		try {
////			eventHandler.receiptReqWriteBackOutSourceQty(ReceiptWriteBackWwMessage.builder().isdelete(isdeletfalg)
////					.outsourceitemid(fromitemid)
////					.receiptreqqty(reqQty.toString()).build());
//
//
//			//throw new RuntimeException("sssss");
//		}catch(Exception e){
//			throw new RuntimeException(e.getMessage());
//		}

		log.error("log error:"+Thread.currentThread().getName());
		ReceiptWriteBackWwMessage build = ReceiptWriteBackWwMessage.builder().isdelete(isdeletfalg)
				.outsourceitemid(fromitemid)
				.receiptreqqty(reqQty.toString()).build();

		eventBusHandler.receiptReqWriteBackOutSourceQty(build);
		String errorKey=build.getOutsourceitemid()+","+build.getReceiptreqqty();
		if(redisUtil.hasKey(errorKey)) {
			String error = redisUtil.get(errorKey).toString();
			if (error != null && !error.equalsIgnoreCase("")) {
				redisUtil.del("error");
				throw new RuntimeException(error);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmmatreceiptreq scmmatreceiptreq,List<Scmmatreceiptreqitem> scmmatreceiptreqitemList) {
		scmmatreceiptreqMapper.updateById(scmmatreceiptreq);

		//删除之前先更新订单分录的数据

		//1.先删除子表数据
		scmmatreceiptreqitemMapper.deleteByMainId(scmmatreceiptreq.getId());
		for (Scmmatreceiptreqitem scmmatreceiptreqitem : scmmatreceiptreqitemList) {

			writeBackReferBillitem(scmmatreceiptreqitem,true);

			//2.子表数据重新插入
			if (scmmatreceiptreqitemList != null && scmmatreceiptreqitemList.size() > 0) {
				for (Scmmatreceiptreqitem entity : scmmatreceiptreqitemList) {
					//外键设置
					entity.setParentid(scmmatreceiptreq.getId());
					scmmatreceiptreqitemMapper.insert(entity);
					writeBackReferBillitem(entity,false);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		//删除前，先将参照信息去
		scmmatreceiptreqitemMapper.selectByMainId(id).forEach(item->{
			writeBackReferBillitem(item,true);
		});
		scmmatreceiptreqitemMapper.deleteByMainId(id);
		scmmatreceiptreqMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmmatreceiptreqitemMapper.deleteByMainId(id.toString());
			scmmatreceiptreqMapper.deleteById(id);
		}
	}

	@Override
	public List<ScmReferMatreceiptreqVo> getReferMatreceipreqVo(IPage<ScmReferMatreceiptreqVo> page, Map queryMap) {

		QueryWrapper<ScmReferMatreceiptreqVo> queryWrapper=new QueryWrapper<>();
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"billcode")){
			String billcode = ERPUtils.getHttpReqParam(queryMap, "billcode");
			queryWrapper.like("head.billcode",billcode);
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"vendorid")){
			String vendorid = ERPUtils.getHttpReqParam(queryMap, "vendorid");
			queryWrapper.like("head.vendorid",vendorid).or().eq(vendorid,"");
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"companyid")){
			queryWrapper.eq("head.companyid",ERPUtils.getHttpReqParam(queryMap,"companyid"));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"warehouseid")){
			queryWrapper.eq("item.warehouseid",ERPUtils.getHttpReqParam(queryMap,"warehouseid"))
					.or().eq(ERPUtils.getHttpReqParam(queryMap,"warehouseid"),"");
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"materialparam")){
			String materialparma = ERPUtils.getHttpReqParam(queryMap, "materialparma");
			queryWrapper.like("material.materialcode",materialparma).or().like("material.materialname",materialparma);
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"begintime") && ERPUtils.ifHtppReqParamContainKey(queryMap,"endtime")){
			String begintime = ERPUtils.getHttpReqParam(queryMap, "begintime");
			String endtime = ERPUtils.getHttpReqParam(queryMap, "endtime");
			queryWrapper.between("head.billdate",begintime,endtime);
		}
		queryWrapper.eq("head.status","2");
		queryWrapper.orderByDesc("billcode");

		List<ScmReferMatreceiptreqVo> referMatreceiptList = scmmatreceiptreqMapper.getReferMatreceiptList(page, queryWrapper);
		return referMatreceiptList;
	}

	@Override
	public void updatePoStatus(List<String> ids, String statusflag) {
		QueryWrapper<Scmmatreceiptreq> queryWrapper=new QueryWrapper<>();
		queryWrapper.ne("status",statusflag);
		queryWrapper.and(item->item.eq("receiptflag","0").or(rq->rq.eq("receiptflag",null)));
		queryWrapper.in("id",ids);
		List<Scmmatreceiptreq> scmpos = scmmatreceiptreqMapper.selectList(queryWrapper);
		scmpos.stream().forEach(item->{
			item.setStatus(statusflag);
			scmmatreceiptreqMapper.updateById(item);
		});
	}

	@Override
	public void updatepoinstockqty(String poitemid, String instockqty) {

	}

	@Override
	@Transactional
	public void updatematreceiptinstockqty(String matreceiptreqitemid, String instockqty) {
		Scmmatreceiptreqitem scmmatreceiptreqitem = scmmatreceiptreqitemMapper.selectById(matreceiptreqitemid);
		if(scmmatreceiptreqitem==null)
		{
			return;
		}
		BigDecimal qty = scmmatreceiptreqitem.getQty();
		BigDecimal receiptqty = scmmatreceiptreqitem.getReceiptqty();
		String newqty = receiptqty.add(new BigDecimal(instockqty)).toString();
		if(ERPUtils.bigthenothers(newqty,qty.toString())){
			scmmatreceiptreqitem.setReceiptflag("2");
		}else{
			scmmatreceiptreqitem.setReceiptflag("1");
		}
		if(new BigDecimal(newqty).compareTo(new BigDecimal(0))==0){
			scmmatreceiptreqitem.setReceiptflag("0");
		}
		if(new BigDecimal(newqty).compareTo(qty)>0){
			throw new RuntimeException("入库数量超过已收货数量，不允许进行入库!");
		}
		scmmatreceiptreqitem.setReceiptqty(new BigDecimal(newqty));
		scmmatreceiptreqitemMapper.updateById(scmmatreceiptreqitem);

		double avgvalue = scmmatreceiptreqitemMapper.selectByMainId(scmmatreceiptreqitem.getParentid()).stream().map(item -> {
			return Integer.parseInt(item.getReceiptflag());
		}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);


		Scmmatreceiptreq scmmatreceiptreq = scmmatreceiptreqMapper.selectById(scmmatreceiptreqitem.getParentid());
		scmmatreceiptreq.setReceiptflag(String.valueOf(avgvalue));
		scmmatreceiptreqMapper.updateById(scmmatreceiptreq);
	}

	@Override
	@Transactional
	public void updatepoitemreturnqty(String poitemid, String returnqty) {
		Scmpoitem scmpoitem = scmpoitemService.getById(poitemid);

		String oldreceiptqty = scmpoitem.getReceiptqty();
		BigDecimal newReceiptQty = new BigDecimal(oldreceiptqty).add(new BigDecimal(returnqty));
		scmpoitem.setReceiptqty(newReceiptQty.toString());

		if(ERPUtils.bigthenothers(scmpoitem.getReceiptqty(),scmpoitem.getQty().toString())){
			if(ERPUtils.stringequals(scmpoitem.getReceiptqty(),scmpoitem.getQty().toString())){
				scmpoitem.setReceiptflag("2");
			}else {
				throw new RuntimeException("不允许超订单数量退货");
			}
		}else {
			scmpoitem.setReceiptflag("1");
		}
		if(scmpoitem.getReceiptqty().equalsIgnoreCase("0")){
			scmpoitem.setReceiptflag("0");
		}
		scmpoitemService.updateById(scmpoitem);
		double headflag = scmpoitemService.selectByMainId(scmpoitem.getParentid()).stream().map(item -> {
			return Integer.parseInt(item.getReceiptflag());
		}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);

		Scmpo scmpo = scmpoService.getById(scmpoitem.getParentid());
		scmpo.setReceiptflag(ERPUtils.convertDoubleToString(headflag));
		scmpoService.updateById(scmpo);
	}

	@Override
	@Transactional
	public void updatepoiteminvoiceqty(String poitemid, String invoiceqty, Boolean isdelete) {
		Scmpoitem scmpoitem = scmpoitemService.getById(poitemid);
		String curInvoiceqty = scmpoitem.getInvoiceqty();
		BigDecimal newInvoiceQty=BigDecimal.ZERO;
		invoiceqty=invoiceqty.replaceAll("-","");
		if(isdelete){
			//删除
			newInvoiceQty=new BigDecimal(curInvoiceqty).subtract(new BigDecimal(invoiceqty));
		}else{
			newInvoiceQty=new BigDecimal(curInvoiceqty).add(new BigDecimal(invoiceqty));
		}

		if(newInvoiceQty.compareTo(scmpoitem.getQty()) == 1) {
			throw new RuntimeException("不允许超量开票");
		}else if(newInvoiceQty.compareTo(scmpoitem.getQty()) == 0){
			scmpoitem.setInvoiceflag("2");
		} else if(newInvoiceQty.compareTo(scmpoitem.getQty()) == -1){
			scmpoitem.setInvoiceflag("1");
		}
		if(newInvoiceQty.compareTo(BigDecimal.ZERO) == 0){
			scmpoitem.setInvoiceflag("0");
		}
		scmpoitem.setInvoiceqty(newInvoiceQty.toString());
		scmpoitemService.updateById(scmpoitem);

		double v = scmpoitemService.selectByMainId(scmpoitem.getParentid()).stream().map(item -> {
			return Integer.parseInt(item.getInvoiceflag());
		}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);

		Scmpo scmpo = scmpoService.getById(scmpoitem.getParentid());
		scmpo.setInvoiceflag(ERPUtils.convertDoubleToString(v));
		scmpoService.updateById(scmpo);
	}

	@Transactional
	@Override
	public void saveBarCodeVendorInfo(List<String> barids) {
		HashMap<String,Scmpo> needToSavepo=new HashMap<>();
		List<Scmmatreceiptreqitem> scmmatreceiptreqitemList=new ArrayList<>();
		Scmmatreceiptreq scmmatreceiptreq=new Scmmatreceiptreq();
		String vendorid="";
		for(String item : barids) {
			Scmbarcode scmbarcode = scmbarcodeService.getById(item);
			if(scmbarcode.getIsvalid()!=null && scmbarcode.getIsvalid().equalsIgnoreCase("1")){
				throw new RuntimeException("二维码已失效");
			}
			String billitemid = scmbarcode.getBillitemid();
			Scmpo scmpo = scmpoService.getById(scmbarcode.getBillid());
			if (vendorid.equalsIgnoreCase("")) {
				vendorid = scmpo.getVendorid();
			}
			Scmpoitem scmpoitem = scmpoitemService.getById(billitemid);
			Scmmatreceiptreqitem scmmatreceiptreqitem = new Scmmatreceiptreqitem();
			scmmatreceiptreqitem.setMaterialid(scmpoitem.getMaterialid());
			scmmatreceiptreqitem.setQty(new BigDecimal(scmbarcode.getBartext()));
			scmmatreceiptreqitem.setUomid(scmpoitem.getUomid());
			scmmatreceiptreqitem.setFromid(scmpoitem.getParentid());
			scmmatreceiptreqitem.setFromitemid(scmpoitem.getId());
			scmmatreceiptreqitemList.add(scmmatreceiptreqitem);
			scmbarcode.setIsvalid("1");
			scmbarcodeService.updateById(scmbarcode);

		};

//		String billcode=scmbillcoderuleService.getBillCode("scmmatreceiptreq");
//		scmmatreceiptreq.setBillcode(billcode);
		scmmatreceiptreq.setBilldate(ERPUtils.getNowDate());
		scmmatreceiptreq.setVendorid(vendorid);

		saveMain(scmmatreceiptreq,scmmatreceiptreqitemList);

	}
}
