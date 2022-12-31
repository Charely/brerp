package org.br.erp.base.mapper;

import java.util.List;
import org.br.erp.base.entity.Scmcustomformatitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 自定义格式表分录
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
public interface ScmcustomformatitemMapper extends BaseMapper<Scmcustomformatitem> {

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
   * @return List<Scmcustomformatitem>
   */
	public List<Scmcustomformatitem> selectByMainId(@Param("mainId") String mainId);
}
