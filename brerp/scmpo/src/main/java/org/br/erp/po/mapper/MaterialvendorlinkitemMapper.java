package org.br.erp.po.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.br.erp.po.entity.Materialvendorlinkitem;

import java.util.List;

/**
 * @Description: 货源清单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-07
 * @Version: V1.0
 */
public interface MaterialvendorlinkitemMapper extends BaseMapper<Materialvendorlinkitem> {

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
   * @return List<Materialvendorlinkitem>
   */

	public List<Materialvendorlinkitem> selectByMainId(@Param("mainId") String mainId);
}
