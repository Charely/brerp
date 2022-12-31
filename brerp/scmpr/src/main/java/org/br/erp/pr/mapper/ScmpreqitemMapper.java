package org.br.erp.pr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.br.erp.pr.entity.Scmpreqitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购申请表体
 * @Author: jeecg-boot
 * @Date:   2022-07-27
 * @Version: V1.0
 */
public interface ScmpreqitemMapper extends BaseMapper<Scmpreqitem> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	@Delete("DELETE FROM SCMPREQITEM WHERE PARENTID = #{mainId}")
	void  deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<Scmpreqitem>
   */
  @Select("SELECT * FROM SCMPREQITEM WHERE PARENTID = #{mainId}")
	public List<Scmpreqitem> selectByMainId(@Param("mainId") String mainId);

  @Update("UPDATE SCMPREQITEM SET is_pr='1',pritemid=#{pritemid} where id=#{itemid}")
	public void updatePrStatue(@Param("itemid") String itemid,@Param("pritemid") String pritemid );

  @Update("UPDATE SCMPREQITEM SET IS_PR='0',PRITEMID='' WHERE PRITEMID=#{pritemid}")
  public void updatePreqStatue(@Param("pritemid") String pritemid);

}
