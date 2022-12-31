package org.br.erp.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.br.erp.base.entity.Scmcustomfields;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 自定义表字段
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
public interface ScmcustomfieldsMapper extends BaseMapper<Scmcustomfields> {

    void appendtablecolumn(@Param("tablename") String tablename,@Param("columncode") String columncode);
}
