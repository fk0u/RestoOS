package com.kiloux.restopos.gui;

import com.kiloux.restopos.apps.*;
import com.kiloux.restopos.ui.MetroUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Metro Start Screen - Windows 8 Style Full-Screen Tile Launcher
 */
public class StartScreenOverlay extends JPanel {

    private MainFrame mainFrame;
    private JDesktopPane desktop;
    private Runnable onClose;
    
    public StartScreenOverlay(MainFrame mainFrame, JDesktopPane desktop, Runnable onClose) {
        this.mainFrame = mainFrame;
        this.desktop = desktop;
        this.onClose = onClose;
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Semi-transparent background
        JPanel bg = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 120, 180, 240)); // Metro Blue
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setBorder(new EmptyBorder(60, 80, 60, 80));
        add(bg, BorderLayout.CENTER);
        
        // --- Header ---
        JLabel title = new JLabel("Start");
        title.setFont(new Font("Segoe UI Light", Font.PLAIN, 48));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 30, 0));
        bg.add(title, BorderLayout.NORTH);
        
        // --- Tile Grid ---
        JPanel tileGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        tileGrid.setOpaque(false);
        
        // App Tiles
        tileGrid.add(createTile("POS", MetroUtils.ACCENT_GREEN, e -> {
            close();
            // Open POS (handled by MainFrame)
        }));
        tileGrid.add(createTile("Admin", MetroUtils.ACCENT_PURPLE, e -> {
            close();
        }));
        tileGrid.add(createTile("Kitchen", MetroUtils.ACCENT_ORANGE, e -> {
            close();
        }));
        tileGrid.add(createTile("Computer", new Color(60, 60, 60), e -> {
            close();
            ExplorerApp app = new ExplorerApp();
            showApp(app);
        }));
        tileGrid.add(createTile("Settings", new Color(80, 80, 80), e -> {
            close();
            SettingsApp app = new SettingsApp();
            showApp(app);
        }));
        tileGrid.add(createTile("Task Mgr", new Color(100, 65, 165), e -> {
            close();
            TaskManagerApp app = new TaskManagerApp(desktop);
            showApp(app);
        }));
        tileGrid.add(createTile("Notepad", new Color(50, 50, 50), e -> {
            close();
            NotepadApp app = new NotepadApp();
            showApp(app);
        }));
        tileGrid.add(createTile("Calculator", new Color(70, 70, 70), e -> {
            close();
            CalculatorApp app = new CalculatorApp();
            showApp(app);
        }));
        
        bg.add(tileGrid, BorderLayout.CENTER);
        
        // --- Footer ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(30, 0, 0, 0));
        
        JLabel userLbl = new JLabel("👤 " + (com.kiloux.restopos.service.UserService.getInstance().getCurrentUser() != null 
            ? com.kiloux.restopos.service.UserService.getInstance().getCurrentUser() 
            : "Guest"));
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLbl.setForeground(Color.WHITE);
        footer.add(userLbl);
        
        JButton shutdownBtn = new JButton("⏻ Shut Down");
        shutdownBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        shutdownBtn.setForeground(Color.WHITE);
        shutdownBtn.setContentAreaFilled(false);
        shutdownBtn.setBorderPainted(false);
        shutdownBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        shutdownBtn.addActionListener(e -> {
            close();
            // Trigger shutdown
            CardLayout cl = (CardLayout) mainFrame.getContentPane().getLayout();
            cl.show(mainFrame.getContentPane(), "SHUTDOWN");
            for (Component c : mainFrame.getContentPane().getComponents()) {
                if (c instanceof ShutdownPanel) {
                    ((ShutdownPanel) c).startShutdown();
                    break;
                }
            }
        });
        footer.add(Box.createHorizontalStrut(50));
        footer.add(shutdownBtn);
        
        bg.add(footer, BorderLayout.SOUTH);
        
        // Close on click outside
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });
        
        // ESC to close
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        getActionMap().put("close", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { close(); }
        });
    }
    
    private JPanel createTile(String text, Color color, ActionListener action) {
        JPanel tile = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MetroUtils.drawMetroTile((Graphics2D) g, getWidth(), getHeight(), color, false, false);
            }
        };
        tile.setPreferredSize(new Dimension(150, 120));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        tile.add(lbl, BorderLayout.CENTER);
        
        tile.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(null);
            }
            public void mouseEntered(MouseEvent e) { tile.repaint(); }
        });
        
        return tile;
    }
    
    private void showApp(JInternalFrame app) {
        if (desktop != null) {
            desktop.add(app);
            app.setVisible(true);
            app.setLocation(100 + (int)(Math.random()*50), 100);
            desktop.moveToFront(app);
        }
    }
    
    private void close() {
        if (onClose != null) onClose.run();
    }
}
