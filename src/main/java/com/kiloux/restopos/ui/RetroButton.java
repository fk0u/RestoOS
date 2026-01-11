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
        
        if (PlexUtils.PLEX_HEADER_TOP != null) {
            PlexUtils.drawGlassButton(g2, w, h, getModel().isRollover(), getModel().isPressed());
        } else {
            // Fallback (shouldn't happen)
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRoundRect(0, 0, w, h, 8, 8);
        }
        g2.drawRoundRect(0, 0, w-1, h-1, 8, 8);
        
        super.paintComponent(g);
    }
    
    public void startPulse() { }
}
