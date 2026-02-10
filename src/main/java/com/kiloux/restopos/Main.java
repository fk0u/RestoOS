package com.kiloux.restopos;

import com.kiloux.restopos.gui.MainFrame;
import com.kiloux.restopos.utils.DatabaseManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        // Enable UI Scaling for High DPI displays
        System.setProperty("sun.java2d.uiScale", "2.5");
        
        // Initialize Database First
        DatabaseManager.initializeDatabase();

        // Setup Look and Feel (FlatLaf Light for Retro Theme base)
        try {
            FlatLightLaf.setup();
            UIManager.put("Panel.background", new java.awt.Color(0xD4E1CC));
            UIManager.put("OptionPane.background", new java.awt.Color(0xD4E1CC));
            UIManager.put("Button.arc", 0); // No rounded corners for default buttons
            UIManager.put("Component.arc", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
