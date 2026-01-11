package com.kiloux.restopos.apps;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WelcomeCenterApp extends JInternalFrame {

    public WelcomeCenterApp() {
        super("Welcome Center", true, true, true, true);
        setSize(700, 500);
        setFrameIcon(null); // No Icon
        
        // Main Container
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);
        setContentPane(main);
        
        // --- Header (Aurora Cyan) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(getWidth(), 80));
        header.setBackground(new Color(0, 50, 80)); // Fallback
        
        // Custom Paint Header
        JPanel gradientHeader = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                com.kiloux.restopos.ui.PlexUtils.drawPlexHeader((Graphics2D)g, getWidth(), getHeight());
            }
        };
        
        JLabel title = new JLabel("Welcome to RestoOS Ultimate");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        gradientHeader.add(title, BorderLayout.WEST);
        main.add(gradientHeader, BorderLayout.NORTH);
        
        // --- Content (Buttons) ---
        JPanel content = new JPanel(new GridLayout(2, 2, 20, 20));
        content.setBorder(new EmptyBorder(30, 30, 30, 30));
        content.setBackground(new Color(240, 250, 255));
        
        content.add(createActionCard("Get Started", "Launch the specific POS module for your role.", "POS"));
        content.add(createActionCard("System Specs", "View details about this computer.", "SYS"));
        content.add(createActionCard("Personalize", "Change background and colors.", "PERS"));
        content.add(createActionCard("What's New", "See the new features in Build 7000.", "NEWS"));
        
        main.add(content, BorderLayout.CENTER);
        
        // --- Footer ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(new Color(230, 240, 250));
        JCheckBox showOnStart = new JCheckBox("Run at startup");
        showOnStart.setSelected(true);
        showOnStart.setOpaque(false);
        footer.add(showOnStart);
        main.add(footer, BorderLayout.SOUTH);
    }
    
    private JPanel createActionCard(String title, String desc, String actionCmd) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Icon (Fake)
        JPanel icon = new JPanel();
        icon.setPreferredSize(new Dimension(50, 50));
        icon.setBackground(UIConfig.PRIMARY_COLOR);
        p.add(icon, BorderLayout.WEST);
        
        // Text
        JPanel txt = new JPanel(new GridLayout(2, 1));
        txt.setOpaque(false);
        txt.setBorder(new EmptyBorder(0, 10, 0, 0));
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(new Color(0, 50, 80));
        
        JTextArea d = new JTextArea(desc);
        d.setWrapStyleWord(true);
        d.setLineWrap(true);
        d.setOpaque(false);
        d.setEditable(false);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        d.setForeground(Color.GRAY);
        
        txt.add(t);
        txt.add(d);
        p.add(txt, BorderLayout.CENTER);
        
        // Hover
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                p.setBackground(new Color(220, 240, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                p.setBackground(Color.WHITE);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleAction(actionCmd);
            }
        });
        
        return p;
    }
    
    private void handleAction(String cmd) {
        if ("POS".equals(cmd)) {
            JOptionPane.showMessageDialog(this, "Please use the Dock to launch POS.");
        } else if ("SYS".equals(cmd)) {
            JOptionPane.showMessageDialog(this, "RestoOS Ultimate 64-bit\n8GB RAM\nIntel Core 2 Duo");
        } else if ("NEWS".equals(cmd)) {
            JOptionPane.showMessageDialog(this, "- New Aurora Boot Screen\n- Hybrid Dock\n- Real SQL Integration");
        }
    }
}
