package com.kiloux.restopos.config;

import java.awt.Color;
import java.awt.Font;

public class UIConfig {
    // Legacy fallback size (runtime uses fullscreen sizing)
    public static final int WINDOW_WIDTH = 428;
    public static final int WINDOW_HEIGHT = 926;

    // Layout
    public static final int SAFE_PADDING = 24;
    public static final int SAFE_PADDING_LANDSCAPE = 16;

    // Colors
    public static final Color PRIMARY_COLOR = new Color(0x34D399); // Emeral 400 (Brighter for dark mode)
    public static final Color SECONDARY_COLOR = new Color(0x2DD4BF); // Teal 400
    public static final Color ACCENT_COLOR = new Color(0xF472B6); // Pink 400
    public static final Color WARNING_COLOR = new Color(0xFBBF24); // Amber 400
    public static final Color DANGER_COLOR = new Color(0xF87171); // Red 400
    public static final Color SUCCESS_COLOR = new Color(0x22C55E); // Green 500
    
    public static final Color BACKGROUND_COLOR = new Color(15, 23, 42); // Fallback dark
    public static final Color BACKGROUND_GRADIENT_START = new Color(2, 6, 23); // slate 950
    public static final Color BACKGROUND_GRADIENT_END = new Color(15, 23, 42);   // slate 900
    public static final Color HUD_OVERLAY = new Color(255, 255, 255, 10);
    
    // Glassmorphism (Dark Mode)
    public static final Color GLASS_BG = new Color(30, 41, 59, 200); // Slate 800 transparent
    public static final Color GLASS_BORDER = new Color(148, 163, 184, 50); // Slate 400 transparent
    public static final Color GLASS_HIGHLIGHT = new Color(255, 255, 255, 20);
    public static final Color GLASS_BACKGROUND = new Color(30, 41, 59, 180); // Glass background for panels
    
    // Border & Highlight Colors
    public static final Color BORDER_COLOR = new Color(148, 163, 184); // Slate 400
    public static final Color HIGHLIGHT_COLOR = new Color(255, 255, 255, 30); // White semi-transparent
    
    // Vista Aero Theme Colors
    public static final Color VISTA_AURORA_DARK = new Color(20, 30, 50, 230); // Dark aurora gradient
    public static final Color VISTA_AURORA_LIGHT = new Color(50, 80, 120, 180); // Light aurora gradient
    public static final Color AERO_TASKBAR = new Color(30, 40, 60, 200); // Aero glass taskbar effect
    
    // Windows 98 Retro Theme Colors
    public static final Color WIN98_GREY = new Color(192, 192, 192); // Classic Win98 grey
    public static final Color WIN98_TITLE_ACTIVE_START = new Color(0, 0, 128); // Win98 active title bar start
    
    // Fonts (Light text)
    public static final Color TEXT_PRIMARY = new Color(241, 245, 249); // Slate 100
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184); // Slate 400
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_RETRO_DIGITAL = new Font("Courier New", Font.BOLD, 14); // Retro digital display font
}
