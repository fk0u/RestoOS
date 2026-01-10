package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import java.awt.*;
import javax.swing.*;

public class OrderProcessPanel extends JPanel {
    private MainFrame mainFrame;
    private JLabel queueLabel;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public OrderProcessPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GlassPanel center = new GlassPanel(new GridBagLayout());
        center.setBorderRadius(40);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel title = new JLabel("Order Confirmed!");
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.PRIMARY_COLOR);
        center.add(title, gbc);
        
        gbc.gridy++;
        queueLabel = new JLabel("Your Queue Number:");
        queueLabel.setFont(UIConfig.FONT_TITLE);
        queueLabel.setForeground(Color.WHITE);
        center.add(queueLabel, gbc);
        
        gbc.gridy++;
        // Large Queue Number display
        JLabel qNumDisplay = new JLabel("#---"); 
        qNumDisplay.setFont(new Font("Segoe UI", Font.BOLD, 80));
        qNumDisplay.setForeground(UIConfig.ACCENT_COLOR);
        this.queueLabel = qNumDisplay; // Re-assign field to this label for updating
        center.add(qNumDisplay, gbc);
        
        gbc.gridy++;
        statusLabel = new JLabel("Status: Waiting for Kitchen...");
        statusLabel.setForeground(UIConfig.TEXT_SECONDARY);
        statusLabel.setFont(UIConfig.FONT_BODY);
        center.add(statusLabel, gbc);
        
        gbc.gridy++;
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 10));
        center.add(progressBar, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(40, 10, 10, 10);
        AnimatedButton homeBtn = new AnimatedButton("Back to Home", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        homeBtn.setPreferredSize(new Dimension(200, 50));
        homeBtn.addActionListener(e -> mainFrame.showCard("ONBOARDING"));
        center.add(homeBtn, gbc);
        
        add(center, BorderLayout.CENTER);
    }
    
    public void setQueueNumber(int num) {
        queueLabel.setText(String.format("#%03d", num));
        statusLabel.setText("Status: Sent to Kitchen");
    }
}
