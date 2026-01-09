package com.kiloux.restopos;

import com.kiloux.restopos.gui.MainFrame;
import com.kiloux.restopos.utils.DatabaseManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Initialize Database First
        DatabaseManager.initializeDatabase();

        // Setup Look and Feel (System default usually best for basic Swing if not using FlatLaf)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
