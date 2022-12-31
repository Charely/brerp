package org.br.erp.po.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.entity.po.ScmVendorEntity;
import org.br.erp.po.entity.Scmpo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
public interface IScmpoService extends IService<Scmpo> {


	 List<ScmMatReceiptReferPoVo> getReferList(IPage<ScmMatReceiptReferPoVo> page, Wrapper<ScmMatReceiptReferPoVo> queryWrapper);

	 List<ScmMatReceiptReferPoVo> getPuinvoiceReferPoList(IPage<ScmMatReceiptReferPoVo> page, Wrapper<ScmMatReceiptReferPoVo> queryWrapper);

	 List<ScmVendorReferPoVo> getvendorReferlist(IPage<ScmVendorReferPoVo> page, Map queryWrapper);

	/**
	 * 添加一对多
	 *
	 * saveMap
	 */
	public void saveMain(Map saveMap) ;
	
	/**
	 * 修改一对多
	 *
	 * @param scmpo
	 * @param scmpoitemList
	 */
	public void updateMain(Map scmpoMap);
	
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

	ScmVendorEntity getVendorList(String barid);

	
}
