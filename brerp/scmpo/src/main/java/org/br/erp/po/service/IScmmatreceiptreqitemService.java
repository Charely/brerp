package org.br.erp.po.service;

import org.br.erp.po.entity.Scmmatreceiptreqitem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.po.vo.ScmInventoryReferMatreceiptItemVo;

import java.util.List;

/**
 * @Description: 收料申请单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
public interface IScmmatreceiptreqitemService extends IService<Scmmatreceiptreqitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmmatreceiptreqitem>
	 */
	public List<Scmmatreceiptreqitem> selectByMainId(String mainId);

	List<ScmInventoryReferMatreceiptItemVo> getscmInventoryReferMaterItemVo(String ids);
}
