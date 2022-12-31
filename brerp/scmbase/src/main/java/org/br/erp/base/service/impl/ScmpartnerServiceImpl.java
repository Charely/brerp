package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmpartner;
import org.br.erp.base.mapper.ScmpartnerMapper;
import org.br.erp.base.service.IScmpartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Service
public class ScmpartnerServiceImpl extends ServiceImpl<ScmpartnerMapper, Scmpartner> implements IScmpartnerService {

    @Autowired
    ScmpartnerMapper scmpartnerMapper;


    /**
     * 0为供应商，1为客户
     * @return
     */
    @Override
    public List<Scmpartner> getPartnerList(String partnertype) {

        QueryWrapper<Scmpartner> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("partnertype",partnertype);
        List<Scmpartner> scmpartners = scmpartnerMapper.selectList(queryWrapper);
        return scmpartners;
    }

}
