package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import java.awt.*;
import javax.swing.*;

public class OnboardingPanel extends JPanel {
    
    private MainFrame mainFrame;

    public OnboardingPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout());
        setOpaque(false);
        
        GlassPanel centerPanel = new GlassPanel(new GridBagLayout());
        centerPanel.setBorderRadius(30);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel welcomeLabel = new JLabel("Selamat datang di");
        welcomeLabel.setFont(UIConfig.FONT_TITLE);
        welcomeLabel.setForeground(UIConfig.TEXT_SECONDARY);
        centerPanel.add(welcomeLabel, gbc);
        
        gbc.gridy++;
        JLabel brandLabel = new JLabel("Kou Restaurant!");
        brandLabel.setFont(UIConfig.FONT_HEADER);
        brandLabel.setForeground(UIConfig.PRIMARY_COLOR);
        centerPanel.add(brandLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(40, 10, 20, 10);
        JLabel askLabel = new JLabel("Mau makan di mana?");
        askLabel.setFont(UIConfig.FONT_BODY);
        askLabel.setForeground(UIConfig.TEXT_PRIMARY);
        centerPanel.add(askLabel, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        AnimatedButton dineInBtn = new AnimatedButton("MAKAN DI SINI (Dine In)", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        dineInBtn.setPreferredSize(new Dimension(280, 50));
        dineInBtn.addActionListener(e -> mainFrame.showCard("TABLE_SELECT"));
        centerPanel.add(dineInBtn, gbc);
        
        gbc.gridy++;
        AnimatedButton takeAwayBtn = new AnimatedButton("BAWA PULANG (Takeaway)", UIConfig.ACCENT_COLOR, UIConfig.ACCENT_COLOR.darker());
        takeAwayBtn.setPreferredSize(new Dimension(280, 50));
        takeAwayBtn.addActionListener(e -> mainFrame.showCard("MENU"));
        centerPanel.add(takeAwayBtn, gbc);

        // Center the glass panel in the main view
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        container.add(centerPanel);
        
        add(container, BorderLayout.CENTER);
    }
}
