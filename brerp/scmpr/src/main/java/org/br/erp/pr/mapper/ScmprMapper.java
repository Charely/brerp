package org.br.erp.pr.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.pr.entity.Scmpr;
import org.br.erp.pr.vo.ScmPoReferPrVo;
import org.br.erp.pr.vo.ScmprReferVo;

import java.util.List;

/**
 * @Description: 采购计划
 * @Author: jeecg-boot
 * @Date:   2022-08-01
 * @Version: V1.0
 */
public interface ScmprMapper extends BaseMapper<Scmpr> {

    public List<ScmprReferVo> getPrReferList(@Param("page") IPage<ScmprReferVo> page,
                                             @Param(Constants.WRAPPER) Wrapper<ScmprReferVo> queryWrapper);

}
