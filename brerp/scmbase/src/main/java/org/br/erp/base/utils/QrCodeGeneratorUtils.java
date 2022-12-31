package org.br.erp.base.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class QrCodeGeneratorUtils {

    public static BufferedImage generateQRCodeImage(String text) {
        HashMap hashMap = new HashMap(16);
        // 设置二维码字符编码
        hashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码纠错等级
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置二维码边距
        hashMap.put(EncodeHintType.MARGIN, 1);
        try {
            // 开始生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, hashMap);
            // 导出到指定目录
           // MatrixToImageWriter.writeToPath(bitMatrix, "png", new File(targetPath + fileName).toPath());
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            return bufferedImage;
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
