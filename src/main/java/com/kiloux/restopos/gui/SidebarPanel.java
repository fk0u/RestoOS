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
        setPreferredSize(new Dimension(170, 0)); // Sidebar width
        
        // Clock Gadget
        add(createAnalogClock());
        add(Box.createVerticalStrut(15));
        
        // Calendar Gadget
        add(createCalendar());
        add(Box.createVerticalStrut(15));
        
        // Performance Gadget
        add(createPerformance());
        add(Box.createVerticalStrut(15));
        
        // Slideshow Gadget
        add(createSlideshow());
        
        // Push everything up
        add(Box.createVerticalGlue());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // Draw the Sidebar Channel (Dark Vertical Bar)
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.SrcOver);
        g2.setColor(new Color(0, 0, 0, 80)); // Very subtle dark shade
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
    
    private JPanel createGadgetPanel() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Vista Gadget Glass BG (Darker)
                g2.setColor(new Color(10, 20, 30, 150)); 
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Shine
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight()/2, 20, 20));
                
                // Border
                g2.setColor(new Color(255, 255, 255, 60));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20));
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return p;
    }
    
    // Analog Clock
    private JPanel createAnalogClock() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Transparency only
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(150, 150));
        
        // Self-painting clock
        JPanel clockFace = new JPanel() {
             @Override
             protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g;
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 int w = getWidth(); 
                 int h = getHeight();
                 int cx = w/2; int cy = h/2;
                 int r = Math.min(w, h)/2 - 5;
                 
                 // Face
                 g2.setColor(new Color(20, 20, 20, 200));
                 g2.fillOval(cx-r, cy-r, r*2, r*2);
                 g2.setColor(Color.WHITE);
                 g2.setStroke(new BasicStroke(2));
                 g2.drawOval(cx-r, cy-r, r*2, r*2);
                 
                 // Hands
                 LocalDateTime now = LocalDateTime.now();
                 double sec = now.getSecond() * 6 - 90;
                 double min = now.getMinute() * 6 - 90;
                 double hour = (now.getHour()%12) * 30 + (now.getMinute()/2.0) - 90;
                 
                 drawHand(g2, cx, cy, sec, r-10, Color.RED, 1);
                 drawHand(g2, cx, cy, min, r-15, Color.WHITE, 3);
                 drawHand(g2, cx, cy, hour, r-30, Color.WHITE, 5);
             }
             
             private void drawHand(Graphics2D g, int x, int y, double deg, int len, Color c, int w) {
                 g.setColor(c);
                 g.setStroke(new BasicStroke(w));
                 g.drawLine(x, y, (int)(x + len*Math.cos(Math.toRadians(deg))), (int)(y + len*Math.sin(Math.toRadians(deg))));
             }
        };
        clockFace.setOpaque(false);
        clockFace.setPreferredSize(new Dimension(140, 140));
        p.add(clockFace);
        
        new Timer(1000, e -> clockFace.repaint()).start();
        return p;
    }
    
    private JPanel createCalendar() {
        JPanel p = createGadgetPanel();
        p.setPreferredSize(new Dimension(140, 160));
        
        JLabel month = new JLabel(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy").format(LocalDateTime.now()));
        month.setForeground(new Color(255, 140, 0)); // Orange
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
         bar.setBackground(new Color(0,0,0,0));
         bar.setForeground(new Color(60, 180, 113));
         
         new Timer(2000, e -> {
             bar.setValue((int)(Math.random() * 40) + 10);
         }).start();
         
         p.add(bar, BorderLayout.CENTER);
         return p;
    }
    
    private JPanel createSlideshow() {
        JPanel p = createGadgetPanel();
        p.setPreferredSize(new Dimension(140, 120));
        p.setLayout(new BorderLayout());
        
        JLabel img = new JLabel("Picture View");
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setForeground(Color.GRAY);
        img.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        
        // Mock image area
        JPanel pic = new JPanel();
        pic.setBackground(Color.BLACK);
        pic.add(img);
        
        p.add(pic, BorderLayout.CENTER);
        return p;
    }
}
