package org.br.erp.pr.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.pr.entity.Scmpr;
import org.br.erp.pr.entity.ScmpreqVo;
import org.br.erp.pr.entity.Scmpritem;
import org.br.erp.pr.vo.ScmPoReferPrVo;
import org.br.erp.pr.vo.ScmprPage;
import org.br.erp.pr.vo.ScmprReferVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购计划
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface IScmprService extends IService<Scmpr> {

	/**
	 */
	public void saveMain(Map pageMap) ;
	
	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(Map pageMap);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	public String getPrOrderDate(String materialid, String prqty, String preqdate);


	IPage<ScmprReferVo> getReferPrVo(IPage<ScmprReferVo> page, Map queryWrapper);

	public void createpo(String ids);

	public void createporfromvendoritem(String ids);

	public List<ScmPoReferPrVo> getPoReferPrList(HashMap querymap);
}
