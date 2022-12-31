package org.br.erp.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.br.erp.base.entity.Material;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
public interface MaterialMapper extends BaseMapper<Material> {

    @Select("select * from material where materialtypeid =#{materialTypeid}")
    List<Material> getMaterialsByMaterialTypeid(String materialTypeid);

    @Select("select * from material where id=#{materialid}")
    Material getMaterialByMaterialId(String materialid);
}
