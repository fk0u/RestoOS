package com.kiloux.restopos.ui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class RetroButton extends JButton {

    public RetroButton(String text) {
        super(text);
        init();
    }
    
    public RetroButton(String text, Color primary, Color secondary) {
        super(text);
        init();
        // Ignoring gradient colors for pure Retro/Win98 look, or using them as text/icon colors
    }

    private void init() {
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBackground(new Color(240, 240, 240)); // Fallback
        setForeground(Color.BLACK);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        // Base Gradient (Vista-like)
        Color top = new Color(245, 245, 245);
        Color bot = new Color(200, 200, 200);
        
        if (getModel().isPressed()) {
            top = new Color(180, 180, 180);
            bot = new Color(220, 220, 220);
        } else if (getModel().isRollover()) {
            top = new Color(255, 255, 255);
            bot = new Color(220, 235, 255); // Blue-ish hint
        }
        
        GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bot);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, w-1, h-1, 8, 8);
        
        // Glass Shine (Top Half)
        GradientPaint shine = new GradientPaint(0, 0, new Color(255, 255, 255, 180), 0, h/2, new Color(255, 255, 255, 50));
        g2.setPaint(shine);
        g2.fillRoundRect(0, 0, w-1, h/2, 8, 8);
        
        // Border
        g2.setColor(new Color(120, 120, 120));
        g2.drawRoundRect(0, 0, w-1, h-1, 8, 8);
        
        super.paintComponent(g);
    }
    
    public void startPulse() { }
}
