package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.common.api.dto.LogDTO;
import org.jeecg.modules.base.objectEntity.CustomEntity;
import org.jeecg.modules.base.objectEntity.Scmbillcoderule;

import java.util.List;

/**
 * @Description: BaseCommonMapper
 * @author: jeecg-boot
 */
public interface BaseCommonMapper {

    /**
     * 保存日志
     * @param dto
     */
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void saveLog(@Param("dto")LogDTO dto);

    void  createTmpTable(String tableName);
    void inserttotmptable(@Param("tablename")String tablename,@Param("p") List<String> p);

    void droptemptable(@Param("tablename")String tablename);

    List<CustomEntity> selectCustomCode(@Param("objectcode")String objectcode);

    String selecttablevalue(@Param("tablename") String tablename,@Param("tablecode")String tablecode,
                            @Param("tableid")String tableid,@Param("tableidvalue")String tableidvalue);


    @Select("select * from scmbillcoderule  left join scmobject  on scmbillcoderule.objectid=scmobject.id" +
            " where scmobject.objectcode=#{objectcode}")
    Scmbillcoderule getBillCoderule(@Param("objectcode") String objectcode);

}
