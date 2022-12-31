package org.br.erp.base.mapper;

import java.util.List;
import org.br.erp.base.entity.Scmbillofmaterialitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 物料清单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-30
 * @Version: V1.0
 */
public interface ScmbillofmaterialitemMapper extends BaseMapper<Scmbillofmaterialitem> {

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
   * @return List<Scmbillofmaterialitem>
   */
	public List<Scmbillofmaterialitem> selectByMainId(@Param("mainId") String mainId);
}
