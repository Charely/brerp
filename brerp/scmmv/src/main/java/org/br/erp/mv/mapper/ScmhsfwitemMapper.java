package org.br.erp.mv.mapper;

import java.util.List;
import org.br.erp.mv.entity.Scmhsfwitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 核算范围分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface ScmhsfwitemMapper extends BaseMapper<Scmhsfwitem> {

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
   * @return List<Scmhsfwitem>
   */
	public List<Scmhsfwitem> selectByMainId(@Param("mainId") String mainId);
}
