package org.br.erp.so.mapper;

import java.util.List;
import org.br.erp.so.entity.Scmsoitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售订单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
public interface ScmsoitemMapper extends BaseMapper<Scmsoitem> {

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
   * @return List<Scmsoitem>
   */
	public List<Scmsoitem> selectByMainId(@Param("mainId") String mainId);
}
