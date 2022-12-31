package org.br.erp.po.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.po.entity.Scmpo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;

import java.util.List;

/**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
public interface ScmpoMapper extends BaseMapper<Scmpo> {


    public List<ScmMatReceiptReferPoVo> getReferList(@Param("page") IPage<ScmMatReceiptReferPoVo> page,
                                                     @Param(Constants.WRAPPER) Wrapper<ScmMatReceiptReferPoVo> queryWrapper);

    public List<ScmMatReceiptReferPoVo> getPuinvoiceReferPoList(@Param("page") IPage<ScmMatReceiptReferPoVo> page,
                                                     @Param(Constants.WRAPPER) Wrapper<ScmMatReceiptReferPoVo> queryWrapper);

    public List<ScmVendorReferPoVo> getvendorReferlist(@Param("page") IPage<ScmVendorReferPoVo> page,
                                                 @Param(Constants.WRAPPER) Wrapper<ScmVendorReferPoVo> queryWrapper);

}
