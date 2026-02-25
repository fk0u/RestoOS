package com.kiloux.restopos.apps;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.gui.MainFrame;
import com.kiloux.restopos.ui.PlexUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Control Panel - Central Settings Hub
 */
public class ControlPanelApp extends JInternalFrame {

    private JDesktopPane desktop;
    
    public ControlPanelApp(JDesktopPane desktop) {
        super("Control Panel", true, true, true, true);
        this.desktop = desktop;
        setSize(700, 500);
        setFrameIcon(null);
        
        setLayout(new BorderLayout());
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBackground(new Color(30, 30, 35)); // Dark header
        
        JLabel title = new JLabel("Adjust your computer's settings");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        title.setForeground(UIConfig.TEXT_PRIMARY); // White text
        title.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        header.add(title, BorderLayout.CENTER);
        
        add(header, BorderLayout.NORTH);
        
        // Grid Content
        JPanel grid = new JPanel(new GridLayout(3, 3, 20, 20));
        grid.setBackground(new Color(10, 15, 20)); // Dark background
        grid.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        grid.add(createIcon("System and Security", "Resource Monitor, Firewall", e -> {
            desktop.add(new TaskManagerApp(desktop)).setVisible(true);
        }));
        
        grid.add(createIcon("User Accounts", "Change account type", e -> {
               MainFrame owner = null;
               java.awt.Container top = desktop.getTopLevelAncestor();
               if (top instanceof MainFrame) owner = (MainFrame) top;
               SettingsApp s = new SettingsApp(owner);
             s.setVisible(true);
             desktop.add(s);
             try { s.setSelected(true); } catch(Exception ex){}
        }));
        
        grid.add(createIcon("Appearance", "Display, Personalization", e -> {
               MainFrame owner = null;
               java.awt.Container top = desktop.getTopLevelAncestor();
               if (top instanceof MainFrame) owner = (MainFrame) top;
               SettingsApp s = new SettingsApp(owner);
             s.setVisible(true);
             desktop.add(s);
        }));
        
        grid.add(createIcon("Network", "View network status", e -> 
            JOptionPane.showMessageDialog(this, "Connected: Ethernet (1Gbps)\nIPv4: 192.168.1.105")
        ));
        
        grid.add(createIcon("RestoPOS Settings", "Database, Printer", e -> 
            JOptionPane.showMessageDialog(this, "Database: MySQL (Connected)\nPrinter: Thermal (Simulated)")
        ));
        
        grid.add(createIcon("Programs", "Uninstall a program", e -> {}));
        
        add(grid, BorderLayout.CENTER);
    }
    
    private JPanel createIcon(String title, String desc, ActionListener action) {
        JPanel p = new JPanel(new BorderLayout(10, 5));
        p.setOpaque(false);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Icon (Draw placeholder)
        JPanel icon = new JPanel() {
             @Override protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g;
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 g2.setColor(new Color(0, 120, 212));
                 g2.fillOval(5, 5, 40, 40);
             }
        };
        icon.setPreferredSize(new Dimension(50, 50));
        icon.setOpaque(false);
        
        // Text
        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(UIConfig.TEXT_PRIMARY); // White
        
        JLabel d = new JLabel(desc);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        d.setForeground(UIConfig.TEXT_SECONDARY); // Light Grey
        
        txt.add(t);
        txt.add(d);
        
        p.add(icon, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        
        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
        });
        
        return p;
    }
}
