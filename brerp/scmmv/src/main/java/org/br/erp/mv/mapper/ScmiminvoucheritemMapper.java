package org.br.erp.im.mapper;

import java.util.List;
import org.br.erp.im.entity.Scmiminvoucheritem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 入库凭证分录
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
public interface ScmiminvoucheritemMapper extends BaseMapper<Scmiminvoucheritem> {

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
   * @return List<Scmiminvoucheritem>
   */
	public List<Scmiminvoucheritem> selectByMainId(@Param("mainId") String mainId);
}
