package org.br.erp.inventory.mapper;

import java.util.List;
import org.br.erp.inventory.entity.Scminventorycheckitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 盘点单分录
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
public interface ScminventorycheckitemMapper extends BaseMapper<Scminventorycheckitem> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<Scminventorycheckitem>
   */
	public List<Scminventorycheckitem> selectByMainId(@Param("mainId") String mainId);
}
