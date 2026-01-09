package com.kiloux.restopos.ui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class Toast extends JWindow {

    private final String message;
    
    public enum Type {
        SUCCESS, INFO, ERROR, WARNING
    }

    public Toast(JFrame owner, String message, Type type) {
        super(owner);
        this.message = message;
        setAlwaysOnTop(true);
        setBackground(new Color(0,0,0,0));
        
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg;
                switch (type) {
                    case SUCCESS: bg = UIConfig.PRIMARY_COLOR; break;
                    case ERROR: bg = UIConfig.DANGER_COLOR; break;
                    case WARNING: bg = UIConfig.WARNING_COLOR; break;
                    default: bg = new Color(50, 50, 50); break;
                }
                
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Inner highlight
                g2.setColor(new Color(255,255,255,50));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
            }
        };
        content.setOpaque(false);
        content.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Padding
        
        JLabel lbl = new JLabel(message);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(UIConfig.FONT_BODY);
        content.add(lbl);
        
        add(content);
        pack();
        
        // Positioning logic
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - getWidth()) / 2;
        int y = dim.height - 100; // Bottom center
        setLocation(x, y);
    }

    public void showToast() {
        setOpacity(0f);
        setVisible(true);

        // Fade In
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            float opacity = getOpacity();
            opacity += 0.1f;
            if (opacity >= 0.9f) {
                setOpacity(0.9f);
                timer.stop();
                
                // Wait and Fade Out
                new Timer(2000, evt -> fadeOut()).start();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }
    
    private void fadeOut() {
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            float opacity = getOpacity();
            opacity -= 0.05f;
            if (opacity <= 0f) {
                setOpacity(0f);
                timer.stop();
                dispose();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }
    
    public static void show(JFrame owner, String msg, Type type) {
        new Toast(owner, msg, type).showToast();
    }
}
