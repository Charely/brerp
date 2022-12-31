package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbillcoderule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 单据编号规则表
 * @Author: jeecg-boot
 * @Date:   2022-10-12
 * @Version: V1.0
 */
public interface IScmbillcoderuleService extends IService<Scmbillcoderule> {

    public String getBillCode(String objectcode);


    void djDeleteButSaveBillCode(String objectCode,String billCode);

}
