package org.br.erp.pr.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.pubsub.PubSubEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.br.erp.base.entity.Material;
import org.br.erp.base.mapper.MaterialMapper;
import org.br.erp.base.service.IScmbillcoderuleService;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.pr.entity.Scmpreq;
import org.br.erp.pr.entity.ScmpreqVo;
import org.br.erp.pr.entity.Scmpreqitem;
import org.br.erp.pr.mapper.ScmpreqMapper;
import org.br.erp.pr.mapper.ScmpreqitemMapper;
import org.br.erp.pr.service.IScmpreqService;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购申请
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
@Service("scmpreqservice")
public class ScmpreqServiceImpl extends ServiceImpl<ScmpreqMapper, Scmpreq> implements IScmpreqService {

	@Autowired
	private ScmpreqMapper scmpreqMapper;
	@Autowired
	private ScmpreqitemMapper scmpreqitemMapper;

	@Autowired
	private MaterialMapper materialMapper;


//
//	@Autowired
//	RepositoryService repositoryService;

	@Autowired
	IScmbillcoderuleService scmbillcoderuleService;

	@Override
	public Result audit(String dataid) {
		//现将单据与流程定义进行关联
//		Scmpreq scmpreq = scmpreqMapper.selectById(dataid);
//		//ProcessDefinition scmpreqdefinition = repositoryService.createProcessDefinitionQuery()
//				.processDefinitionCategory("scmpreq").active().latestVersion().singleResult();
//		if(scmpreqdefinition!=null){
//			String id = scmpreqdefinition.getId();
//			String key = scmpreqdefinition.getKey();
//			//flowCommonService.initActBusiness(scmpreq.getPreqcode()+"审批",dataid,"scmpreqservice",key,id);
//			HashMap<String, Object> variables = new HashMap<>();
//			variables.put("dataId",dataid);
//		//	Result result = flowDefinitionService.startProcessInstanceByDataId(dataid, variables);
//			//return result;
//			return Result.ok("");
//		}else{
//			return Result.error("启动失败");
//		}
		return Result.ok("");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmpreq scmpreq, List<Scmpreqitem> scmpreqitemList) {

		String billCode = scmbillcoderuleService.getBillCode("scmpreq");
		scmpreq.setPreqcode(billCode);

		scmpreqMapper.insert(scmpreq);
		if(scmpreqitemList!=null && scmpreqitemList.size()>0) {
			BigDecimal sumQty=new BigDecimal(0);
			BigDecimal sumValue=new BigDecimal(0);
			for(Scmpreqitem entity:scmpreqitemList) {
				//外键设置
				entity.setParentid(scmpreq.getId());
				sumQty = sumQty.add(entity.getQty());
				if(entity.getTaxinvalue()==null){
					entity.setTaxinvalue(BigDecimal.ZERO);
				}
				sumValue=sumValue.add(entity.getTaxinvalue());

				Material material = materialMapper.selectById(entity.getMaterialid());
				if(material!=null) {
					entity.setMaterialcode(material.getMaterialcode());
					entity.setMaterialname(material.getMaterialname());
				}
				entity.setIsPr("0");
				scmpreqitemMapper.insert(entity);
			}
			scmpreq.setSumqty(sumQty);
			scmpreq.setSumvalue(sumValue);
		}

		scmpreqMapper.updateById(scmpreq);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmpreq scmpreq,List<Scmpreqitem> scmpreqitemList) {
		scmpreq.setStatus("0");
		scmpreqMapper.updateById(scmpreq);

		BigDecimal sumQty=new BigDecimal(0);
		BigDecimal sumValue=new BigDecimal(0);
		
		//1.先删除子表数据
		scmpreqitemMapper.deleteByMainId(scmpreq.getId());
		
		//2.子表数据重新插入
		if(scmpreqitemList!=null && scmpreqitemList.size()>0) {
			for(Scmpreqitem entity:scmpreqitemList) {
				//外键设置
				entity.setParentid(scmpreq.getId());
				String materialid = entity.getMaterialid();
				Material material = materialMapper.selectById(materialid);
				if(material!=null) {
					entity.setMaterialcode(material.getMaterialcode());
					entity.setMaterialname(material.getMaterialname());
				}
				BigDecimal qty = entity.getQty();
				sumQty=sumQty.add(qty);
				sumValue=sumValue.add(entity.getTaxinvalue());
				scmpreqitemMapper.insert(entity);
			}
			scmpreq.setSumqty(sumQty);
			scmpreq.setSumvalue(sumValue);
			scmpreqMapper.updateById(scmpreq);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmpreqitemMapper.deleteByMainId(id);
		scmpreqMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmpreqitemMapper.deleteByMainId(id.toString());
			scmpreqMapper.deleteById(id);
		}
	}

	@Override
	public List<Scmpreq> selectScmpreqList(Collection<? extends Serializable> idList) {
		List<Scmpreq> scmpreqs = scmpreqMapper.selectBatchIds(idList);
		return scmpreqs;
	}

	@Override
	public List<ScmpreqVo> getScmpreqVoList() {
		return scmpreqMapper.selectAll();
	}

	@Override
	public IPage<ScmpreqVo> selectpregVoPage(IPage<ScmpreqVo> page, Map queryMap) {

		QueryWrapper<ScmpreqVo> queryWrapper=new QueryWrapper<>();
		if(queryMap.containsKey("preqcode")){
			String[] preqcode=(String[])queryMap.get("preqcode");
			queryWrapper.like("preqcode",preqcode[0]);
		}
		if(queryMap.containsKey("materialparam")){
			String[] materialparam = (String[])queryMap.get("materialparam");
			queryWrapper.like("materialcode",materialparam[0]).or().like("materialname",materialparam[0]);
		}
		if(queryMap.containsKey("createTimeRange[]")){
			String[] timelink=(String[])queryMap.get("createTimeRange[]");
			queryWrapper.between("billdate",timelink[0],timelink[1]);
		}
		if(queryMap.containsKey("companyid")){
			String companyid = ERPUtils.getHttpReqParam(queryMap, "companyid");
			if(!companyid.equalsIgnoreCase("")){
				queryWrapper.eq("companyid",companyid);
			}
		}

		queryWrapper.orderByDesc("billdate");

		return page.setRecords(scmpreqMapper.getpreqVoList(page,queryWrapper));
	}

	/*
	* 流程完毕之后回写单据状态
	* */
//	@Transactional
//	@Override
//	public void afterFlowHandle(FlowMyBusiness business) {
//		//没有后续任务的时候，就更新单据状态为已完成
//		if(StringUtils.isBlank(business.getTaskId())){
//			String dataId = business.getDataId();
//			Scmpreq scmpreq = scmpreqMapper.selectById(dataId);
//			scmpreq.setStatus("2");
//			scmpreqMapper.updateById(scmpreq);
//		}else{
//			String dataId=business.getDataId();
//			if(StringUtils.isNotEmpty(dataId)){
//				Scmpreq scmpreq = scmpreqMapper.selectById(dataId);
//				scmpreq.setStatus("1");
//				scmpreqMapper.updateById(scmpreq);
//			}
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
}
