package org.br.erp.outsource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.outsource.entity.Scmoutsourceitem;
import org.br.erp.outsource.entity.Scmoutsource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.outsource.vo.ScmReceiptReqReferWwVo;
import org.br.erp.outsource.vo.ScmoutsourcePage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 委外订单
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
public interface IScmoutsourceService extends IService<Scmoutsource> {

	/**
	 * 添加一对多
	 *
	 * @param scmoutsource
	 * @param scmoutsourceitemList
	 */
	public void saveMain(Scmoutsource scmoutsource, List<Scmoutsourceitem> scmoutsourceitemList, ScmoutsourcePage scmoutsourcePage) ;
	
	/**
	 * 修改一对多
	 *
   * @param scmoutsource
   * @param scmoutsourceitemList
	 */
	public void updateMain(Scmoutsource scmoutsource,List<Scmoutsourceitem> scmoutsourceitemList);
	
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

	public void updateStatus(List<String> ids,String flag);


	public void wwoutStock(List<String> ids);

	public List<ScmReceiptReqReferWwVo> getReciptReqReferWw(IPage<ScmReceiptReqReferWwVo> page,Map requestMap);
}
