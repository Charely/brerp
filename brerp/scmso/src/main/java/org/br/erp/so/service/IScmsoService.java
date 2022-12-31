package org.br.erp.so.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.so.entity.Scmsoitem;
import org.br.erp.so.entity.Scmso;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.so.vo.ScmOutStockReferSoVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 销售订单
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
public interface IScmsoService extends IService<Scmso> {

	/**
	 * 添加一对多
	 *
	 * @param scmso
	 * @param scmsoitemList
	 */
	public void saveMain(Scmso scmso,List<Scmsoitem> scmsoitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmso
	 * @param scmsoitemList
	 */
	public void updateMain(Scmso scmso,List<Scmsoitem> scmsoitemList);
	
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

	public List<ScmOutStockReferSoVo> getReferSOlist(IPage<ScmOutStockReferSoVo> page, Map queryMap,String isred);

	public void updatePoStatus(List<String> ids,String statusflag);
	
}
