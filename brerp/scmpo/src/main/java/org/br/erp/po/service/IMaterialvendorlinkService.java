package org.br.erp.po.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.po.entity.Materialvendorlink;
import org.br.erp.po.entity.Materialvendorlinkitem;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 货源清单
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
public interface IMaterialvendorlinkService extends IService<Materialvendorlink> {

	/**
	 * 添加一对多
	 *
	 * @param materialvendorlink
	 * @param materialvendorlinkitemList
	 */
	public void saveMain(Materialvendorlink materialvendorlink,List<Materialvendorlinkitem> materialvendorlinkitemList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param materialvendorlink
	 * @param materialvendorlinkitemList
	 */
	public void updateMain(Materialvendorlink materialvendorlink,List<Materialvendorlinkitem> materialvendorlinkitemList);
	
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

	public List<Materialvendorlinkitem> getVendorItemsByMaterialid(String materialid);

	public List<Materialvendorlink> getMaterialsOfVendor(String vendorid);
	
}
