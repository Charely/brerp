package org.br.erp.so.service;

import org.br.erp.so.entity.Scmsalesinvoiceitem;
import org.br.erp.so.entity.Scmsaleinvoice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售发票
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
public interface IScmsaleinvoiceService extends IService<Scmsaleinvoice> {

	/**
	 * 添加一对多
	 *
	 * @param scmsaleinvoice
	 * @param scmsalesinvoiceitemList
	 */
	public void saveMain(Scmsaleinvoice scmsaleinvoice,List<Scmsalesinvoiceitem> scmsalesinvoiceitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmsaleinvoice
	 * @param scmsalesinvoiceitemList
	 */
	public void updateMain(Scmsaleinvoice scmsaleinvoice,List<Scmsalesinvoiceitem> scmsalesinvoiceitemList);
	
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
