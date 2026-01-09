package com.kiloux.restopos.ui;

import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.Timer;

public class AnimatedButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private Color currentColor;
    private float scale = 1.0f;
    private Timer hoverTimer;
    private boolean isHovering = false;
    
    // Ripple Effect
    private Point rippleCenter;
    private float rippleRadius = 0;
    private float maxRippleRadius = 0;
    private Timer rippleTimer;
    private float rippleAlpha = 0.3f;

    public AnimatedButton(String text, Color primary, Color secondary) {
        super(text);
        this.normalColor = primary;
        this.hoverColor = secondary;
        this.currentColor = primary;
        
        setFont(UIConfig.FONT_BODY);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                currentColor = hoverColor;
                startAnimation(1.05f);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                currentColor = normalColor;
                startAnimation(1.0f);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                scale = 0.95f;
                createRipple(e.getPoint());
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                scale = isHovering ? 1.05f : 1.0f;
                repaint();
            }
        });
    }

    private void createRipple(Point center) {
        rippleCenter = center;
        rippleRadius = 0;
        rippleAlpha = 0.3f;
        maxRippleRadius = Math.max(getWidth(), getHeight()) * 1.5f;
        
        if (rippleTimer != null && rippleTimer.isRunning()) rippleTimer.stop();
        
        rippleTimer = new Timer(15, e -> {
            rippleRadius += maxRippleRadius * 0.08f;
            if (rippleRadius >= maxRippleRadius) {
                rippleAlpha -= 0.02f;
                if (rippleAlpha <= 0) {
                    rippleTimer.stop();
                    rippleCenter = null;
                }
            }
            repaint();
        });
        rippleTimer.start();
    }

    private void startAnimation(float targetScale) {
        if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();
        
        hoverTimer = new Timer(10, e -> {
            boolean done = true;
            if (Math.abs(scale - targetScale) > 0.01) {
                scale += (targetScale - scale) * 0.2f;
                done = false;
            } else {
                scale = targetScale;
            }
            repaint();
            if (done) hoverTimer.stop();
        });
        hoverTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        int scaledW = (int) (w * scale);
        int scaledH = (int) (h * scale);
        int x = (w - scaledW) / 2;
        int y = (h - scaledH) / 2;
        
        g2.setColor(currentColor);
        g2.fillRoundRect(x, y, scaledW, scaledH, 15, 15);
        
        // Paint Ripple
        if (rippleCenter != null) {
            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(x, y, scaledW, scaledH, 15, 15));
            g2.setColor(new Color(1f, 1f, 1f, Math.max(0, rippleAlpha)));
            g2.fillOval(
                (int)(rippleCenter.x - rippleRadius), 
                (int)(rippleCenter.y - rippleRadius), 
                (int)(rippleRadius * 2), 
                (int)(rippleRadius * 2)
            );
            g2.setClip(null);
        }
        
        super.paintComponent(g);
    }
}
