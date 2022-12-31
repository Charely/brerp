package org.br.erp.po.mapper;

import java.util.List;
import org.br.erp.po.entity.Scmmatreceiptreqitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 收料申请单分录
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
public interface ScmmatreceiptreqitemMapper extends BaseMapper<Scmmatreceiptreqitem> {

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
   * @return List<Scmmatreceiptreqitem>
   */
	public List<Scmmatreceiptreqitem> selectByMainId(@Param("mainId") String mainId);
}
