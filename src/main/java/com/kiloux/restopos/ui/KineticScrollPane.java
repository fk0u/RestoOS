package com.kiloux.restopos.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KineticScrollPane extends JScrollPane {

    private Point lastPoint;
    private JViewport viewport;
    private Timer kineticTimer;
    private double velocityY = 0;
    
    public KineticScrollPane(Component view) {
        super(view);
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(null);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Hide scrollbar for immersion
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        viewport = getViewport();
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
                if (kineticTimer != null && kineticTimer.isRunning()) {
                    kineticTimer.stop();
                }
                velocityY = 0;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getPoint();
                int dy = currentPoint.y - lastPoint.y;
                
                Point viewPos = viewport.getViewPosition();
                viewPos.y -= dy;
                
                // Bounds check
                limitViewPosition(viewPos);
                viewport.setViewPosition(viewPos);
                
                velocityY = dy; // Track velocity
                lastPoint = currentPoint;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startKineticScroll();
            }
        };
        
        viewport.getView().addMouseListener(ma);
        viewport.getView().addMouseMotionListener(ma);
        
        // Also add to self if click lands on container
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
    
    // Add listener to children recursively causing issues? 
    // Usually better to use a glass pane approach or check event sources,
    // but for this simple grid, attaching to the View panel (gridContainer) is usually enough.
    
    private void startKineticScroll() {
        kineticTimer = new Timer(16, e -> {
            if (Math.abs(velocityY) < 1) {
                kineticTimer.stop();
                return;
            }
            
            Point viewPos = viewport.getViewPosition();
            viewPos.y -= velocityY;
            
            // Limit bounds
            boolean hitEdge = limitViewPosition(viewPos);
            viewport.setViewPosition(viewPos);
            
            if (hitEdge) {
                velocityY = 0;
                kineticTimer.stop();
            } else {
                velocityY *= 0.95; // Friction
            }
        });
        kineticTimer.start();
    }
    
    private boolean limitViewPosition(Point p) {
        boolean hit = false;
        int maxY = viewport.getView().getHeight() - viewport.getHeight();
        
        if (p.y < 0) {
            p.y = 0;
            hit = true;
        } else if (p.y > maxY) {
            if (maxY < 0) p.y = 0;
            else p.y = maxY;
            hit = true;
        }
        return hit;
    }
}
