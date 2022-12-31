package org.br.erp.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.so.entity.Scmso;
import org.br.erp.so.entity.Scmsoitem;
import org.br.erp.so.mapper.ScmsoitemMapper;
import org.br.erp.so.mapper.ScmsoMapper;
import org.br.erp.so.service.IScmsoService;
import org.br.erp.so.vo.ScmOutStockReferSoVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 销售订单
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
@Service
@Lazy
public class ScmsoServiceImpl extends ServiceImpl<ScmsoMapper, Scmso> implements IScmsoService {

	@Autowired
	private ScmsoMapper scmsoMapper;
	@Autowired
	private ScmsoitemMapper scmsoitemMapper;

	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmso scmso, List<Scmsoitem> scmsoitemList) {

		String billCode = scmbillcoderuleService.getBillCode("scmso");
		scmso.setBillcode(billCode);
		scmsoMapper.insert(scmso);
		String isred=scmso.getIsred();
		if(scmsoitemList!=null && scmsoitemList.size()>0) {
			for(Scmsoitem entity:scmsoitemList) {
				//外键设置
				entity.setParentid(scmso.getId());
				if(isred!=null && isred.equalsIgnoreCase("1")) {
					entity.setIsred("1");
				}else{
					entity.setIsred("0");
				}
				scmsoitemMapper.insert(entity);
				if(isred!=null && isred.equalsIgnoreCase("1")){
					if(entity.getFromitemid()!=null
							&& !entity.getFromitemid().equalsIgnoreCase("")){
						writeBackSoitemFromReturnSo(entity,false);
					}
				}
			}
		}
	}

	@Transactional
	private void writeBackSoitemFromReturnSo(Scmsoitem entity,boolean isdel) {
		String fromitemid = entity.getFromitemid();
		Scmsoitem scmsoitem = scmsoitemMapper.selectById(fromitemid);

		BigDecimal returnqty = scmsoitem.getReturnqty();


		if(!isdel) {
			BigDecimal add = returnqty.add(entity.getQty());
			scmsoitem.setReturnqty(add);
		}else if(isdel){
			BigDecimal subtract = returnqty.subtract(entity.getQty());
			scmsoitem.setReturnqty(subtract);
		}

		if(scmsoitem.getReturnqty().compareTo(scmsoitem.getQty()) >= 0){
			if(scmsoitem.getReturnqty().compareTo(scmsoitem.getQty()) == 0){
				scmsoitem.setReturnflag("2");
			}else{
				throw new RuntimeException("不允许超订单退货");
			}
		}else {
			scmsoitem.setReturnflag("1");
		}
		if(scmsoitem.getReturnqty().compareTo(new BigDecimal(0))== 0){
			scmsoitem.setReturnflag("0");
		}

		scmsoitemMapper.updateById(scmsoitem);

		double v = scmsoitemMapper.selectByMainId(entity.getParentid()).stream()
				.map(item -> {
					return Integer.parseInt(item.getReturnflag());
				}).collect(Collectors.toList())
				.stream().mapToInt(Integer::intValue).average().orElse(0);

		Scmso fromso = scmsoMapper.selectById(entity.getParentid());
		fromso.setReturnflag(ERPUtils.convertDoubleToString(v));
		scmsoMapper.updateById(fromso);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmso scmso,List<Scmsoitem> scmsoitemList) {
		scmsoMapper.updateById(scmso);
		
		//1.先删除子表数据
		scmsoitemMapper.deleteByMainId(scmso.getId());
		
		//2.子表数据重新插入
		if(scmsoitemList!=null && scmsoitemList.size()>0) {
			for(Scmsoitem entity:scmsoitemList) {
				//外键设置
				entity.setParentid(scmso.getId());
				writeBackSoitemFromReturnSo(entity,true);
				scmsoitemMapper.insert(entity);
				writeBackSoitemFromReturnSo(entity,false);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmsoitemMapper.selectByMainId(id).stream().forEach(item->{
			writeBackSoitemFromReturnSo(item,true);
		});
		scmsoitemMapper.deleteByMainId(id);
		scmsoMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmsoitemMapper.deleteByMainId(id.toString());
			scmsoMapper.deleteById(id);
		}
	}

	@Override
	public List<ScmOutStockReferSoVo> getReferSOlist(IPage<ScmOutStockReferSoVo> page, Map queryMap,String isred) {
		QueryWrapper<ScmOutStockReferSoVo> queryWrapper=new QueryWrapper<>();

		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"companyid")){
			queryWrapper.eq("companyid",ERPUtils.getHttpReqParam(queryMap,"companyid"));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"billcode")){
			queryWrapper.like("billcode",ERPUtils.getHttpReqParam(queryMap,"billcode"));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"materialparam")){
			queryWrapper.and(item->item.like("materialcode",ERPUtils.getHttpReqParam(queryMap,"materialparam"))
					.or(i->i.like("materialname",ERPUtils.getHttpReqParam(queryMap,"materialparam"))));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"beginTime")){
			queryWrapper.and(item->item.between("billdate",
					ERPUtils.getHttpReqParam(queryMap,"beginTime"),ERPUtils.getHttpReqParam(queryMap,"endTime")));
		}
		if(ERPUtils.ifHtppReqParamContainKey(queryMap,"customerid")){
			queryWrapper.and(item->item.eq("customerid",ERPUtils.getHttpReqParam(queryMap,"customerid")));
		}
		if(isred.equalsIgnoreCase("0")) {
			//销售出库单参照销售订单
			queryWrapper.ne("scmsoitem.outflag", "2");
			queryWrapper.eq("scmso.isred","0");
		} else if(isred.equalsIgnoreCase("1")){
			//销售退货订单参照销售订单
			queryWrapper.ne("scmsoitem.returnflag","2");
			queryWrapper.eq("scmso.isred","0");
		} else if(isred.equalsIgnoreCase("2"))
		{
			//出库单红单参照退货订单
			queryWrapper.ne("scmsoitem.outflag","2");
			queryWrapper.eq("scmso.isred","1");
		}else if(isred.equalsIgnoreCase("3")){
			//销售发票参照销售订单
			queryWrapper.ne("scmsoitem.invoiceflag","2");
		}
		queryWrapper.eq("status","2");
		queryWrapper.orderByDesc("billdate");
		return scmsoMapper.getReferSOlist(page,queryWrapper);
	}

	@Override
	public void updatePoStatus(List<String> ids, String statusflag) {
		QueryWrapper<Scmso> queryWrapper=new QueryWrapper<>();
		queryWrapper.ne("status",statusflag);

		queryWrapper.and(item->item.eq("outflag","0").or(rq->rq.eq("outflag",null)));
		queryWrapper.in("id",ids);
		List<Scmso> scmsos = scmsoMapper.selectList(queryWrapper);
		scmsos.stream().forEach(item->{
			item.setStatus(statusflag);
			scmsoMapper.updateById(item);
		});
	}

}
