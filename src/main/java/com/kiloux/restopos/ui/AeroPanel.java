package com.kiloux.restopos.ui;

import java.awt.*;
import javax.swing.*;

public class AeroPanel extends JPanel {
    
    private Color tintColor = new Color(255, 255, 255, 30);
    private Color borderColor = new Color(255, 255, 255, 100);
    private boolean round = true;

    public AeroPanel() {
        setOpaque(false);
    }
    
    public AeroPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int arc = round ? 15 : 0;
        
        // Glass Body
        g2.setColor(tintColor);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        
        // Subtle Shine (Top Half)
        GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 50), 0, h/2, new Color(255, 255, 255, 10));
        g2.setPaint(shine);
        g2.fillRoundRect(0, 0, w, h/2, arc, arc);
        
        // Border / Stroke
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, w-1, h-1, arc, arc);
        
        g2.dispose();
    }
    
    public void setTintColor(Color c) { this.tintColor = c; repaint(); }
}
