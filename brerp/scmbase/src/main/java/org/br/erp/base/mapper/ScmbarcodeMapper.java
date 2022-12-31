package org.br.erp.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.br.erp.base.entity.Scmbarcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 条码表
 * @Author: jeecg-boot
 * @Date:   2022-10-20
 * @Version: V1.0
 */
public interface ScmbarcodeMapper extends BaseMapper<Scmbarcode> {

    void updatebarcode(@Param("barcode")byte[] barcode,@Param("id")String id);
}
