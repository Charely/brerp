package org.br.erp.inventory.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.mapper.ScminventorytypeMapper;
import org.br.erp.inventory.base.service.IScminventorytypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 库存单据类型
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Service
public class ScminventorytypeImpl extends ServiceImpl<ScminventorytypeMapper, Scminventorytype> implements IScminventorytypeService {

    @Autowired
    private ScminventorytypeMapper scminventorytypeMapper;
    @Override
    public String getInventoryType(String inventorytypecode) {
        QueryWrapper<Scminventorytype> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("typecode",inventorytypecode);
        List<Scminventorytype> scminventorytypes = scminventorytypeMapper.selectList(queryWrapper);
        Scminventorytype scminventorytype = (Scminventorytype) scminventorytypes.get(0);
        if(scminventorytype.getInstock()!=null && scminventorytype.getInstock().equalsIgnoreCase("true")){
            return "0";
        }else{
            return "1";
        }
    }

    @Override
    public Scminventorytype getInventoryTypeByTypecode(String typecode) {
        QueryWrapper<Scminventorytype> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("typecode",typecode);
        List<Scminventorytype> scminventorytypes = scminventorytypeMapper.selectList(queryWrapper);
        Scminventorytype scminventorytype = (Scminventorytype) scminventorytypes.get(0);
        return scminventorytype;
    }
}
