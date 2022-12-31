package org.br.erp.price.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.price.entity.Priceline;
import org.br.erp.price.mapper.PricelineMapper;
import org.br.erp.price.service.IPricelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 价格管理
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Service
public class PricelineServiceImpl extends ServiceImpl<PricelineMapper, Priceline> implements IPricelineService {

    @Autowired
    PricelineMapper pricelineMapper;

    @Override
    public BigDecimal getMaterialPrice(String priceType, String materialid, String vendorid, String customerid) {
        QueryWrapper<Priceline> pricelineQueryWrapper=new QueryWrapper<>();
        pricelineQueryWrapper.eq("pricetype",priceType);
        pricelineQueryWrapper.eq("materialid",materialid);
        if(priceType.equalsIgnoreCase("0")){
            //采购价
            pricelineQueryWrapper.eq("vendorid",vendorid);
        }else if(priceType.equalsIgnoreCase("1")){
            //销售价
            pricelineQueryWrapper.eq("customerid",customerid);
        }else{
            pricelineQueryWrapper.eq("vendorid",vendorid);
        }
        List<Priceline> pricelines = pricelineMapper.selectList(pricelineQueryWrapper);
        if(pricelines!=null && pricelines.size()>0){
            BigDecimal price = pricelines.get(0).getPrice();
            return price;
        }else{
            return new BigDecimal(0);
        }
    }
}
