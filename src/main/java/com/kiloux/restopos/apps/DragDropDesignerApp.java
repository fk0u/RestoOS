package com.kiloux.restopos.apps;

import com.kiloux.restopos.gui.forms.SwingDesignerPanelForm;
import javax.swing.JInternalFrame;

public class DragDropDesignerApp extends JInternalFrame {

    public DragDropDesignerApp() {
        super("Swing Drag & Drop Designer", true, true, true, true);
        setSize(980, 620);
        setFrameIcon(null);
        setContentPane(new SwingDesignerPanelForm());
    }
}