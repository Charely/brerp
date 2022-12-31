package org.br.erp.inventory.mapper;

import java.util.List;

import org.br.erp.inventory.entity.Scminventoryitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 库存单据分录
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
public interface ScminventoyitemMapper extends BaseMapper<Scminventoryitem> {

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
   * @return List<Scminventoyitem>
   */
	public List<Scminventoryitem> selectByMainId(@Param("mainId") String mainId);
}
