package org.br.erp.po.mapper;

import java.util.List;
import org.br.erp.po.entity.Scmlinkqtyitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 配额协议分录
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
public interface ScmlinkqtyitemMapper extends BaseMapper<Scmlinkqtyitem> {

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
   * @return List<Scmlinkqtyitem>
   */
	public List<Scmlinkqtyitem> selectByMainId(@Param("mainId") String mainId);
}
