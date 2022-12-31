package org.br.erp.so.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.so.entity.Scmso;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.so.vo.ScmOutStockReferSoVo;

/**
 * @Description: 销售订单
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
public interface ScmsoMapper extends BaseMapper<Scmso> {


    public List<ScmOutStockReferSoVo> getReferSOlist(@Param("page") IPage<ScmOutStockReferSoVo> page,
                                                     @Param(Constants.WRAPPER) Wrapper<ScmOutStockReferSoVo> queryWrapper);
}
