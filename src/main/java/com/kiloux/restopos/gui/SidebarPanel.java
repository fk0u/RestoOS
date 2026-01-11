package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import javax.swing.*;

public class SidebarPanel extends JPanel {

    public SidebarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 0)); // Transparent
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(160, 0)); // Sidebar width
        
        // Clock Gadget
        add(createClock());
        add(Box.createVerticalStrut(15));
        
        // Calendar Gadget
        add(createCalendar());
        add(Box.createVerticalStrut(15));
        
        // CPU/Performance Gadget
        add(createPerformance());
        
        // Push everything up
        add(Box.createVerticalGlue());
    }
    
    private JPanel createGadgetPanel() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 100)); // Dark Glass
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Shine
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight()/2, 20, 20));
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return p;
    }
    
    private JPanel createClock() {
        JPanel p = createGadgetPanel();
        p.setPreferredSize(new Dimension(140, 140));
        
        JLabel time = new JLabel("00:00");
        time.setFont(new Font("Segoe UI", Font.BOLD, 28));
        time.setForeground(Color.WHITE);
        time.setHorizontalAlignment(SwingConstants.CENTER);
        
        new Timer(1000, e -> {
            time.setText(java.time.format.DateTimeFormatter.ofPattern("HH:mm").format(LocalDateTime.now()));
        }).start();
        
        p.add(time, BorderLayout.CENTER);
        return p;
    }
    
    private JPanel createCalendar() {
        JPanel p = createGadgetPanel();
        p.setPreferredSize(new Dimension(140, 160));
        
        JLabel month = new JLabel(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy").format(LocalDateTime.now()));
        month.setForeground(Color.ORANGE);
        month.setFont(new Font("Segoe UI", Font.BOLD, 14));
        month.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel day = new JLabel(String.valueOf(LocalDateTime.now().getDayOfMonth()));
        day.setForeground(Color.WHITE);
        day.setFont(new Font("Segoe UI", Font.BOLD, 48));
        day.setHorizontalAlignment(SwingConstants.CENTER);
        
        p.add(month, BorderLayout.NORTH);
        p.add(day, BorderLayout.CENTER);
        return p;
    }
    
    private JPanel createPerformance() {
         JPanel p = createGadgetPanel();
         p.setPreferredSize(new Dimension(140, 100));
         
         JLabel title = new JLabel("CPU Usage");
         title.setForeground(Color.LIGHT_GRAY);
         p.add(title, BorderLayout.NORTH);
         
         JProgressBar bar = new JProgressBar(0, 100);
         bar.setValue(25);
         bar.setStringPainted(true);
         
         new Timer(2000, e -> {
             bar.setValue((int)(Math.random() * 40) + 10);
         }).start();
         
         p.add(bar, BorderLayout.CENTER);
         return p;
    }
}
