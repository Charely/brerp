package org.br.erp.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.br.erp.base.entity.onlcgformfield;

import java.util.List;

public interface onlcgformfieldMapper extends BaseMapper<onlcgformfield> {

    @Select("select id,db_field_name,cgform_head_id,db_field_txt,field_show_type,field_length,is_show_list,order_num " +
            "from onl_cgform_field where  cgform_head_id =#{head_id}")
    @Results({
            @Result(property = "id",column = "id"),
            @Result(property = "dbfieldname",column = "db_field_name"),
            @Result(property = "cgformheadid",column = "cgform_head_id"),
            @Result(property = "dbfieldtxt",column = "db_field_txt"),
            @Result(property = "fieldshowtype",column = "field_show_type"),
            @Result(property = "fieldlength",column = "field_length"),
            @Result(property = "isshowlist",column = "is_show_list"),
            @Result(property = "ordernum",column = "order_num"),
    })
    public List<onlcgformfield> selectfieldlist(String head_id);
}
