package org.br.erp.outsource.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.outsource.entity.Scmoutsource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.outsource.vo.ScmReceiptReqReferWwVo;

/**
 * @Description: 委外订单
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
public interface ScmoutsourceMapper extends BaseMapper<Scmoutsource> {

    public List<ScmReceiptReqReferWwVo> getreceiptreqreferlist(@Param("page") IPage<ScmReceiptReqReferWwVo> page,
                                                     @Param(Constants.WRAPPER) Wrapper<ScmReceiptReqReferWwVo> queryWrapper);
}
