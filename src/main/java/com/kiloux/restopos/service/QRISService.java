package com.kiloux.restopos.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;

public class QRISService {
    
    public static BufferedImage generateQRIS(String content, int size) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Simulate dynamic QRIS string generation
    public static String generateDynamicQRISString(double amount, String transactionId) {
        // In real world this follows ASPI standard (ISO 8583 derivatives)
        // Format: [PayloadFormatIndicator][PointOfInitiationMethod]...[CRC]
        return "00020101021226580016ID.CO.QRIS.WWW01189360091433300220652030000" + transactionId + "5403" + (int)amount + "5802ID5913KouRestaurant6009Samarinda6304ABCD";
    }
}
