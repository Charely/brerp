package org.br.erp.po.service;

import org.br.erp.po.entity.Scmlinkqtyitem;
import org.br.erp.po.entity.Scmlinkqty;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 配额定义
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
public interface IScmlinkqtyService extends IService<Scmlinkqty> {

	/**
	 * 添加一对多
	 *
	 * @param scmlinkqty
	 * @param scmlinkqtyitemList
	 */
	public void saveMain(Scmlinkqty scmlinkqty,List<Scmlinkqtyitem> scmlinkqtyitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmlinkqty
	 * @param scmlinkqtyitemList
	 */
	public void updateMain(Scmlinkqty scmlinkqty,List<Scmlinkqtyitem> scmlinkqtyitemList);
	
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
