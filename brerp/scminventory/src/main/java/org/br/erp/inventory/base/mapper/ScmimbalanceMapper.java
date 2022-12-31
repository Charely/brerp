package org.br.erp.inventory.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.inventory.base.entity.Scmimbalance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.inventory.base.vo.ScmimbalanceVo;

/**
 * @Description: 库存账本表
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
public interface ScmimbalanceMapper extends BaseMapper<Scmimbalance> {

    List<ScmimbalanceVo> getBalanceAmount(@Param("page")IPage<ScmimbalanceVo> page,
                                          @Param(Constants.WRAPPER) Wrapper<ScmimbalanceVo> queryWrapper);
}
