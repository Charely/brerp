package org.br.erp.inventory.mapper;

import java.util.List;
import org.br.erp.inventory.entity.Scmtransferbillitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 移库单分录
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
public interface ScmtransferbillitemMapper extends BaseMapper<Scmtransferbillitem> {

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
   * @return List<Scmtransferbillitem>
   */
	public List<Scmtransferbillitem> selectByMainId(@Param("mainId") String mainId);
}
