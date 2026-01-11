package com.kiloux.restopos.ui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Metro/Windows 8 Style Rendering Engine
 * Flat Design with Accent Colors
 */
public class MetroUtils {

    // Metro Accent Colors
    public static Color ACCENT_CYAN = new Color(0, 153, 188);      // Windows 8 Cyan
    public static Color ACCENT_PURPLE = new Color(100, 80, 180);   // Purple
    public static Color ACCENT_GREEN = new Color(0, 120, 80);      // Dark Teal
    public static Color ACCENT_ORANGE = new Color(215, 125, 0);    // Orange
    
    public static Color METRO_BG_DARK = new Color(30, 30, 30);     // Dark Mode BG
    public static Color METRO_BG_LIGHT = new Color(245, 245, 245); // Light Mode BG
    public static Color METRO_TEXT_LIGHT = new Color(255, 255, 255);
    public static Color METRO_TEXT_DARK = new Color(30, 30, 30);
    
    /**
     * Draw a flat Metro tile button
     */
    public static void drawMetroTile(Graphics2D g2, int w, int h, Color accent, boolean hover, boolean pressed) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color fill = accent;
        if (pressed) fill = fill.darker();
        else if (hover) fill = fill.brighter();
        
        // Flat rectangle (no rounded corners for pure Metro)
        g2.setColor(fill);
        g2.fillRect(0, 0, w, h);
        
        // Subtle bottom shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRect(0, h - 3, w, 3);
    }
    
    /**
     * Draw a Metro style header bar
     */
    public static void drawMetroHeader(Graphics2D g2, int w, int h, Color accent) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Solid accent color
        g2.setColor(accent);
        g2.fillRect(0, 0, w, h);
    }
    
    /**
     * Create a styled Metro label (large, light font)
     */
    public static JLabel createMetroTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
        lbl.setForeground(METRO_TEXT_LIGHT);
        return lbl;
    }
    
    /**
     * Create a Metro styled button (tile)
     */
    public static JButton createTileButton(String text, Color accent, ActionListener action) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                drawMetroTile((Graphics2D)g, getWidth(), getHeight(), accent, getModel().isRollover(), getModel().isPressed());
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (action != null) btn.addActionListener(action);
        return btn;
    }
    
    public interface ActionListener extends java.awt.event.ActionListener {}
}
