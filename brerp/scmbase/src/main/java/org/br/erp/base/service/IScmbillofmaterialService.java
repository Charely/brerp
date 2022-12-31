package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbillofmaterialitem;
import org.br.erp.base.entity.Scmbillofmaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物料清单
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface IScmbillofmaterialService extends IService<Scmbillofmaterial> {

	/**
	 * 添加一对多
	 *
	 * @param scmbillofmaterial
	 * @param scmbillofmaterialitemList
	 */
	public void saveMain(Scmbillofmaterial scmbillofmaterial,List<Scmbillofmaterialitem> scmbillofmaterialitemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param scmbillofmaterial
   * @param scmbillofmaterialitemList
	 */
	public void updateMain(Scmbillofmaterial scmbillofmaterial,List<Scmbillofmaterialitem> scmbillofmaterialitemList);
	
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


	public List<Scmbillofmaterialitem> getBomInfoByItemid(String partentMaterialid);
	
}
