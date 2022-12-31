package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scminventorycheckitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 盘点单分录
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface IScminventorycheckitemService extends IService<Scminventorycheckitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scminventorycheckitem>
	 */
	public List<Scminventorycheckitem> selectByMainId(String mainId);
}
