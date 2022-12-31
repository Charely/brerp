package org.br.erp.base.service;

import org.br.erp.base.entity.Scmpartner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
public interface IScmpartnerService extends IService<Scmpartner> {

    List<Scmpartner> getPartnerList(String partnertype);
}
