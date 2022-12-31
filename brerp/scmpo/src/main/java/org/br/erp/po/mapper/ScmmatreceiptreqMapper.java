package org.br.erp.po.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.br.erp.po.entity.Scmmatreceiptreq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmReferMatreceiptreqVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;

/**
 * @Description: 收料申请单
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
public interface ScmmatreceiptreqMapper extends BaseMapper<Scmmatreceiptreq> {


    public List<ScmReferMatreceiptreqVo> getReferMatreceiptList(@Param("page") IPage<ScmReferMatreceiptreqVo> page,
                                                                @Param(Constants.WRAPPER) Wrapper<ScmReferMatreceiptreqVo> queryWrapper);
}
