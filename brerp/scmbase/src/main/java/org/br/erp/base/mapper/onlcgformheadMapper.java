package org.br.erp.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.br.erp.base.entity.onlcgformhead;

import java.util.List;

public interface onlcgformheadMapper extends BaseMapper<onlcgformhead> {

    @Select("select id,table_name from onl_cgform_head where table_name =#{tablename}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "table_name",column = "table_name")
    })
    public List<onlcgformhead> selectheadlist(String tablename);
}
