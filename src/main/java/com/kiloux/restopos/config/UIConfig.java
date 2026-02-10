package com.kiloux.restopos.config;

import java.awt.Color;
import java.awt.Font;

public class UIConfig {
<<<<<<< HEAD
    // Legacy fallback size (runtime uses fullscreen sizing)
=======
    // UI Scaling - Increase this to make everything larger
    public static final float UI_SCALE = 2.5f; // 1.0 = normal, 1.5 = 150%, 2.0 = 200%, 2.5 = 250%
    
    // Retro PDA Dimensions (Optional usage, keeping legacy consts)
>>>>>>> 26f46c44ea3575f56ad27fac9a56757fe24c2b57
    public static final int WINDOW_WIDTH = 428;
    public static final int WINDOW_HEIGHT = 926;

    // Layout
<<<<<<< HEAD
    public static final int SAFE_PADDING = 24;
    public static final int SAFE_PADDING_LANDSCAPE = 16;
=======
    public static final int SAFE_PADDING = (int)(25 * UI_SCALE);
    public static final int SAFE_PADDING_LANDSCAPE = (int)(25 * UI_SCALE);
>>>>>>> 26f46c44ea3575f56ad27fac9a56757fe24c2b57

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
    
<<<<<<< HEAD
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
=======
    // Borders & Gradients
    public static final Color BORDER_COLOR = new Color(255, 255, 255, 40);
    public static final Color TITLE_START = new Color(40, 50, 60);
    public static final Color TITLE_END = new Color(20, 25, 30);

    // Fonts - Segoe UI + Ubuntu Style (Scaled)
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, (int)(24 * UI_SCALE));
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, (int)(16 * UI_SCALE));
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, (int)(14 * UI_SCALE));
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, (int)(12 * UI_SCALE));
    public static final Font FONT_RETRO_DIGITAL = new Font("Consolas", Font.BOLD, (int)(14 * UI_SCALE));
    
    // Legacy mapping
    public static final Color WIN98_GREY = new Color(46, 52, 54, 150);
    public static final Color SAGE_GREEN_DARK = SECONDARY_COLOR;
    public static final Color WIN98_TITLE_ACTIVE_START = TITLE_START;
    public static final Color WIN98_TITLE_ACTIVE_END = TITLE_END;
    public static final Color WIN98_TITLE_TEXT = TEXT_PRIMARY;
    
    public static final Color RETRO_BORDER_HIGHLIGHT = new Color(255, 255, 255, 80);
    public static final Color RETRO_BORDER_SHADOW = new Color(0, 0, 0, 150);
    public static final Color RETRO_BORDER_FACE = new Color(255, 255, 255, 10);

    public static final Color GLASS_BG = AERO_GLASS;
    public static final Color GLASS_BACKGROUND = AERO_GLASS;
    public static final Color BACKGROUND_BASE = BACKGROUND_COLOR;
    public static final Color GLASS_BORDER = new Color(255, 255, 255, 60);
    public static final Color GLASS_HIGHLIGHT = new Color(255, 255, 255, 100);
    public static final Color HUD_OVERLAY = new Color(0, 0, 0, 100);
    
    public static final Color HIGHLIGHT_COLOR = PRIMARY_COLOR; 
>>>>>>> 26f46c44ea3575f56ad27fac9a56757fe24c2b57
}
