package org.br.erp.base.service.impl;

import org.br.erp.base.entity.Currency;
import org.br.erp.base.mapper.CurrencyMapper;
import org.br.erp.base.service.ICurrencyService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 币种
 * @Author: jeecg-boot
 * @Date:   2022-11-18
 * @Version: V1.0
 */
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {

}
