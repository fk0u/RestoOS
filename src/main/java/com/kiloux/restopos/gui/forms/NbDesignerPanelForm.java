package com.kiloux.restopos.gui.forms;

public class NbDesignerPanelForm extends javax.swing.JPanel {

    public NbDesignerPanelForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        descLabel = new javax.swing.JLabel();
        openRuntimeButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20));
        titleLabel.setText("NetBeans JPanel Form");

        descLabel.setText("Form ini kompatibel NetBeans GUI Builder (Design tab).");

        openRuntimeButton.setText("Buka Swing Designer Runtime");
        openRuntimeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openRuntimeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(descLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(openRuntimeButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descLabel)
                .addGap(18, 18, 18)
                .addComponent(openRuntimeButton)
                .addGap(0, 240, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openRuntimeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openRuntimeButtonActionPerformed
        SwingDesignerFrameForm.open();
    }//GEN-LAST:event_openRuntimeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descLabel;
    private javax.swing.JButton openRuntimeButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
