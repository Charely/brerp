package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.br.erp.base.entity.Scmbarcode;
import org.br.erp.base.mapper.ScmbarcodeMapper;
import org.br.erp.base.service.IScmbarcodeService;
import org.br.erp.base.service.writebackapi.IGetVendorPo;
import org.br.erp.base.utils.QrCodeGeneratorUtils;
import org.br.erp.entity.po.ScmVendorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

/**
 * @Description: 条码表
 * @Author: jeecg-boot
 * @Date:   2022-10-20
 * @Version: V1.0
 */
@Service
@Slf4j
public class ScmbarcodeServiceImpl extends ServiceImpl<ScmbarcodeMapper, Scmbarcode> implements IScmbarcodeService {

    @Autowired
    ScmbarcodeMapper scmbarcodeMapper;

    /**
     * 保存并生成二维码
     * @param barcode
     */
    @Override
    public void createBarCode(Scmbarcode barcode) {
        try {
            scmbarcodeMapper.insert(barcode);
            BufferedImage bufferedImage = QrCodeGeneratorUtils.generateQRCodeImage(barcode.getId());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            log.info(os.toString());
            barcode.setBarcode(os.toByteArray());
            scmbarcodeMapper.updatebarcode(os.toByteArray(),barcode.getId());
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public void updateBarcode(Scmbarcode barcode) {
        try {
            String id = barcode.getId();

            BufferedImage bufferedImage = QrCodeGeneratorUtils.generateQRCodeImage(id);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            barcode.setBarcode(os.toByteArray());
            os.close();
            scmbarcodeMapper.updateById(barcode);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @Override
    public List<Scmbarcode> getBarcodes(String billitemid) {
        QueryWrapper<Scmbarcode> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("billitemid",billitemid);
        queryWrapper.eq("isvalid","0");
        List<Scmbarcode> scmbarcodes = scmbarcodeMapper.selectList(queryWrapper);
        return scmbarcodes;
    }

    @Override
    public ScmVendorEntity getVendorInfo(String billid) {
//        Scmbarcode scmbarcode = scmbarcodeMapper.selectById(billid);
//
//        ScmVendorEntity vendorList = getVendorPo.getVendorList(scmbarcode.getBillitemid());
//        vendorList.setNeedreceiptqty(scmbarcode.getBartext());
//        return vendorList;
        return null;
    }
}
