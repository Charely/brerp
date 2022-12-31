package org.br.erp.inventory.service;

import org.br.erp.inventory.entity.Scmtransferbillitem;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 移库单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
public interface IScmtransferbillitemService extends IService<Scmtransferbillitem> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<Scmtransferbillitem>
	 */
	public List<Scmtransferbillitem> selectByMainId(String mainId);
}
