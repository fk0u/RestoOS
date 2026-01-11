package com.kiloux.restopos.apps;

import com.kiloux.restopos.ui.AeroPanel;
import java.awt.*;
import javax.swing.*;

public class NotepadApp extends JInternalFrame {
    
    public NotepadApp() {
        super("Notepad", true, true, true, true);
        setSize(500, 400);
        setFrameIcon(null); // Or load an icon if available, null uses Default or none
        
        // Menu
        JMenuBar mb = new JMenuBar();
        JMenu mFile = new JMenu("File");
        mFile.add(new JMenuItem("New"));
        mFile.add(new JMenuItem("Open..."));
        mFile.add(new JMenuItem("Save"));
        mFile.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        mFile.add(exit);
        
        JMenu mEdit = new JMenu("Edit");
        mEdit.add(new JMenuItem("Cut"));
        mEdit.add(new JMenuItem("Copy"));
        mEdit.add(new JMenuItem("Paste"));
        
        JMenu mFormat = new JMenu("Format");
        mFormat.add(new JMenuItem("Word Wrap"));
        mFormat.add(new JMenuItem("Font..."));
        
        mb.add(mFile);
        mb.add(mEdit);
        mb.add(mFormat);
        setJMenuBar(mb);
        
        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        
        setContentPane(scroll);
    }
}
