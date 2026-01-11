package com.kiloux.restopos.config;

import java.awt.Color;
import java.awt.Font;

public class UIConfig {
    // Retro PDA Dimensions (Optional usage, keeping legacy consts)
    public static final int WINDOW_WIDTH = 428;
    public static final int WINDOW_HEIGHT = 926;

    // Layout
    public static final int SAFE_PADDING = 10;
    public static final int SAFE_PADDING_LANDSCAPE = 10;

    // --- LONGHORN / VISTA / AERO THEME ---
    
    // Desktop - Slate Blue / Plex Main
    public static final Color BACKGROUND_COLOR = new Color(0x3A6EA5); 
    public static final Color PANEL_BACKGROUND = new Color(0xF0F0F0); // Light for content areas

    // Vista Glass / Aero
    public static final Color AERO_GLASS = new Color(240, 245, 255, 180); // Milky White Glass
    public static final Color AERO_TASKBAR = new Color(10, 10, 10, 220); // Dark Glass
    public static final Color AERO_START_MENU = new Color(245, 250, 255, 240); // Opaque Glass
    
    // Accents (Vista Green / Turquoise)
    public static final Color PRIMARY_COLOR = new Color(0x3CB371); // Medium Sea Green (Success/Primary)
    public static final Color SECONDARY_COLOR = new Color(0x4682B4); // Steel Blue
    public static final Color ACCENT_COLOR = new Color(0x00CED1); // Dark Turquoise
    
    public static final Color DANGER_COLOR = new Color(0xCD5C5C); // Indian Red
    public static final Color WARNING_COLOR = new Color(0xFFA500); // Orange

    // Text - ClearType
    public static final Color TEXT_PRIMARY = new Color(20, 20, 20);
    public static final Color TEXT_SECONDARY = new Color(80, 80, 80);
    
    // Borders & Gradients
    public static final Color BORDER_COLOR = new Color(160, 160, 160);
    public static final Color TITLE_START = new Color(40, 60, 80); // Dark Blue Slate
    public static final Color TITLE_END = new Color(80, 120, 160); // Lighter Slate

    // Fonts - Segoe UI Focus
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_RETRO_DIGITAL = new Font("Consolas", Font.BOLD, 14);
    
    // Legacy mapping (kept for code compatibility, but mapped to Modern)
    public static final Color WIN98_GREY = new Color(245, 245, 245); // Updated to Off-White
    public static final Color SAGE_GREEN_DARK = PRIMARY_COLOR;
    public static final Color WIN98_TITLE_ACTIVE_START = TITLE_START;
    public static final Color WIN98_TITLE_ACTIVE_END = TITLE_END;
    public static final Color WIN98_TITLE_TEXT = Color.WHITE;
    
    public static final Color RETRO_BORDER_HIGHLIGHT = Color.WHITE;
    public static final Color RETRO_BORDER_SHADOW = new Color(180, 180, 180);
    public static final Color RETRO_BORDER_FACE = PANEL_BACKGROUND;

    public static final Color GLASS_BG = AERO_GLASS;
    public static final Color GLASS_BACKGROUND = AERO_GLASS;
    public static final Color BACKGROUND_BASE = BACKGROUND_COLOR;
    public static final Color GLASS_BORDER = new Color(255, 255, 255, 100);
    public static final Color GLASS_HIGHLIGHT = new Color(255, 255, 255, 150);
    public static final Color HUD_OVERLAY = new Color(0, 0, 0, 40);
    
    // Legacy Alias
    public static final Color HIGHLIGHT_COLOR = PRIMARY_COLOR; // Green-ish
}
