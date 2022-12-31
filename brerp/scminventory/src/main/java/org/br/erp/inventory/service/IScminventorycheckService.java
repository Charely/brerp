package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scminventorycheckitem;
import org.br.erp.inventory.entity.Scminventorycheck;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 库存盘点单
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IScminventorycheckService extends IService<Scminventorycheck> {

	/**
	 * 添加一对多
	 *
	 * @param scminventorycheck
	 * @param scminventorycheckitemList
	 */
	public void saveMain(Scminventorycheck scminventorycheck,List<Scminventorycheckitem> scminventorycheckitemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param scminventorycheck
   * @param scminventorycheckitemList
	 */
	public void updateMain(Scminventorycheck scminventorycheck,List<Scminventorycheckitem> scminventorycheckitemList);
	
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


	public void updatePoStatus(List<String> ids,String statusflag);

	/**
	 * 盘点确认
	 * @param ids
	 */
	public void inventorycheck(List<String> ids);

}
