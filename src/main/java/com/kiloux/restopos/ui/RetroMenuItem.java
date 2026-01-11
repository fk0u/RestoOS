package com.kiloux.restopos.ui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;

public class RetroMenuItem extends JPanel {
    
    private String labelText;
    private int index;
    private Image iconImage;
    private boolean isSelected = false;
    private Runnable onClickAction;
    private String priceText;

    public RetroMenuItem(int index, String label, String price, Image icon, Runnable onClick) {
        this.index = index;
        this.labelText = label;
        this.priceText = price;
        this.iconImage = icon;
        this.onClickAction = onClick;
        
        setLayout(new BorderLayout());
        setBackground(UIConfig.BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(UIConfig.BORDER_COLOR, 1));
        
        // Make it interactive
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickAction != null) onClickAction.run();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                isSelected = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isSelected = false;
                repaint();
            }
        });
        
        setPreferredSize(new Dimension(160, 160));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background Color (Highlight if selected)
        if (isSelected) {
            g2.setColor(UIConfig.HIGHLIGHT_COLOR);
            g2.fillRect(1, 1, w - 2, h - 2); // Inside border
        } else {
            g2.setColor(UIConfig.BACKGROUND_COLOR);
            g2.fillRect(1, 1, w - 2, h - 2);
        }

        // 1. Index Number (Top Left)
        g2.setColor(UIConfig.TEXT_PRIMARY);
        g2.setFont(UIConfig.FONT_SMALL);
        g2.drawString(String.format("%02d", index), 8, 18);

        // 2. Icon (Center - Isometric Effect placeholder or Image)
        int iconSize = 64;
        int iconX = (w - iconSize) / 2;
        int iconY = (h - iconSize) / 2 - 10;

        if (iconImage != null) {
            g2.drawImage(iconImage, iconX, iconY, iconSize, iconSize, null);
        } else {
            // Isometric box placeholder if no image
            drawIsometricBox(g2, w / 2, h / 2 - 10, 30, isSelected ? UIConfig.PRIMARY_COLOR : UIConfig.SECONDARY_COLOR);
        }

        // 3. Label and Price (Bottom)
        FontMetrics fm = g2.getFontMetrics(UIConfig.FONT_BODY);
        int textW = fm.stringWidth(labelText);
        g2.setColor(UIConfig.TEXT_PRIMARY);
        g2.setFont(UIConfig.FONT_BODY);
        
        // Truncate if too long
        String displayLabel = labelText;
        if (textW > w - 10) {
           displayLabel = labelText.substring(0, Math.min(10, labelText.length())) + "...";
           textW = fm.stringWidth(displayLabel);
        }
        g2.drawString(displayLabel, (w - textW) / 2, h - 30);
        
        // Price
        g2.setFont(UIConfig.FONT_SMALL);
        fm = g2.getFontMetrics(UIConfig.FONT_SMALL);
        int priceW = fm.stringWidth(priceText);
        g2.setColor(UIConfig.TEXT_SECONDARY);
        g2.drawString(priceText, (w - priceW) / 2, h - 10);
    }
    
    private void drawIsometricBox(Graphics2D g2, int cx, int cy, int size, Color color) {
        // Simple faux-isometric cube
        int[] xPoints = {cx, cx + size, cx, cx - size};
        int[] yPoints = {cy - size/2, cy, cy + size/2, cy};
        
        g2.setColor(color);
        g2.fillPolygon(xPoints, yPoints, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(xPoints, yPoints, 4);
        
        // Side 1
        int[] xSide1 = {cx, cx + size, cx + size, cx};
        int[] ySide1 = {cy + size/2, cy, cy + size, cy + size + size/2};
        g2.setColor(color.darker());
        g2.fillPolygon(xSide1, ySide1, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(xSide1, ySide1, 4);
        
         // Side 2
        int[] xSide2 = {cx, cx - size, cx - size, cx};
        int[] ySide2 = {cy + size/2, cy, cy + size, cy + size + size/2};
        g2.setColor(color.darker().darker());
        g2.fillPolygon(xSide2, ySide2, 4);
        g2.setColor(Color.BLACK);
        g2.drawPolygon(xSide2, ySide2, 4);
    }
}
