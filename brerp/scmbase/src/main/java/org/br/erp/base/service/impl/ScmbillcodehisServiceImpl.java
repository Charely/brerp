package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmbillcodehis;
import org.br.erp.base.entity.Scmbillcoderule;
import org.br.erp.base.mapper.ScmbillcodehisMapper;
import org.br.erp.base.mapper.ScmbillcoderuleMapper;
import org.br.erp.base.service.IScmbillcodehisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 单据编号历史记录
 * @Author: jeecg-boot
 * @Date:   2022-10-14
 * @Version: V1.0
 */
@Service
public class ScmbillcodehisServiceImpl extends ServiceImpl<ScmbillcodehisMapper, Scmbillcodehis> implements IScmbillcodehisService {

    @Autowired
    ScmbillcodehisMapper scmbillcodehisMapper;

    @Override
    public List<Scmbillcodehis> selectScmBillCodeHisByObjectId(String objectId) {

        QueryWrapper<Scmbillcodehis> scmbillcodehisQueryWrapper=new QueryWrapper<>();
        scmbillcodehisQueryWrapper.eq("objectId",objectId);
        scmbillcodehisQueryWrapper.orderByAsc("maxbillcode");
        scmbillcodehisQueryWrapper.orderByAsc("billcode");
        List<Scmbillcodehis> scmbillcodehis = scmbillcodehisMapper.selectList(scmbillcodehisQueryWrapper);
        if(scmbillcodehis!=null){
            return scmbillcodehis;
        }
        return null ;
    }
}
