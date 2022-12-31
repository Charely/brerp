package org.br.erp.base.service;

import org.br.erp.base.entity.Scmprintformat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 打印数据源
 * @Author: jeecg-boot
 * @Date:   2022-12-18
 * @Version: V1.0
 */
public interface IScmprintformatService extends IService<Scmprintformat> {


    public void savefomrat(Scmprintformat scmprintformat) ;
    public Scmprintformat queryPrintFormatByObjectCode(String objectCode);
}
