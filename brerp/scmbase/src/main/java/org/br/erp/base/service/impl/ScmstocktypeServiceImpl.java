package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.Scmstocktype;
import org.br.erp.base.mapper.ScmstocktypeMapper;
import org.br.erp.base.service.IScmstocktypeService;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @Description: 库存状态
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Service
public class ScmstocktypeServiceImpl extends ServiceImpl<ScmstocktypeMapper, Scmstocktype> implements IScmstocktypeService {

    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Autowired
    private ScmstocktypeMapper scmstocktypeMapper;
    @Override
    @Transactional
    public void saveMain(Scmstocktype scmstocktype) {
        scmstocktypeMapper.insert(scmstocktype);

        QueryWrapper<Scmstocktype> queryWrapper=new QueryWrapper<>();
        List<Scmstocktype> scmstocktypes = scmstocktypeMapper.selectList(queryWrapper);
        HashMap<String,String > dictItems=new HashMap<>();
        for (Scmstocktype scmstocktype1 : scmstocktypes) {
            dictItems.put(scmstocktype1.getId(),scmstocktype1.getStockname());
        }
        if(dictItems!=null && dictItems.size()>0) {
            sysBaseAPI.saveDictModel("scmstocktype", "库存状态", dictItems);
        }
    }
}
