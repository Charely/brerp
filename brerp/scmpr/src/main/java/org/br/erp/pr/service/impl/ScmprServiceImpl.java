package org.br.erp.pr.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hpsf.Decimal;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.aspectj.weaver.tools.MatchingContext;
import org.br.erp.base.service.IErpBaseService;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.service.writebackapi.writebackpr;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.po.entity.*;
import org.br.erp.po.mapper.*;
import org.br.erp.po.service.IMaterialvendorlinkService;
import org.br.erp.po.service.IMaterialvendorlinkitemService;
import org.br.erp.pr.entity.Scmpr;
import org.br.erp.pr.entity.Scmpritem;
import org.br.erp.pr.mapper.ScmprMapper;
import org.br.erp.pr.mapper.ScmpritemMapper;
import org.br.erp.pr.service.IScmprService;
import org.br.erp.pr.service.IScmpreqitemService;
import org.br.erp.pr.vo.ScmPoReferPrVo;
import org.br.erp.pr.vo.ScmprPage;
import org.br.erp.pr.vo.ScmprReferVo;
import org.br.erp.price.mapper.PricelineMapper;
import org.br.erp.price.service.IPricelineService;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description: 采购计划
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
@Service("scmprservice")
public class ScmprServiceImpl extends ServiceImpl<ScmprMapper, Scmpr> implements
		IScmprService, writebackpr {

	@Autowired
	private ScmprMapper scmprMapper;
	@Autowired
	private ScmpritemMapper scmpritemMapper;
	@Autowired
	private ScmpoMapper scmpoMapper;
	@Autowired
	private ScmpoitemMapper scmpoitemMapper;

	@Autowired
	private IPricelineService pricelineService;

//	@Autowired
//	private IMaterialService materialService;

	@Autowired
	private IScmpreqitemService scmpreqitemService;

	@Autowired
	public RedisUtil redisUtil;


	@Autowired
	private IMaterialvendorlinkService materialvendorlinkService;


	@Autowired
	private MaterialvendorlinkMapper materialvendorlinkMapper;
	@Autowired
	private MaterialvendorlinkitemMapper materialvendorlinkitemMapper;

	@Autowired
	private ScmlinkqtyMapper scmlinkqtyMapper;

	@Autowired
	private ScmlinkqtyitemMapper scmlinkqtyitemMapper;


	@Autowired
	private IErpBaseService erpBaseService;

	@Autowired
	IScmbillcoderuleService scmbillcoderuleService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Map pageMap) {
		//通过Map 获取前台的值
		Scmpr scmpr = new Scmpr();
		Map scmret = ERPUtils.mapToClass(pageMap, Scmpr.class, "scmpr");
		if (scmret != null && scmret.containsKey("ret")) {
			Object ret = scmret.get("ret");
			scmpr = (Scmpr) ret;
		}
		scmpr.setStatus("0");
		//获取单据编号字段
		String scmprbillcode = scmbillcoderuleService.getBillCode("scmpr");
		scmpr.setPrcode(scmprbillcode);
		scmprMapper.insert(scmpr);
		//获取到新的id
		String id = scmpr.getId();
		if (scmret.containsKey("cusret")) {
			Object cusret = scmret.get("cusret");
			Map<Object, Object> customdata = (Map<Object, Object>) cusret;
			erpBaseService.saveCustomFieldValues(customdata, "scmpr", id);
		}
		saveOrUpdateItemData(pageMap,id);
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Map pageMap) {
		Scmpr scmpr=new Scmpr();

		Map scmret = ERPUtils.mapToClass(pageMap, Scmpr.class, "scmpr");
		if (scmret != null && scmret.containsKey("ret")) {
			Object ret = scmret.get("ret");
			scmpr = (Scmpr) ret;
		}
		scmprMapper.updateById(scmpr);

		String id = scmpr.getId();
		if (scmret.containsKey("cusret")) {
			Object cusret = scmret.get("cusret");
			Map<Object, Object> customdata = (Map<Object, Object>) cusret;
			erpBaseService.saveCustomFieldValues(customdata, "scmpr", id);
		}
		
		//1.先删除子表数据
		scmpritemMapper.deleteByMainId(scmpr.getId());

		saveOrUpdateItemData(pageMap, id);
	}

	@Transactional
	private void saveOrUpdateItemData(Map pageMap, String id) {
		List<Scmpritem> scmpritemList = new ArrayList<>();
		for (Object o : pageMap.keySet()) {
			if (pageMap.get(o) !=null && pageMap.get(o).getClass().equals(ArrayList.class)) {
				ArrayList prItems = (ArrayList) pageMap.get(o);
				for (Object prItem : prItems) {
					Map scmpritem = ERPUtils.mapToClass((Map) prItem, Scmpritem.class, "scmpritem");
					if (scmpritem.containsKey("ret")) {
						Scmpritem curItem = (Scmpritem) scmpritem.get("ret");
						if (curItem != null) {
							curItem.setParentid(id);
							scmpritemMapper.insert(curItem);
							if (scmpritem.containsKey("cusret")) {
								erpBaseService.saveCustomFieldValues((Map<Object, Object>) scmpritem.get("cusret"), "scmpritem", curItem.getId());
							}
							if (curItem.getFromitemid()!=null && !curItem.getFromitemid().equalsIgnoreCase("")) {
								scmpreqitemService.updatePrStatue(curItem.getFromitemid(),curItem.getId());
							}
						}
					}

				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		//删除之前，先把原来关联的申请都更新掉没有被参照过
		List<Scmpritem> scmpritems = scmpritemMapper.selectByMainId(id);
		scmpritems.forEach(item->{
			scmpreqitemService.updatePreqItemStatus(item.getId());
		});
		scmpritemMapper.deleteByMainId(id);
		scmbillcoderuleService.djDeleteButSaveBillCode("scmpr",scmprMapper.selectById(id).getPrcode());
		scmprMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmpritemMapper.deleteByMainId(id.toString());
			scmprMapper.deleteById(id);
		}
	}

	@Override
	public String getPrOrderDate(String materialid, String prqty, String preqdate) {
	//	Material material = materialService.getById(materialid);
		//固定采购周期
		Integer fixedPoDate = 0;
		//日供量
		BigDecimal fixedGl = new BigDecimal(0);
		//根据需求日期来计算订货日期
		BigDecimal qty = new BigDecimal(prqty);

		if(fixedGl.compareTo(BigDecimal.ZERO)>0 && fixedPoDate!=null){
			double time=	 (double)(Math.ceil(qty.divide(fixedGl, 10, RoundingMode.HALF_UP).doubleValue()))+fixedPoDate;
			Calendar cal = Calendar.getInstance();
			//cal.setTime(ERPUtils.getTimestamp(preqdate,""));
			cal.add(Calendar.DAY_OF_MONTH,(int)-time);
			return preqdate;
			//return ERPUtils.getDateString(cal.getTime(),"");
		}else{
			return preqdate;
		}
	}

	@Override
	public IPage<ScmprReferVo> getReferPrVo(IPage<ScmprReferVo> page, Map queryMap) {

		QueryWrapper<ScmprReferVo> queryWrapper=new QueryWrapper<>();
		if(queryMap.containsKey("prcode")){
			String[] preqcode=(String[])queryMap.get("prcode");
			queryWrapper.like("prcode",preqcode[0]);
		}
//		if(queryMap.containsKey("vendorid")){
//			//如果存在供应商，那么就去货源清单中查找
//			String[] vendorid =(String[])queryMap.get("vendorid");
//			List<Materialvendorlink> materialsOfVendor = materialvendorlinkService.getMaterialsOfVendor(vendorid[0]);
//			if(materialsOfVendor!=null && materialsOfVendor.size() >0){
//				for (Materialvendorlink materialvendorlink : materialsOfVendor) {
//					queryWrapper.or(item->item.eq("materialid",materialvendorlink.getMaterialid()));
//				}
//			}else{
//				throw new RuntimeException("请先定义当前供应商的货源清单");
//			}
//		}

		if(queryMap.containsKey("materialparam")){
			String[] materialparam = (String[])queryMap.get("materialparam");
			queryWrapper.like("materialcode",materialparam[0]).or().like("materialname",materialparam[0]);
		}
		if(queryMap.containsKey("createTimeRange[]")){
			String[] timelink=(String[])queryMap.get("createTimeRange[]");
			queryWrapper.between("head.prdate",timelink[0],timelink[1]);
		}
		if(queryMap.containsKey("companyid")){
			String companyid = ERPUtils.getHttpReqParam(queryMap, "companyid");
			queryWrapper.eq("head.companyid",companyid);
		}

		queryWrapper.orderByAsc("item.prorder_date");
		queryWrapper.orderByDesc("head.prcode");
		return page.setRecords(scmprMapper.getPrReferList(page,queryWrapper));
	}

	/**
	 * 根据采购计划生成采购订单
	 * @param ids
	 */
	@Override
	@Transactional
	public void createpo(String ids) {
		//todo 1 获取采购计划的信息
		//String[] split = ids.split(",");
		//List<Scmpr> scmprs = scmprMapper.selectBatchIds(Arrays.asList(split));
		QueryWrapper<Scmpritem> queryWrapper=new QueryWrapper();
		queryWrapper.in("parentid", ids);
		List<Scmpritem> scmpritems = scmpritemMapper.selectList(queryWrapper);
		HashMap<String,List<Scmpritem>> res=new HashMap<>();
		//todo 2 找到货源清单信息
		for (Scmpritem scmpritem : scmpritems) {
			String materialid = scmpritem.getMaterialid();
			QueryWrapper<Materialvendorlink> materialvendorlinkQueryWrapper=new QueryWrapper<>();
			materialvendorlinkQueryWrapper.eq("materialid",materialid);
			materialvendorlinkQueryWrapper.eq("canenable","true");
			List<Materialvendorlink> materialvendorlinks = materialvendorlinkMapper.selectList(materialvendorlinkQueryWrapper);
			if(materialvendorlinks!=null && materialvendorlinks.size()>0){
				//todo 2.1 取第一个符合的货源清单信息
				Materialvendorlink materialvendorlink = materialvendorlinks.get(0);
				QueryWrapper<Materialvendorlinkitem> materialvendorlinkitemQueryWrapper=new QueryWrapper<>();
				materialvendorlinkitemQueryWrapper.eq("parentid",materialvendorlink.getId());
				materialvendorlinkitemQueryWrapper.eq("fixedvendor","1");
				List<Materialvendorlinkitem> materialvendorlinkitems = materialvendorlinkitemMapper.selectList(materialvendorlinkitemQueryWrapper);
				if(materialvendorlinkitems!=null && materialvendorlinkitems.size()>0){
					//todo 2.2 取供应商
					Materialvendorlinkitem materialvendorlinkitem = materialvendorlinkitems.get(0);
					String vendorid = materialvendorlinkitem.getVendorid();
					BigDecimal minqty = materialvendorlinkitem.getMinqty();
					BigDecimal maxqty = materialvendorlinkitem.getMaxqty();
					BigDecimal prqty = new BigDecimal(scmpritem.getQty().toString());
					if(prqty.compareTo(minqty) <0){
						scmpritem.setQty(minqty.toString());
					}
					if(prqty.compareTo(maxqty) >0){
						scmpritem.setQty(maxqty.toString());
					}
					if(res.containsKey(vendorid)){
						List<Scmpritem> sumitmes = res.get(vendorid);
						sumitmes.add(scmpritem);
						res.put(vendorid,sumitmes);
					}else{
						List<Scmpritem> scmpritemList=new ArrayList<>();
						scmpritemList.add(scmpritem);
						res.put(vendorid,scmpritemList);
					}
				}
				else{
					throw new RuntimeException("请定义正确的货源清单信息");
				}
			}
			else{
				throw new RuntimeException("没有定义物料："+scmpritem.getMaterialcode()+"的货源清单信息");
			}
		}

		createPoCommonMethod(res);
	}

	private void createPoCommonMethod(HashMap<String, List<Scmpritem>> res) {
		if(res !=null && res.size()>0){
			//开始合单生成对应的采购订单
			for (String curvendorid : res.keySet()) {
				List<Scmpritem> scmpritemList = res.get(curvendorid);
				Scmpo scmpo=new Scmpo();
				Scmpr scmpr = scmprMapper.selectById(scmpritemList.get(0).getParentid());
				scmpo.setVendorid(curvendorid);
				scmpo.setPodate(ERPUtils.getDateString(new Date(),"yyyy-MM-dd"));
				scmpo.setPodeptid(scmpr.getPrdept());
				scmpo.setPoemptid(scmpr.getPrempid());
				scmpoMapper.insert(scmpo);
				List<Scmpoitem> entities=new ArrayList<>();
				for (Scmpritem scmpritem : scmpritemList) {
					Scmpoitem scmpoitem=new Scmpoitem();
					if(scmpritem.getIspo().equalsIgnoreCase("2")){
						throw  new RuntimeException("已全部生成采购订单，不允许重复生成");
					}
					scmpoitem.setMaterialid(scmpritem.getMaterialid());
					scmpoitem.setMaterialcode(scmpritem.getMaterialcode());
					scmpoitem.setMaterialname(scmpritem.getMaterialname());
					scmpoitem.setQty(new BigDecimal(scmpritem.getQty()));
					scmpoitem.setParentid(scmpo.getId());
					//scmpoitem.setDeliverydate(ERPUtils.getTimestamp(scmpritem.getProrderDate(),"yyyy-MM-dd"));
					scmpoitem.setTaxinprice(new BigDecimal(scmpritem.getTaxinprice()));

					//获取物料价格
					BigDecimal materialPrice = pricelineService.getMaterialPrice("0", scmpritem.getMaterialid(), curvendorid, "");
					scmpoitem.setTaxinprice(materialPrice);

					BigDecimal taxinvalue = materialPrice.multiply(new BigDecimal(scmpritem.getQty())).round(MathContext.DECIMAL32);
					//根据数量计算出价格
					taxinvalue = taxinvalue.divide(new BigDecimal(1), RoundingMode.CEILING);
					scmpoitem.setTaxinvalue(taxinvalue);
					scmpoitem.setFromitemid(scmpritem.getId());
					entities.add(scmpoitem);
					scmpoitemMapper.insert(scmpoitem);
					scmpritem.setIspo("2");
					scmpritemMapper.updateById(scmpritem);
				}
			}
		}
	}

	@Override
	public void createporfromvendoritem(String ids) {
		QueryWrapper<Scmpritem> queryWrapper=new QueryWrapper();
		queryWrapper.in("parentid", ids);
		List<Scmpritem> scmpritems = scmpritemMapper.selectList(queryWrapper);
		HashMap<String,List<Scmpritem>> res=new HashMap<>();
		for (Scmpritem scmpritem : scmpritems) {
			String materialid = scmpritem.getMaterialid();
			//todo 去配额里面查找对应的供应商信息
			HashMap columnMap=new HashMap();
			columnMap.put("materialid",materialid);
			columnMap.put("canenable","Y");
			List<Scmlinkqty> scmlinkqty = scmlinkqtyMapper.selectByMap(columnMap);
			if(scmlinkqty!=null && scmlinkqty.size()==1){
				Scmlinkqty currentScmlinkqty = scmlinkqty.get(0);
				List<Scmlinkqtyitem> scmlinkqtyitems = scmlinkqtyitemMapper.selectByMainId(currentScmlinkqty.getId());
				if(scmlinkqtyitems!=null && scmlinkqtyitems.size()>0){
					for (Scmlinkqtyitem scmlinkqtyitem : scmlinkqtyitems) {
						String vendorid = scmlinkqtyitem.getVendorid();
						Double quotarate = scmlinkqtyitem.getQuotarate();
						Scmpritem currentPrItem=new Scmpritem();
						BeanUtils.copyProperties(scmpritem,currentPrItem);
						String prqty = currentPrItem.getQty();
						BigDecimal divQty = new BigDecimal(prqty).multiply(new BigDecimal(quotarate))
								.divide(BigDecimal.valueOf(100),RoundingMode.CEILING);
						currentPrItem.setQty(divQty.toString());
						if(!res.containsKey(vendorid)){
							List<Scmpritem> currentPritem=new ArrayList<>();
							currentPritem.add(currentPrItem);
							res.put(vendorid,currentPritem);
						}else{
							List<Scmpritem> scmpritemList = res.get(vendorid);
							scmpritemList.add(currentPrItem);
						}
					}
				}else{
					throw new RuntimeException("当前物料"+scmpritem.getMaterialcode()+"的供应商配额信息有错误，请确认");
				}
			}else{
				if(scmlinkqty.size()==0) {
					throw new RuntimeException("不存在当前物料" + scmpritem.getMaterialcode() + "的供应商配额信息");
				}else if(scmlinkqty.size()>1){
					throw new RuntimeException("当前物料"+scmpritem.getMaterialcode()+"存在多个供应商配额信息，请确认");
				}else{
					throw new RuntimeException("不存在当前物料" + scmpritem.getMaterialcode() + "的供应商配额信息");
				}
			}

		}

		createPoCommonMethod(res);
	}

	/**
	 * 查找用于可以生成采购订单的信息
	 * @param querymap
	 * @return
	 */
	@Override
	public List<ScmPoReferPrVo> getPoReferPrList(HashMap querymap) {




		return null;
	}


//	@Override
//	@Transactional
//	public void afterFlowHandle(FlowMyBusiness business) {
//		if(business.getTaskId().isEmpty())
//		{
//			Scmpr scmpr = scmprMapper.selectById(business.getDataId());
//			scmpr.setStatus("2");
//			scmprMapper.updateById(scmpr);
//		}else {
//			Scmpr scmpr = scmprMapper.selectById(business.getDataId());
//			scmpr.setStatus("1");
//			scmprMapper.updateById(scmpr);
//		}
//	}

//	@Override
//	public Object getBusinessDataById(String dataId) {
//		return null;
//	}
//
//	@Override
//	public Map<String, Object> flowValuesOfTask(String taskNameId, Map<String, Object> values) {
//		return null;
//	}
//
//	@Override
//	public List<String> flowCandidateUsernamesOfTask(String taskNameId, Map<String, Object> values) {
//		return null;
//	}

	@Override
	@Transactional
	public void updatepritempoflag(String pritemid,String prqty, String flag) {
		Scmpritem scmpritem = scmpritemMapper.selectById(pritemid);
		if(scmpritem==null)
			return;
		scmpritem.setIspo(flag);

		if(new BigDecimal(prqty).compareTo(new BigDecimal(scmpritem.getQty())) == 1){
			throw new RuntimeException("不允许超计划下订单");
		}
		scmpritemMapper.updateById(scmpritem);
	}
}
