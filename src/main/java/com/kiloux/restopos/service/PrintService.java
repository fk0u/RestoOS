package com.kiloux.restopos.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import java.awt.Desktop;
import com.itextpdf.layout.properties.TextAlignment;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrintService {

    public static String generateReceipt(Order order, List<CartItem> items, double payAmount, double change) {
        String filename = "receipt_" + System.currentTimeMillis() + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(new File(filename));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Header
            document.add(new Paragraph("KOU RESTAURANT")
                    .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Jl. Juanda No. 88, Samarinda\nTelp: 0812-3456-7890")
                    .setFontSize(10).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("------------------------------------------------")
                    .setTextAlignment(TextAlignment.CENTER));
            
            // Info
            document.add(new Paragraph("Date: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
            document.add(new Paragraph("Order ID: #" + order.getId()));
            document.add(new Paragraph("Type: " + order.getOrderType()));
            
            // Items
            float[] columnWidths = {4, 1, 2}; // Name, Qty, Price
            Table table = new Table(columnWidths); // Use unit arrays in newer iText or relative widths
            
            for (CartItem item : items) {
                document.add(new Paragraph(item.getMenuItem().getName() + " x" + item.getQuantity())
                        .setFontSize(10));
                document.add(new Paragraph(String.format("Rp %.0f", item.getSubtotal()))
                        .setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
            }
            
            document.add(new Paragraph("------------------------------------------------"));
            
            // Totals
            document.add(new Paragraph("Total: Rp " + String.format("%.0f", order.getTotalAmount()))
                    .setBold().setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Bayar: Rp " + String.format("%.0f", payAmount))
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph("Kembali: Rp " + String.format("%.0f", change))
                    .setTextAlignment(TextAlignment.RIGHT));
            
            // Footer
            document.add(new Paragraph("\nTerima Kasih atas Kunjungan Anda!")
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(10));
            
            document.close();
            
            // Auto-open
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(filename);
                    Desktop.getDesktop().open(myFile);
                } catch (Exception ex) {
                    // ex.printStackTrace();
                }
            }
            return filename;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
