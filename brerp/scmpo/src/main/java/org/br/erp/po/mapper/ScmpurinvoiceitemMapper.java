package org.br.erp.po.mapper;

import java.util.List;
import org.br.erp.po.entity.Scmpurinvoiceitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购发票分录
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
public interface ScmpurinvoiceitemMapper extends BaseMapper<Scmpurinvoiceitem> {

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
   * @return List<Scmpurinvoiceitem>
   */
	public List<Scmpurinvoiceitem> selectByMainId(@Param("mainId") String mainId);
}
