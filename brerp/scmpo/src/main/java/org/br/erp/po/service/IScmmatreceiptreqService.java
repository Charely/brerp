package org.br.erp.po.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.po.entity.Scmmatreceiptreqitem;
import org.br.erp.po.entity.Scmmatreceiptreq;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.po.vo.ScmReferMatreceiptreqVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 收料申请单
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
public interface IScmmatreceiptreqService extends IService<Scmmatreceiptreq> {

	/**
	 * 添加一对多
	 *
	 * @param scmmatreceiptreq
	 * @param scmmatreceiptreqitemList
	 */
	public void saveMain(Scmmatreceiptreq scmmatreceiptreq,List<Scmmatreceiptreqitem> scmmatreceiptreqitemList) ;
	
	/**
	 * 修改一对多
	 *
   * @param scmmatreceiptreq
   * @param scmmatreceiptreqitemList
	 */
	public void updateMain(Scmmatreceiptreq scmmatreceiptreq,List<Scmmatreceiptreqitem> scmmatreceiptreqitemList);
	
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


	public List<ScmReferMatreceiptreqVo> getReferMatreceipreqVo(IPage<ScmReferMatreceiptreqVo> page, Map queryMap);


	public void updatePoStatus(List<String> ids,String statusflag);

	void saveBarCodeVendorInfo(List<String> ids);
	
}
