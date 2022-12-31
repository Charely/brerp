package org.br.erp.po.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.br.erp.po.entity.Scmpoitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购订单分录
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
public interface ScmpoitemMapper extends BaseMapper<Scmpoitem> {

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
   * @return List<Scmpoitem>
   */
	public List<Scmpoitem> selectByMainId(@Param("mainId") String mainId);
}
