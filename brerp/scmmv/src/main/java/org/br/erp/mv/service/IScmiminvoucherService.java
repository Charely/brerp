package org.br.erp.im.service;

import org.br.erp.im.entity.Scmiminvoucheritem;
import org.br.erp.im.entity.Scmiminvoucher;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 入库凭证
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface IScmiminvoucherService extends IService<Scmiminvoucher> {

	/**
	 * 添加一对多
	 *
	 * @param scmiminvoucher
	 * @param scmiminvoucheritemList
	 */
	public void saveMain(Scmiminvoucher scmiminvoucher,List<Scmiminvoucheritem> scmiminvoucheritemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param scmiminvoucher
   * @param scmiminvoucheritemList
	 */
	public void updateMain(Scmiminvoucher scmiminvoucher,List<Scmiminvoucheritem> scmiminvoucheritemList);
	
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


	/**
	 * 库存单据生成存货凭证
	 * @param ids
	 */
	public void createMvouncher(List<String> ids,String flag) throws ParseException;


	public void updateStatus(List<String> ids,String flag);
	
}
