package org.br.erp.so.mapper;

import java.util.List;
import org.br.erp.so.entity.Scmsalesinvoiceitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
public interface ScmsalesinvoiceitemMapper extends BaseMapper<Scmsalesinvoiceitem> {

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
   * @return List<Scmsalesinvoiceitem>
   */
	public List<Scmsalesinvoiceitem> selectByMainId(@Param("mainId") String mainId);
}
