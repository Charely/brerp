package org.br.erp.outsource.service;

import org.br.erp.outsource.entity.Scmoutsourceitem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.outsource.entity.Scmoutsourceitembom;

import java.util.List;

/**
 * @Description: 委外订单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
public interface IScmoutsourceitemService extends IService<Scmoutsourceitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmoutsourceitem>
	 */
	public List<Scmoutsourceitem> selectByMainId(String mainId);

	public List<Scmoutsourceitembom> selectBomInfoByMainId(String itemid);
}
