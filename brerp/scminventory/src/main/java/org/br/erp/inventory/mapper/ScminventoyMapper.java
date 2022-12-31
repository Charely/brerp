package org.br.erp.inventory.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.br.erp.inventory.entity.Scminventoy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.br.erp.inventory.vo.ScmPoInvoiceReferInstock;

/**
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
public interface ScminventoyMapper extends BaseMapper<Scminventoy> {



    public List<ScmPoInvoiceReferInstock> getReferInstockData(@Param("page") IPage<ScmPoInvoiceReferInstock> page,
                                                              @Param(Constants.WRAPPER) Wrapper<ScmPoInvoiceReferInstock> queryWrapper);
}
