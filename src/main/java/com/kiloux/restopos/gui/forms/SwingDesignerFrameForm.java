package com.kiloux.restopos.gui.forms;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SwingDesignerFrameForm extends JFrame {

    public SwingDesignerFrameForm() {
        super("Swing Designer - JFrame Form");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(new SwingDesignerPanelForm());
        setSize(1080, 680);
        centerToScreen();
    }

    private void centerToScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> new SwingDesignerFrameForm().setVisible(true));
    }
}