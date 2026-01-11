package com.kiloux.restopos.gui;

import com.kiloux.restopos.utils.SoundManager;
import java.awt.*;
import java.awt.geom.FlatteningPathIterator;
import javax.swing.*;

public class ShutdownPanel extends JPanel {

    private Timer timer;
    private float alpha = 0.0f; // 0 to 1 (Black overlay)
    private int angle = 0; // Spinner
    private boolean isRunning = false;
    
    public ShutdownPanel() {
        setBackground(new Color(0, 50, 80)); // Vista Aurora Base
    }
    
    public void startShutdown() {
        isRunning = true;
        SoundManager.getInstance().play("shutdown");
        
        // 5 Second Timer (approx sound length)
        timer = new Timer(50, e -> {
            angle += 15;
            
            // Fade out starts at 3s
            if (angle > 3000 / 50 * 15) { // after ~3s
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    timer.stop();
                    // Black Screen wait
                    try { Thread.sleep(1000); } catch(Exception ex){}
                    System.exit(0);
                }
            }
            repaint();
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        if (isRunning) {
            // Background (Aurora Snapshot)
             GradientPaint gp = new GradientPaint(0, 0, new Color(10, 30, 50), 0, h, new Color(0, 10, 20));
             g2.setPaint(gp);
             g2.fillRect(0, 0, w, h);
             
             // Text
             g2.setColor(Color.WHITE);
             g2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
             String txt = "Shutting down...";
             FontMetrics fm = g2.getFontMetrics();
             g2.drawString(txt, (w - fm.stringWidth(txt))/2, h/2 - 20);
             
             // Spinner (Simple Circle Dots)
             int cx = w/2;
             int cy = h/2 + 30;
             int r = 15;
             g2.translate(cx, cy);
             g2.rotate(Math.toRadians(angle));
             for (int i = 0; i < 8; i++) {
                 g2.setColor(new Color(255, 255, 255, 255 - (i * 30)));
                 g2.fillOval(r, -3, 6, 6);
                 g2.rotate(Math.toRadians(45));
             }
             g2.rotate(-Math.toRadians(angle)); // Reset
             g2.translate(-cx, -cy);
             
             // Fade Overlay
             if (alpha > 0) {
                 g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                 g2.fillRect(0, 0, w, h);
             }
        }
    }
}
