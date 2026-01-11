package com.kiloux.restopos.ui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class PlexUtils {

    // Plex Palette
    public static final Color PLEX_HEADER_TOP = new Color(58, 110, 165); // Slate Blue
    public static final Color PLEX_HEADER_BOT = new Color(20, 45, 75);   // Darker Slate
    public static final Color PLEX_GLOSS_WHITE = new Color(255, 255, 255, 60);
    public static final Color PLEX_BORDER = new Color(255, 255, 255, 120);
    
    public static void drawPlexHeader(Graphics2D g2, int w, int h) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 1. Base Gradient (Vertical)
        GradientPaint gp = new GradientPaint(0, 0, PLEX_HEADER_TOP, 0, h, PLEX_HEADER_BOT);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        
        // 2. Gloss Shine (Top Half)
        g2.setPaint(new GradientPaint(0, 0, new Color(255,255,255,100), 0, h/2, new Color(255,255,255,10)));
        g2.fillRoundRect(0, 0, w, h/2, 0, 0); // Flat rect for header shine
        
        // 3. Bottom Highlight Line
        g2.setColor(new Color(255, 255, 255, 80));
        g2.drawLine(0, h-1, w, h-1);
    }
    
    public static void drawGlassButton(Graphics2D g2, int w, int h, boolean hover, boolean press) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color base = hover ? new Color(100, 150, 200, 180) : new Color(50, 80, 100, 150);
        if (press) base = new Color(30, 60, 90, 200);
        
        // Shape
        RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, w, h, 8, 8);
        
        // Fill
        g2.setColor(base);
        g2.fill(rect);
        
        // Gloss Top
        g2.setPaint(new GradientPaint(0, 0, new Color(255,255,255,120), 0, h/2, new Color(255,255,255,20)));
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h/2, 8, 8));
        
        // Border
        g2.setColor(new Color(255, 255, 255, 150));
        g2.draw(rect);
    }
    
    public static void applyPlexStyle(JComponent comp) {
        comp.setBackground(new Color(245, 250, 255)); // Plex Off-White
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
}
