package org.br.erp.po.service;

import org.br.erp.po.entity.Scmpurinvoiceitem;
import org.br.erp.po.entity.Scmpurinvoice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购发票
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
public interface IScmpurinvoiceService extends IService<Scmpurinvoice> {

	/**
	 * 添加一对多
	 *
	 * @param scmpurinvoice
	 * @param scmpurinvoiceitemList
	 */
	public void saveMain(Scmpurinvoice scmpurinvoice,List<Scmpurinvoiceitem> scmpurinvoiceitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmpurinvoice
	 * @param scmpurinvoiceitemList
	 */
	public void updateMain(Scmpurinvoice scmpurinvoice,List<Scmpurinvoiceitem> scmpurinvoiceitemList);
	
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
