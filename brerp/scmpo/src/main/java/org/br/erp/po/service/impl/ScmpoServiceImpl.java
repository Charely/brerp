package org.br.erp.po.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.base.Constant.ERPStatus;
import org.br.erp.base.entity.Material;
import org.br.erp.base.entity.Scmbarcode;
import org.br.erp.base.entity.Scmpartner;
import org.br.erp.base.service.*;
import org.br.erp.base.service.writebackapi.IGetVendorPo;
import org.br.erp.base.service.writebackapi.writebackpr;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.entity.po.ScmVendorEntity;
import org.br.erp.po.entity.Scmmatreceiptreq;
import org.br.erp.po.entity.Scmmatreceiptreqitem;
import org.br.erp.po.entity.Scmpo;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.mapper.ScmpoMapper;
import org.br.erp.po.mapper.ScmpoitemMapper;
import org.br.erp.po.service.IScmmatreceiptreqService;
import org.br.erp.po.service.IScmpoService;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.br.erp.base.controller.ScmbarcodeController.BASE64_PRE;

/**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@Service
public class ScmpoServiceImpl extends ServiceImpl<ScmpoMapper, Scmpo>
		implements IScmpoService {

	@Autowired
	private ScmpoMapper scmpoMapper;
	@Autowired
	private ScmpoitemMapper scmpoitemMapper;

	@Autowired
	private IErpBaseService erpBaseService;

	@Autowired
	private IMaterialService materialService;
	@Autowired
	IScmbillcoderuleService scmbillcoderuleService;

	@Autowired
	IScmbarcodeService scmbarcodeService;

	@Autowired
	writebackpr writebackpr;


	@Autowired
	IScmpartnerService scmpartnerService;

	@Override
	public List<ScmMatReceiptReferPoVo> getReferList(IPage<ScmMatReceiptReferPoVo> page, Wrapper<ScmMatReceiptReferPoVo> queryWrapper) {

		return scmpoMapper.getReferList(page,queryWrapper);
	}

	@Override
	public List<ScmMatReceiptReferPoVo> getPuinvoiceReferPoList(IPage<ScmMatReceiptReferPoVo> page, Wrapper<ScmMatReceiptReferPoVo> queryWrapper) {
		return scmpoMapper.getPuinvoiceReferPoList(page,queryWrapper);
	}

	@Override
	public List<ScmVendorReferPoVo> getvendorReferlist(IPage<ScmVendorReferPoVo> page, Map queryMap) {
		QueryWrapper<ScmVendorReferPoVo> scmVendorReferPoVoQueryWrapper=new QueryWrapper<>();

		if(queryMap.containsKey("pocode")){
			scmVendorReferPoVoQueryWrapper.eq("billcode",queryMap.get("pocode").toString());
		}

		if(queryMap.containsKey("vendorid")){
			String[] vendorids=(String[])queryMap.get("vendorid");
			scmVendorReferPoVoQueryWrapper.eq("vendorid",vendorids[0]);
		}

		if(queryMap.containsKey("createTimeRange[]")){
			String[] timelink=(String[])queryMap.get("createTimeRange[]");
			scmVendorReferPoVoQueryWrapper.between("billdate",timelink[0],timelink[1]);
		}



		scmVendorReferPoVoQueryWrapper.orderByDesc("billcode");

		List<ScmVendorReferPoVo> scmVendorReferPoVos = scmpoMapper.getvendorReferlist(page, scmVendorReferPoVoQueryWrapper);

		for (ScmVendorReferPoVo scmVendorReferPoVo : scmVendorReferPoVos) {
			List<Scmbarcode> barcodes = scmbarcodeService.getBarcodes(scmVendorReferPoVo.getItemid());
			if(barcodes==null || barcodes.size()==0){
				scmVendorReferPoVo.setImgsrc("");
			}else{
				Scmbarcode scmbarcode = barcodes.get(0);
				byte[] barcode = scmbarcode.barcode;
				if(barcode!=null){
					String s = Base64.getEncoder().encodeToString(barcode);
					s.replaceAll("\n","").replaceAll("\r","");
					scmVendorReferPoVo.setImgsrc(BASE64_PRE+s);
					scmVendorReferPoVo.setZtqty(scmbarcode.getBartext());
					continue;
				}
			}
		}

		return scmVendorReferPoVos;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Map saveMap) {
		Scmpo scmpo = new Scmpo();
		Map scmret = ERPUtils.mapToClass(saveMap, Scmpo.class, "scmpo");
		if (scmret != null && scmret.containsKey("ret")) {
			Object ret = scmret.get("ret");
			scmpo = (Scmpo) ret;
		}
		scmpo.setStatus(ERPStatus.Create.getCode());

		//获取单据编号字段
		String scmpobillcode = scmbillcoderuleService.getBillCode("scmpo");
		scmpo.setBillcode(scmpobillcode);

		scmpoMapper.insert(scmpo);

		if (scmret.containsKey("cusret")) {
			Object cusret = scmret.get("cusret");
			Map<Object, Object> customdata = (Map<Object, Object>) cusret;
			erpBaseService.saveCustomFieldValues(customdata, "scmpo", scmpo.getId());
		}

		saveOrUpdateItemData(saveMap,scmpo.getId());
	}



	@Transactional
	private void saveOrUpdateItemData(Map pageMap, String id) {
		List<Scmpoitem> scmpoitems = new ArrayList<>();
		for (Object o : pageMap.keySet()) {
			if (pageMap.get(o) !=null && pageMap.get(o).getClass().equals(ArrayList.class)) {
				ArrayList poItems = (ArrayList) pageMap.get(o);
				for (Object poitem : poItems) {
					Map scmpoitem = ERPUtils.mapToClass((Map) poitem, Scmpoitem.class, "scmpoitem");
					if (scmpoitem.containsKey("ret")) {
						Scmpoitem curItem = (Scmpoitem) scmpoitem.get("ret");
						if (curItem != null) {
							//判断是否是退货订单
							Scmpo scmpo = scmpoMapper.selectById(id);
							curItem.setParentid(id);
							curItem.setReceiptreqflag("0");
							curItem.setReceiptflag("0");
							curItem.setIsred(scmpo.getIsred());
							scmpoitemMapper.insert(curItem);
							if (scmpoitem.containsKey("cusret")) {
								erpBaseService.saveCustomFieldValues((Map<Object, Object>) scmpoitem.get("cusret"), "scmpoitem", curItem.getId());
							}

							if(curItem.getFromitemid()!=null && !curItem.getFromitemid().equalsIgnoreCase("")){
								writebackpr.updatepritempoflag(curItem.getFromitemid(), curItem.getQty().toString(),"1");

								if(scmpo.getIsred()!=null && scmpo.getIsred().equalsIgnoreCase("1")){
									//回写蓝单订单的退货数量
									String fromitemid = curItem.getFromitemid();
									Scmpoitem frompoitem = scmpoitemMapper.selectById(fromitemid);
									String returnqty = frompoitem.getReturnqty();
									if(returnqty.equalsIgnoreCase("") || returnqty.equalsIgnoreCase(null)){
										returnqty="0";
									}
									String newReturnQty = new BigDecimal(returnqty).add(curItem.getQty()).toString();
									frompoitem.setReturnqty(newReturnQty);
									if(ERPUtils.bigthenothers(frompoitem.getReturnqty(),frompoitem.getQty().toString())){
										frompoitem.setReturnflag("2");
									}else{
										frompoitem.setReturnflag("1");
									}

									if(new BigDecimal(frompoitem.getReturnqty()).compareTo(frompoitem.getQty()) ==1){
										throw new RuntimeException("不允许超订单退货");
									}
									if(frompoitem.getReturnqty().equalsIgnoreCase("0")){
										frompoitem.setReturnflag("0");
									}
									scmpoitemMapper.updateById(frompoitem);

									//更新表头的退货标志
									double headreturnFlag = scmpoitemMapper.selectByMainId(frompoitem.getParentid()).stream().map(item -> {
										return Integer.parseInt(item.getReturnflag());
									}).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).average().orElse(0);

									Scmpo scmfrompo = scmpoMapper.selectById(frompoitem.getParentid());
									scmfrompo.setReturnflag(ERPUtils.convertDoubleToString(headreturnFlag));
									scmpoMapper.updateById(scmfrompo);
								}
							}

//							if (!curItem.getFromitemid().equalsIgnoreCase("")) {
//								scmpreqitemService.updatePrStatue(curItem.getFromitemid(),curItem.getId());
//							}
						}
					}

				}
			}
		}
	}



	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Map scmpoMap) {

		Scmpo scmpo=new Scmpo();

		Map scmret = ERPUtils.mapToClass(scmpoMap, Scmpo.class, "scmpo");
		if (scmret != null && scmret.containsKey("ret")) {
			Object ret = scmret.get("ret");
			scmpo = (Scmpo) ret;
		}
		scmpoMapper.updateById(scmpo);

		String id = scmpo.getId();
		if (scmret.containsKey("cusret")) {
			Object cusret = scmret.get("cusret");
			Map<Object, Object> customdata = (Map<Object, Object>) cusret;
			erpBaseService.saveCustomFieldValues(customdata, "scmpo", id);
		}

		//1.先删除子表数据
		scmpoitemMapper.deleteByMainId(id);

		saveOrUpdateItemData(scmpoMap, id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		//删除前先把对应的采购计划的状态更新为没生成
		List<Scmpoitem> scmpoitems = scmpoitemMapper.selectByMainId(id);
		for (Scmpoitem scmpoitem : scmpoitems) {
			erpBaseService.updateTableStatus("scmpritem","ispo","0",
					"id",scmpoitem.getFromitemid());
		}
		scmpoitemMapper.deleteByMainId(id);
		scmpoMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmpoitemMapper.deleteByMainId(id.toString());
			scmpoMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updatePoStatus(List<String> ids, String statusflag) {
		QueryWrapper<Scmpo> queryWrapper=new QueryWrapper<>();
		queryWrapper.ne("status",statusflag);
		//queryWrapper.eq("receiptreqflag","0");
		queryWrapper.and(item->item.eq("receiptreqflag","0").or(rq->rq.eq("receiptreqflag",null)));
		//queryWrapper.eq("receiptflag","0");
		queryWrapper.and(item->item.eq("receiptflag","0").or(rq->rq.eq("receiptflag",null)));
		queryWrapper.in("id",ids);
		List<Scmpo> scmpos = scmpoMapper.selectList(queryWrapper);
		scmpos.stream().forEach(item->{
			item.setStatus(statusflag);
			scmpoMapper.updateById(item);
		});
	}

	@Override
	public ScmVendorEntity getVendorList(String barid) {
		Scmbarcode scmbarcode = scmbarcodeService.getById(barid);
		Scmpoitem scmpoitem = scmpoitemMapper.selectById(scmbarcode.getBillitemid());
		Scmpo scmpo = scmpoMapper.selectById(scmpoitem.getParentid());
		Material material = materialService.getMaterialbyMaterialId(scmpoitem.getMaterialid());
		Scmpartner scmpartner = scmpartnerService.getById(scmpo.getVendorid());
		ScmVendorEntity res=new ScmVendorEntity();
		res.setMaterialcode(material.getMaterialcode());
		res.setMaterialname(material.getMaterialname());
		res.setVendorcode(scmpartner.getPartnercode());
		res.setVendorname(scmpartner.getPartnername());
		res.setNeedreceiptqty(scmbarcode.getBartext());
		res.setBarid(barid);
		return res;
	}
}
