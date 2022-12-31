package org.br.erp.base.service;

import org.br.erp.base.entity.Scmbarcode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.br.erp.entity.po.ScmVendorEntity;

import java.util.List;

/**
 * @Description: 条码表
 * @Author: jeecg-boot
 * @Date:   2022-10-20
 * @Version: V1.0
 */
public interface IScmbarcodeService extends IService<Scmbarcode> {


    public void createBarCode(Scmbarcode barcode);

    void updateBarcode(Scmbarcode scmbarcode);

    List<Scmbarcode> getBarcodes(String billitemid);


    ScmVendorEntity getVendorInfo(String billid);

}
