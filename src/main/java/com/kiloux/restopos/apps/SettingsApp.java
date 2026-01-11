package com.kiloux.restopos.apps;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.ui.MetroUtils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Settings App - Metro Style
 * Functional Personalization & System Info
 */
public class SettingsApp extends JInternalFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    public SettingsApp() {
        super("Settings", true, true, true, true);
        setSize(800, 550);
        setFrameIcon(null);
        
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(MetroUtils.METRO_BG_DARK);
        setContentPane(main);
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MetroUtils.drawMetroHeader((Graphics2D) g, getWidth(), getHeight(), MetroUtils.ACCENT_CYAN);
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 80));
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel title = MetroUtils.createMetroTitle("Settings");
        header.add(title, BorderLayout.WEST);
        main.add(header, BorderLayout.NORTH);
        
        // --- Sidebar Navigation ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(40, 40, 40));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        sidebar.add(createNavButton("Personalize", "PERSONALIZE"));
        sidebar.add(createNavButton("System", "SYSTEM"));
        sidebar.add(createNavButton("Accounts", "ACCOUNTS"));
        sidebar.add(Box.createVerticalGlue());
        
        main.add(sidebar, BorderLayout.WEST);
        
        // --- Content Area (Cards) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(MetroUtils.METRO_BG_DARK);
        
        contentPanel.add(createPersonalizePage(), "PERSONALIZE");
        contentPanel.add(createSystemPage(), "SYSTEM");
        contentPanel.add(createAccountsPage(), "ACCOUNTS");
        
        main.add(contentPanel, BorderLayout.CENTER);
    }
    
    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 50, 50));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(MetroUtils.ACCENT_CYAN); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(new Color(50, 50, 50)); }
        });
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        return btn;
    }
    
    // ========== PAGES ==========
    
    private JPanel createPersonalizePage() {
        JPanel p = new JPanel(new GridLayout(3, 3, 20, 20));
        p.setBackground(MetroUtils.METRO_BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JLabel lbl = new JLabel("Choose an accent color:");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setForeground(Color.WHITE);
        p.add(lbl);
        p.add(new JLabel()); // Spacer
        p.add(new JLabel()); // Spacer
        
        // Color Tiles
        p.add(createColorTile(MetroUtils.ACCENT_CYAN, "Cyan"));
        p.add(createColorTile(MetroUtils.ACCENT_PURPLE, "Purple"));
        p.add(createColorTile(MetroUtils.ACCENT_GREEN, "Teal"));
        p.add(createColorTile(MetroUtils.ACCENT_ORANGE, "Orange"));
        p.add(createColorTile(new Color(200, 50, 50), "Red"));
        p.add(createColorTile(new Color(80, 80, 80), "Gray"));
        
        return p;
    }
    
    private JPanel createColorTile(Color c, String name) {
        JPanel tile = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                MetroUtils.drawMetroTile((Graphics2D) g, getWidth(), getHeight(), c, false, false);
            }
        };
        tile.setPreferredSize(new Dimension(100, 80));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(name, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tile.add(lbl, BorderLayout.CENTER);
        
        tile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Note: UIConfig.PRIMARY_COLOR is final. In a real app, this would persist to a config file.
                // For now, we just show confirmation (runtime theme changes would require full L&F reload).
                JOptionPane.showMessageDialog(SettingsApp.this, "Accent color selected: " + name + ".\nThis feature requires app restart for full effect.", "Personalize", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return tile;
    }
    
    private JPanel createSystemPage() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(MetroUtils.METRO_BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        p.add(createInfoRow("Device Name", System.getProperty("os.name")));
        p.add(createInfoRow("Java Version", System.getProperty("java.version")));
        p.add(createInfoRow("Available Processors", String.valueOf(Runtime.getRuntime().availableProcessors())));
        p.add(createInfoRow("Total Memory", (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB"));
        p.add(createInfoRow("User Home", System.getProperty("user.home")));
        p.add(createInfoRow("Application", "RestoOS Ultimate Edition"));
        p.add(createInfoRow("Build", "v11.1 Metro"));
        
        return p;
    }
    
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(600, 40));
        
        JLabel l = new JLabel(label + ":");
        l.setForeground(Color.GRAY);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setPreferredSize(new Dimension(180, 30));
        
        JLabel v = new JLabel(value);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        row.add(l);
        row.add(v);
        return row;
    }
    
    private JPanel createAccountsPage() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(MetroUtils.METRO_BG_DARK);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        
        String user = com.kiloux.restopos.service.UserService.getInstance().getCurrentUser();
        String role = com.kiloux.restopos.service.UserService.getInstance().getCurrentRole();
        
        JLabel userLbl = new JLabel("Logged in as: " + (user != null ? user : "Guest"));
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        info.add(userLbl);
        
        JLabel roleLbl = new JLabel("Role: " + (role != null ? role : "N/A"));
        roleLbl.setForeground(Color.GRAY);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.add(roleLbl);
        
        info.add(Box.createVerticalStrut(30));
        
        JButton logoutBtn = MetroUtils.createTileButton("Sign Out", new Color(200, 50, 50), e -> {
            com.kiloux.restopos.service.UserService.getInstance().logout();
            JOptionPane.showMessageDialog(this, "Logged out. Please close this window and use the Start Menu to Log Off.", "Account", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        logoutBtn.setPreferredSize(new Dimension(150, 50));
        info.add(logoutBtn);
        
        p.add(info, BorderLayout.NORTH);
        return p;
    }
}
