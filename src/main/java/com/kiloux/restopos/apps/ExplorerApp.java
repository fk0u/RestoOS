package com.kiloux.restopos.apps;

import com.kiloux.restopos.ui.PlexUtils;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.awt.event.*;

public class ExplorerApp extends JInternalFrame {

    private JPanel fileGrid;
    private JLabel pathLabel;
    private File currentDir;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ExplorerApp() {
        super("Computer", true, true, true, true);
        setSize(900, 600);
        setFrameIcon(null);
        
        // Main Container
        JPanel main = new JPanel(new BorderLayout());
        setContentPane(main);
        
        // --- 1. Plex Header (Address Bar) ---
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                PlexUtils.drawPlexHeader((Graphics2D)g, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.setOpaque(false);
        JButton backBtn = createNavButton(" < ");
        backBtn.addActionListener(e -> navigateUp());
        controls.add(backBtn);
        
        pathLabel = new JLabel("My Computer");
        pathLabel.setForeground(Color.WHITE);
        pathLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        controls.add(pathLabel);
        
        header.add(controls, BorderLayout.WEST);
        
        // Search
        JTextField search = new JTextField("Search Computer");
        search.setPreferredSize(new Dimension(200, 30));
        search.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,100)));
        search.setOpaque(false);
        search.setForeground(Color.WHITE);
        search.setCaretColor(Color.WHITE);
        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBox.setOpaque(false);
        searchBox.add(search);
        header.add(searchBox, BorderLayout.EAST);
        
        main.add(header, BorderLayout.NORTH);
        
        // --- 2. Split Pane (Sidebar / Content) ---
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(200);
        split.setBorder(null);
        
        // Sidebar (Common Tasks)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 245, 255)); // Plex Pale Blue
        sidebar.add(createSidebarLink("Desktop"));
        sidebar.add(createSidebarLink("Documents"));
        sidebar.add(createSidebarLink("Downloads"));
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(createSidebarLink("Local Disk (C:)"));
        
        JScrollPane sideScroll = new JScrollPane(sidebar);
        sideScroll.setBorder(null);
        split.setLeftComponent(sideScroll);
        
        // Content (File Grid)
        fileGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        fileGrid.setBackground(Color.WHITE);
        JScrollPane gridScroll = new JScrollPane(fileGrid);
        gridScroll.setBorder(null);
        split.setRightComponent(gridScroll);
        
        main.add(split, BorderLayout.CENTER);
        
        // Init
        loadRoots();
    }
    
    private void navigate(File dir) {
        if (dir == null || !dir.exists()) return;
        currentDir = dir;
        pathLabel.setText(dir.getAbsolutePath());
        
        fileGrid.removeAll();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isHidden()) fileGrid.add(createFileIcon(f));
            }
        }
        fileGrid.revalidate();
        fileGrid.repaint();
    }
    
    private void navigateUp() {
        if (currentDir == null) {
            loadRoots(); // Back to "My Computer"
            return; 
        }
        File p = currentDir.getParentFile();
        if (p != null) navigate(p);
        else loadRoots();
    }
    
    private void loadRoots() {
        currentDir = null;
        pathLabel.setText("My Computer");
        fileGrid.removeAll();
        for (File f : File.listRoots()) {
            fileGrid.add(createFileIcon(f));
        }
        fileGrid.revalidate();
        fileGrid.repaint();
    }
    
    // UI Helpers
    
    private JButton createNavButton(String txt) {
        JButton b = new JButton(txt) {
            @Override
            protected void paintComponent(Graphics g) {
                PlexUtils.drawGlassButton((Graphics2D)g, getWidth(), getHeight(), getModel().isRollover(), getModel().isPressed());
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        return b;
    }
    
    private JPanel createSidebarLink(String txt) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(0, 51, 153)); // Link Blue
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent e) {
                 if (txt.contains("C:")) navigate(new File("C:\\"));
                 else navigate(new File(System.getProperty("user.home") + "\\" + txt));
             }
        });
        p.add(l);
        return p;
    }
    
    private JPanel createFileIcon(File f) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(80, 80));
        p.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        // Icon (Fake based on type)
        JLabel icon = new JLabel();
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setText(f.isDirectory() ? "[DIR]" : "[FILE]");
        icon.setForeground(f.isDirectory() ? new Color(255, 200, 0) : Color.GRAY); // Yellow Folder
        icon.setFont(new Font("Segoe UI", Font.BOLD, 10)); // Placeholder for real icon
        
        // Name
        JLabel name = new JLabel(f.getName().length() > 10 ? f.getName().substring(0,8)+"..." : f.getName());
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        p.add(icon);
        p.add(name);
        
        // Click
        p.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (f.isDirectory()) {
                        navigate(f);
                    } else if (f.getName().endsWith(".txt") || f.getName().endsWith(".log") || f.getName().endsWith(".md")) {
                        // Open in Notepad
                        try {
                            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f));
                            StringBuilder sb = new StringBuilder();
                            String ln;
                            while ((ln = br.readLine()) != null) sb.append(ln).append("\n");
                            br.close();
                            
                            NotepadApp notepad = new NotepadApp();
                            // Access text area (it's in content pane)
                            if (notepad.getContentPane() instanceof javax.swing.JScrollPane) {
                                javax.swing.JScrollPane sp = (javax.swing.JScrollPane) notepad.getContentPane();
                                if (sp.getViewport().getView() instanceof javax.swing.JTextArea) {
                                    ((javax.swing.JTextArea) sp.getViewport().getView()).setText(sb.toString());
                                }
                            }
                            notepad.setTitle("Notepad - " + f.getName());
                            javax.swing.JDesktopPane dp = (javax.swing.JDesktopPane) javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JDesktopPane.class, ExplorerApp.this);
                            if (dp != null) {
                                dp.add(notepad);
                                notepad.setVisible(true);
                                notepad.setLocation(150, 100);
                            }
                        } catch (Exception ex) {
                            javax.swing.JOptionPane.showMessageDialog(ExplorerApp.this, "Cannot open file: " + ex.getMessage());
                        }
                    }
                }
            }
            public void mouseEntered(MouseEvent e) { p.setBackground(new Color(230, 240, 255)); }
            public void mouseExited(MouseEvent e) { p.setBackground(Color.WHITE); }
        });
        
        return p;
    }
}
