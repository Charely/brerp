package org.br.erp.pr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.pr.entity.Scmpreqitem;
import org.br.erp.pr.vo.ScmpreqitemVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购申请表体
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
public interface IScmpreqitemService extends IService<Scmpreqitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmpreqitem>
	 */
	public List<Scmpreqitem> selectByMainId(String mainId);

	public List<Scmpreqitem> selectByMainIds(Collection<? extends Serializable> ids);


	public List<ScmpreqitemVo> selectByIds(Collection<? extends Serializable> ids);


	public void updatePrStatue(String itemid,String pritemid);


	void updatePreqItemStatus(String pritemid);

}
