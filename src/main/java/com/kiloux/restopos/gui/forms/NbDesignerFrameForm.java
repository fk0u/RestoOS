package com.kiloux.restopos.gui.forms;

public class NbDesignerFrameForm extends javax.swing.JFrame {

    public NbDesignerFrameForm() {
        initComponents();
    }

    public static void open() {
        java.awt.EventQueue.invokeLater(() -> new NbDesignerFrameForm().setVisible(true));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nbDesignerPanelForm1 = new com.kiloux.restopos.gui.forms.NbDesignerPanelForm();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("NetBeans JFrame Form");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nbDesignerPanelForm1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(nbDesignerPanelForm1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.kiloux.restopos.gui.forms.NbDesignerPanelForm nbDesignerPanelForm1;
    // End of variables declaration//GEN-END:variables
}
