package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scminventoryitem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 库存单据分录
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
public interface IScminventoyitemService extends IService<Scminventoryitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scminventoyitem>
	 */
	public List<Scminventoryitem> selectByMainId(String mainId);
}
