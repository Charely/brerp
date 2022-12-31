package org.br.erp.price.service;

import org.br.erp.price.entity.Priceline;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: 价格管理
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
public interface IPricelineService extends IService<Priceline> {

   public BigDecimal getMaterialPrice(String priceType, String materialid, String vendorid, String customerid);
}
