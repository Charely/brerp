package org.br.erp.outsource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.br.erp.base.Config.EventHandler;
import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;
import org.br.erp.base.Config.MyGreenEventBusHandler;
import org.br.erp.base.entity.Materialstock;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.service.writebackapi.IReceiptReqWriteBackOutSource;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.outsource.entity.Scmoutsource;
import org.br.erp.outsource.entity.Scmoutsourceitem;
import org.br.erp.outsource.entity.Scmoutsourceitembom;
import org.br.erp.outsource.mapper.ScmoutsourcebomMapper;
import org.br.erp.outsource.mapper.ScmoutsourceitemMapper;
import org.br.erp.outsource.mapper.ScmoutsourceMapper;
import org.br.erp.outsource.service.IScmoutsourceService;
import org.br.erp.outsource.vo.ScmReceiptReqReferWwVo;
import org.br.erp.outsource.vo.ScmoutsourcePage;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 委外订单
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Service
public class ScmoutsourceServiceImpl extends ServiceImpl<ScmoutsourceMapper, Scmoutsource>
		implements IScmoutsourceService, IReceiptReqWriteBackOutSource {

	@Autowired
	private ScmoutsourceMapper scmoutsourceMapper;
	@Autowired
	private ScmoutsourceitemMapper scmoutsourceitemMapper;

	@Autowired
	private ScmoutsourcebomMapper scmoutsourcebomMapper;

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private MyGreenEventBusHandler eventBusHandler;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmoutsource scmoutsource, List<Scmoutsourceitem> scmoutsourceitemList, ScmoutsourcePage scmoutsourcePage) {
		scmoutsourceMapper.insert(scmoutsource);
		List<Scmoutsourceitembom> scmoutsourceitembomList = scmoutsourcePage.getScmoutsourceitembomList();

		if(scmoutsourceitemList!=null && scmoutsourceitemList.size()>0) {
			for(Scmoutsourceitem entity:scmoutsourceitemList) {
				//外键设置
				entity.setParentid(scmoutsource.getId());
				entity.setId(entity.getBomitemid());
				scmoutsourceitemMapper.insert(entity);
				scmoutsourceitembomList.forEach(item->{
					if(item.getParentid().equalsIgnoreCase(entity.getBomitemid())){
						if(item.getId().indexOf("row_") > -1){
							item.setId(null);
						}
						scmoutsourcebomMapper.insert(item);
					}
				});
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmoutsource scmoutsource,List<Scmoutsourceitem> scmoutsourceitemList) {
		scmoutsourceMapper.updateById(scmoutsource);
		
		//1.先删除子表数据
		scmoutsourceitemMapper.deleteByMainId(scmoutsource.getId());
		
		//2.子表数据重新插入
		if(scmoutsourceitemList!=null && scmoutsourceitemList.size()>0) {
			for(Scmoutsourceitem entity:scmoutsourceitemList) {
				//外键设置
				entity.setParentid(scmoutsource.getId());
				scmoutsourceitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmoutsourceitemMapper.deleteByMainId(id);
		scmoutsourceMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmoutsourceitemMapper.deleteByMainId(id.toString());
			scmoutsourceMapper.deleteById(id);
		}
	}

	@Override
	@Transactional
	public void updateStatus(List<String> ids, String flag) {
		ids.forEach(item->{
			Scmoutsource scmoutsource = scmoutsourceMapper.selectById(item);
			if(scmoutsource.getStatus()!=null && !scmoutsource.getStatus().equalsIgnoreCase(flag)){
				scmoutsource.setStatus(flag);
			}else{
				scmoutsource.setStatus(flag);
			}
			scmoutsourceMapper.updateById(scmoutsource);
		});
	}


	private List<Scmoutsourceitembom> getBomInfo(String mainid){
		List<Scmoutsourceitembom> res=new ArrayList<>();
		List<Scmoutsourceitem> scmoutsourceitems = scmoutsourceitemMapper.selectByMainId(mainid);
		for (Scmoutsourceitem scmoutsourceitem : scmoutsourceitems) {
			String bomitemid = scmoutsourceitem.getBomitemid();
			QueryWrapper<Scmoutsourceitembom> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("parentid",bomitemid);
			List<Scmoutsourceitembom> scmoutsourceitemboms = scmoutsourcebomMapper.selectList(queryWrapper);
			res.addAll(scmoutsourceitemboms);
		}
		return res;
	}

	/**
	 * 委外出库
	 * @param ids
	 */
	@Override
	@Transactional
	public void wwoutStock(List<String> ids) {
		List<ScmOutSourceToOutStockModel> res=new ArrayList<>();
		for (String id : ids) {
			//根据表头id获取全部信息
			Scmoutsource scmoutsource = scmoutsourceMapper.selectById(id);

			List<Scmoutsourceitembom> bomInfo = getBomInfo(id);
			for (Scmoutsourceitembom scmoutsourceitembom : bomInfo) {

				List<Materialstock> materialstockByMaterialid = materialService.getMaterialstockByMaterialid(scmoutsourceitembom.getMaterialid());

				String defaultWarehouseid="";
				if(materialstockByMaterialid!=null && materialstockByMaterialid.size()>0 ){
					Materialstock materialstock = materialstockByMaterialid.get(0);
					if(materialstock!=null && materialstock.getDefwarehouseid()!=null){
						defaultWarehouseid=materialstock.getDefwarehouseid();
					}
				}
				ScmOutSourceToOutStockModel scmOutSourceToOutStockModel=
						ScmOutSourceToOutStockModel.builder()
								.fromid(id)
								.fromitemid(scmoutsourceitembom.getParentid())
								.materialid(scmoutsourceitembom.getMaterialid())
								.companyid(scmoutsource.getCompanyid())
								.vendorid(scmoutsource.getVendorid())
								.warehouseid(defaultWarehouseid)
								.batchid("")
								.outqty(scmoutsourceitembom.getQty()).build();
				res.add(scmOutSourceToOutStockModel);
			}

			eventBusHandler.onMessageEvent(res);

		}
	}

	@Override
	public List<ScmReceiptReqReferWwVo> getReciptReqReferWw(IPage<ScmReceiptReqReferWwVo> page,Map queryMap) {
		QueryWrapper<ScmReceiptReqReferWwVo> queryWrapper=new QueryWrapper<>();
		queryWrapper.orderByAsc("head.billdate");
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

		List<ScmReceiptReqReferWwVo> receiptReqReferWwVoList = scmoutsourceMapper.getreceiptreqreferlist(page, queryWrapper);

		return receiptReqReferWwVoList;

	}

	@Override
	public void ReqWriteBackOutSource(ReceiptWriteBackWwMessage receiptWriteBackWwMessage) {
		boolean isdelete = receiptWriteBackWwMessage.isIsdelete();
		String outsourceitemid = receiptWriteBackWwMessage.getOutsourceitemid();
		String receiptreqqty = receiptWriteBackWwMessage.getReceiptreqqty();
		Scmoutsourceitem scmoutsourceitem = scmoutsourceitemMapper.selectById(outsourceitemid);
		if(isdelete){
			//删除的话，先删除原有的数量，再进行处理
			String curReqQty = scmoutsourceitem.getReceiptreqqty();
			if (ERPUtils.getBigDecimal(curReqQty).compareTo(ERPUtils.getBigDecimal(receiptreqqty)) == -1) {
				throw new RuntimeException("当前数量不足，不允许删除！");
			}
			BigDecimal subtract = ERPUtils.getBigDecimal(curReqQty).subtract(ERPUtils.getBigDecimal(receiptreqqty));
			scmoutsourceitem.setReceiptreqqty(subtract.toString());
			updateScmoutsourceitemReceiptFlag(scmoutsourceitem);
			updateScmoutsourceReceiptFlag(scmoutsourceitem);
		}
		else if(!isdelete){
			//新增直接更新
			String curReqQty = scmoutsourceitem.getReceiptreqqty();

			BigDecimal subtract = ERPUtils.getBigDecimal(curReqQty).add(ERPUtils.getBigDecimal(receiptreqqty));
			scmoutsourceitem.setReceiptreqqty(subtract.toString());
			updateScmoutsourceitemReceiptFlag(scmoutsourceitem);
		}
	}

	/**
	 * 更新表体收货标识
	 * @param scmoutsourceitem
	 */
	private void updateScmoutsourceitemReceiptFlag(Scmoutsourceitem scmoutsourceitem) {
		if(ERPUtils.getBigDecimal(scmoutsourceitem.getReceiptreqqty()).compareTo(scmoutsourceitem.getQty()) ==1){
			throw new RuntimeException("不允许超订单收货");
		}
		if(ERPUtils.getBigDecimal(scmoutsourceitem.getReceiptreqqty()).compareTo(BigDecimal.ZERO) == 0){
			scmoutsourceitem.setReceiptreqflag("0");
		}
		if(ERPUtils.getBigDecimal(scmoutsourceitem.getReceiptreqqty()).compareTo(scmoutsourceitem.getQty()) == -1){
			scmoutsourceitem.setReceiptreqflag("1");
		} else if(ERPUtils.getBigDecimal(scmoutsourceitem.getReceiptreqqty()).compareTo(scmoutsourceitem.getQty()) == 0){
			scmoutsourceitem.setReceiptreqflag("2");
		}

		scmoutsourceitemMapper.updateById(scmoutsourceitem);
	}

	/**
	 * 更新表头收货标识
	 * @param scmoutsourceitem
	 */
	private void updateScmoutsourceReceiptFlag(Scmoutsourceitem scmoutsourceitem) {
		double v = scmoutsourceitemMapper.selectByMainId(scmoutsourceitem.getParentid()).stream().mapToInt(item -> {
			return Integer.parseInt(item.getReceiptreqflag());
		}).average().orElse(0);

		Scmoutsource scmoutsource = scmoutsourceMapper.selectById(scmoutsourceitem.getParentid());

		scmoutsource.setReceiptReqFlag(ERPUtils.convertDoubleToString(v));

		scmoutsourceMapper.updateById(scmoutsource);
	}
}
