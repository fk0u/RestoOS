package com.kiloux.restopos.ui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import javax.swing.JPanel;

public class GlassPanel extends JPanel {

    private int borderRadius = 20;

    public GlassPanel() {
        setOpaque(false);
    }
    
    public GlassPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Glass Background
        g2.setColor(UIConfig.GLASS_BACKGROUND);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
        
        // Border
        g2.setColor(UIConfig.GLASS_BORDER);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
