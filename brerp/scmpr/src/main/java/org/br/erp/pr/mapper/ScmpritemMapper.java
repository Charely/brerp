package org.br.erp.pr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.br.erp.pr.entity.Scmpritem;

import java.util.List;

/**
 * @Description: 采购计划分录
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface ScmpritemMapper extends BaseMapper<Scmpritem> {

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
   * @return List<Scmpritem>
   */

	public List<Scmpritem> selectByMainId(@Param("mainId") String mainId);
}
