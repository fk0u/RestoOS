package com.kiloux.restopos.apps;

import com.kiloux.restopos.ui.AeroPanel;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.io.File;
import javax.swing.*;

public class MediaPlayerApp extends JInternalFrame {
    
    private JLabel nowPlaying;
    private JProgressBar progressBar;
    private Timer progressTimer;
    private boolean isPlaying = false;
    private int progress = 0;

    public MediaPlayerApp() {
        super("Windows Media Player", true, true, true, true);
        setSize(500, 350);
        setFrameIcon(null);
        
        // Main Container (Dark Glass)
        AeroPanel panel = new AeroPanel(new BorderLayout());
        panel.setTintColor(new Color(20, 20, 20, 200));
        setContentPane(panel);
        
        // Visualizer Area
        JPanel viz = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw Waves
                g.setColor(new Color(0, 255, 100)); // Green waves
                if(isPlaying) {
                    for(int i=0; i<getWidth(); i+=10) {
                        int h = (int)(Math.random() * 50) + 20;
                        g.fillRect(i, getHeight()/2 - h/2, 5, h);
                    }
                }
            }
        };
        viz.setOpaque(true);
        panel.add(viz, BorderLayout.CENTER);
        
        // Controls
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        nowPlaying = new JLabel("No Media Selected");
        nowPlaying.setForeground(Color.WHITE);
        nowPlaying.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottom.add(nowPlaying, BorderLayout.NORTH);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setPreferredSize(new Dimension(100, 5));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        bottom.add(progressBar, BorderLayout.CENTER);
        
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btns.setOpaque(false);
        
        RetroButton openBtn = new RetroButton("Open..."); // Open
        RetroButton playBtn = new RetroButton("Play"); // Play
        RetroButton stopBtn = new RetroButton("Stop"); // Stop
        
        openBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                nowPlaying.setText("Playing: " + f.getName());
                progress = 0;
                progressBar.setValue(0);
                isPlaying = true; // Auto play
                progressTimer.start();
                viz.repaint();
            }
        });
        
        playBtn.addActionListener(e -> {
            isPlaying = !isPlaying;
            playBtn.setText(isPlaying ? "Pause" : "Play");
            if(isPlaying) progressTimer.start(); else progressTimer.stop();
            viz.repaint();
        });
        
        stopBtn.addActionListener(e -> {
            isPlaying = false;
            progress = 0;
            progressBar.setValue(0);
            progressTimer.stop();
            playBtn.setText("Play");
            viz.repaint();
        });
        
        btns.add(openBtn);
        btns.add(playBtn);
        btns.add(stopBtn);
        
        bottom.add(btns, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        
        // Timer for simulation
        progressTimer = new Timer(100, e -> {
            if (isPlaying) {
                progress++;
                if (progress > 100) progress = 0; // Loop
                progressBar.setValue(progress);
                viz.repaint(); // Animate viz
            }
        });
    }
}
