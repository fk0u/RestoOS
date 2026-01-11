package com.kiloux.restopos.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BootPanel extends JPanel {

    private Timer timer;
    private int progress = 0;
    private Runnable onComplete;
    private float glow = 0f;
    private boolean glowUp = true;

    public BootPanel(Runnable onComplete) {
        this.onComplete = onComplete;
        setDoubleBuffered(true); // Smooth animation
        
        // Trigger Sound
        com.kiloux.restopos.utils.SoundManager.getInstance().play("startup");
        
        timer = new Timer(50, e -> update());
        timer.start();
    }
    
    private void update() {
        progress++;
        
        // Pulse Effect
        if (glowUp) {
            glow += 0.05f;
            if (glow >= 1.0f) { glow = 1.0f; glowUp = false; }
        } else {
            glow -= 0.05f;
            if (glow <= 0.0f) { glow = 0.0f; glowUp = true; }
        }
        repaint();
        
        if (progress > 120) { // ~6 seconds
            timer.stop();
            onComplete.run();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        // 1. Animated Aurora Background
        // Deep Black top, Aurora Green/Blue bottom
        Color bottomColor = new Color(0, 50, (int)(50 + glow*20)); // Pulsing blue
        GradientPaint gp = new GradientPaint(0, 0, Color.BLACK, 0, h, bottomColor);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        
        // 2. The "Energy Orb" / Flare
        // Draw a light flare in the center moving
        int flareX = (int)(w * (progress / 120.0));
        RadialGradientPaint flare = new RadialGradientPaint(
            new java.awt.geom.Point2D.Float(w/2, h/2),
            w/2,
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(0, 255, 255, (int)(glow*100)), new Color(0,0,0,0)}
        );
        g2.setPaint(flare);
        g2.fillRect(0, 0, w, h);
        
        // 3. Logo Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        String txt = "Starting Windows...";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt, (w - fm.stringWidth(txt))/2, h/2);
        
        // 4. Copyright
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.setColor(Color.GRAY);
        String copy = "© Microsoft Corporation (RestoOS Edition)";
        fm = g2.getFontMetrics();
        g2.drawString(copy, (w - fm.stringWidth(copy))/2, h - 50);
        
        // 5. Progress Bar area
        int barW = 200;
        int barH = 6;
        int barX = (w - barW)/2;
        int barY = h/2 + 40;
        
        g2.setColor(new Color(50, 50, 50));
        g2.drawRoundRect(barX, barY, barW, barH, 6, 6);
        
        // Active Bar (Indeterminate style)
        int fillW = (int)(barW * (progress/120.0));
        g2.setColor(new Color(0, 200, 100)); // Vista Green
        g2.fillRoundRect(barX, barY, fillW, barH, 6, 6);
    }
}
