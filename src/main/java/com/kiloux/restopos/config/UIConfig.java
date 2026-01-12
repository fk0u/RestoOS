package com.kiloux.restopos.config;

import java.awt.Color;
import java.awt.Font;

public class UIConfig {
    // UI Scaling - Increase this to make everything larger
    public static final float UI_SCALE = 2.5f; // 1.0 = normal, 1.5 = 150%, 2.0 = 200%, 2.5 = 250%
    
    // Retro PDA Dimensions (Optional usage, keeping legacy consts)
    public static final int WINDOW_WIDTH = 428;
    public static final int WINDOW_HEIGHT = 926;

    // Layout
    public static final int SAFE_PADDING = (int)(25 * UI_SCALE);
    public static final int SAFE_PADDING_LANDSCAPE = (int)(25 * UI_SCALE);

    // --- AERO-GNOME HYBRID THEME (PREMIUM DARK) ---
    
    // Desktop - Deep Aurora
    public static final Color BACKGROUND_COLOR = new Color(10, 15, 20); // Deep Space
    public static final Color VISTA_AURORA_DARK = new Color(5, 10, 15);
    public static final Color VISTA_AURORA_LIGHT = new Color(25, 45, 65);
    
    // Panels - Glassy Dark (Adwaita/Aero mix)
    public static final Color PANEL_BACKGROUND = new Color(30, 30, 35, 200);

    // Glass - Premium
    public static final Color AERO_GLASS = new Color(20, 25, 30, 180); // Dark Smokey Glass
    public static final Color AERO_TASKBAR = new Color(10, 10, 10, 220); // Almost Black
    public static final Color AERO_START_MENU = new Color(25, 25, 30, 240); 
    
    // Accents - Ubuntu/Vista Hybrid
    public static final Color PRIMARY_COLOR = new Color(52, 101, 164); // Gnome Blue
    public static final Color SECONDARY_COLOR = new Color(115, 210, 22); // Chameleon Green
    public static final Color ACCENT_COLOR = new Color(245, 121, 0); // Ubuntu Orange
    
    public static final Color DANGER_COLOR = new Color(204, 0, 0); // Gnome Red
    public static final Color WARNING_COLOR = new Color(237, 212, 0); // Gnome Yellow

    // Text - ClearType White
    public static final Color TEXT_PRIMARY = new Color(238, 238, 236); // Off-White
    public static final Color TEXT_SECONDARY = new Color(186, 189, 182); // Light Grey
    public static final Color TEXT_DARK = new Color(20, 20, 20); 
    
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
}
