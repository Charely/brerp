package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.entity.Scminventoy;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.inventory.vo.ScmPoInvoiceReferInstock;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
public interface IScminventoyService extends IService<Scminventoy> {

	/**
	 * 添加一对多

	 */
	public void saveMain(Map saveMap) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scminventoy
	 * @param scminventoyitemList
	 */
	public void updateMain(Scminventoy scminventoy,List<Scminventoryitem> scminventoyitemList);
	
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

	void updateInventoryFlag(String id,String inventoryFlag);


	public void updatePoStatus(List<String> ids,String statusflag);


	public void create1000item(String id);

	public List<ScmPoInvoiceReferInstock> getReferInstockData(Map queryMap);
}
