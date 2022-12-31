package org.br.erp.mv.service;

import org.br.erp.mv.entity.Scmhsfwitem;
import org.br.erp.mv.entity.Scmhsfw;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 存货核算范围
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface IScmhsfwService extends IService<Scmhsfw> {

	/**
	 * 添加一对多
	 *
	 * @param scmhsfw
	 * @param scmhsfwitemList
	 */
	public void saveMain(Scmhsfw scmhsfw,List<Scmhsfwitem> scmhsfwitemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param scmhsfw
   * @param scmhsfwitemList
	 */
	public void updateMain(Scmhsfw scmhsfw,List<Scmhsfwitem> scmhsfwitemList);
	
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
	
}
