package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scmtransferbillitem;
import org.br.erp.inventory.entity.Scmtransferbill;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
public interface IScmtransferbillService extends IService<Scmtransferbill> {

	/**
	 * 添加一对多
	 *
	 * @param scmtransferbill
	 * @param scmtransferbillitemList
	 */
	public void saveMain(Scmtransferbill scmtransferbill,List<Scmtransferbillitem> scmtransferbillitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmtransferbill
	 * @param scmtransferbillitemList
	 */
	public void updateMain(Scmtransferbill scmtransferbill,List<Scmtransferbillitem> scmtransferbillitemList);
	
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
}
