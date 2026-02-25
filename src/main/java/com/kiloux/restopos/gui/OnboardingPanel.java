package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;

public class OnboardingPanel extends javax.swing.JPanel {

    private MainFrame mainFrame;

    public OnboardingPanel(MainFrame frame) {
        this.mainFrame = frame;
        initComponents();
        setOpaque(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        containerPanel = new javax.swing.JPanel();
        formPanel = new com.kiloux.restopos.ui.GlassPanel(new java.awt.GridBagLayout());
        welcomeLabel = new javax.swing.JLabel();
        brandLabel = new javax.swing.JLabel();
        askLabel = new javax.swing.JLabel();
        dineInBtn = new com.kiloux.restopos.ui.AnimatedButton("MAKAN DI SINI (Dine In)", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        takeAwayBtn = new com.kiloux.restopos.ui.AnimatedButton("BAWA PULANG (Takeaway)", UIConfig.ACCENT_COLOR, UIConfig.ACCENT_COLOR.darker());

        setLayout(new java.awt.BorderLayout());

        containerPanel.setOpaque(false);
        containerPanel.setLayout(new java.awt.GridBagLayout());

        formPanel.setBorderRadius(30);

        welcomeLabel.setFont(UIConfig.FONT_TITLE);
        welcomeLabel.setForeground(UIConfig.TEXT_SECONDARY);
        welcomeLabel.setText("Selamat datang di");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        formPanel.add(welcomeLabel, gridBagConstraints);

        brandLabel.setFont(UIConfig.FONT_HEADER);
        brandLabel.setForeground(UIConfig.PRIMARY_COLOR);
        brandLabel.setText("Kou Restaurant!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        formPanel.add(brandLabel, gridBagConstraints);

        askLabel.setFont(UIConfig.FONT_BODY);
        askLabel.setForeground(UIConfig.TEXT_PRIMARY);
        askLabel.setText("Mau makan di mana?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(40, 10, 20, 10);
        formPanel.add(askLabel, gridBagConstraints);

        dineInBtn.setPreferredSize(new java.awt.Dimension(280, 50));
        dineInBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dineInBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        formPanel.add(dineInBtn, gridBagConstraints);

        takeAwayBtn.setPreferredSize(new java.awt.Dimension(280, 50));
        takeAwayBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeAwayBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        formPanel.add(takeAwayBtn, gridBagConstraints);

        containerPanel.add(formPanel, new java.awt.GridBagConstraints());

        add(containerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void dineInBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dineInBtnActionPerformed
        if (mainFrame != null) {
            mainFrame.showCard("TABLE_SELECT");
        }
    }//GEN-LAST:event_dineInBtnActionPerformed

    private void takeAwayBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeAwayBtnActionPerformed
        if (mainFrame != null) {
            mainFrame.showCard("MENU");
        }
    }//GEN-LAST:event_takeAwayBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel askLabel;
    private javax.swing.JLabel brandLabel;
    private javax.swing.JPanel containerPanel;
    private com.kiloux.restopos.ui.AnimatedButton dineInBtn;
    private com.kiloux.restopos.ui.GlassPanel formPanel;
    private com.kiloux.restopos.ui.AnimatedButton takeAwayBtn;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
