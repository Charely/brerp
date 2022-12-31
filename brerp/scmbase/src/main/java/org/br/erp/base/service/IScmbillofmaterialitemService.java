package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbillofmaterialitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物料清单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface IScmbillofmaterialitemService extends IService<Scmbillofmaterialitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmbillofmaterialitem>
	 */
	public List<Scmbillofmaterialitem> selectByMainId(String mainId);
}
