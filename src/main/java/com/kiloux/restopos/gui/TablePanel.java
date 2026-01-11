package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.TableDAO;
import com.kiloux.restopos.model.Table;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TablePanel extends JPanel {
    
    private MainFrame mainFrame;
    private TableDAO tableDAO;
    private DeviceOrientation orientation;

    private JPanel body;

    public TablePanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.tableDAO = new TableDAO();
        this.orientation = orientation;
        
        setLayout(new BorderLayout(0, 10));
        setBackground(UIConfig.BACKGROUND_COLOR);
        
        // Header
        JLabel title = new JLabel("PILIH MEJA", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        add(body, BorderLayout.CENTER);

        refreshTables();
    }

    public void applyOrientation(DeviceOrientation newOrientation) {
        if (newOrientation == null) return;
        this.orientation = newOrientation;
        refreshTables();
    }

    private void refreshTables() {
        body.removeAll();

        // 4 Columns as per general grid spec, or 3 if portrait to fit better? 
        // User asked for "Grid (4 kolom)" for menu items. Let's stick to 4 for tables too for consistency if space allows.
        int cols = 4; 
        JPanel grid = new JPanel(new GridLayout(0, cols, 10, 10));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Table> tables = tableDAO.getAllTables();
        for (Table t : tables) {
            JButton tableBtn = createRetroTableButton(t);
            grid.add(tableBtn);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIConfig.BACKGROUND_COLOR);

        body.add(scroll, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private JButton createRetroTableButton(Table t) {
        boolean isOccupied = "OCCUPIED".equalsIgnoreCase(t.getStatus());
        Color baseColor = isOccupied ? UIConfig.DANGER_COLOR : UIConfig.SECONDARY_COLOR;
        
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // Pixel/Sharp look
                
                int w = getWidth();
                int h = getHeight();
                
                // Background
                g2.setColor(UIConfig.BACKGROUND_COLOR);
                g2.fillRect(0, 0, w, h);
                
                // Table Box
                int pad = 15;
                g2.setColor(baseColor);
                g2.fillRect(pad, pad, w - pad*2, h - pad*2);
                
                // Border
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(pad, pad, w - pad*2 - 1, h - pad*2 - 1);
                
                // Chairs (Squares)
                int chairSize = 12;
                g2.setColor(Color.DARK_GRAY);
                
                // Top/Bottom
                g2.fillRect(w/2 - chairSize/2, 2, chairSize, chairSize);
                g2.fillRect(w/2 - chairSize/2, h - 2 - chairSize, chairSize, chairSize);
                
                // Text
                String text = "M-" + t.getTableNumber();
                g2.setFont(UIConfig.FONT_TITLE);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(text);
                
                g2.setColor(UIConfig.TEXT_PRIMARY);
                g2.drawString(text, (w - tw)/2, h/2 + 5);
                
                // Status Overlay if occupied
                if (isOccupied) {
                    g2.setColor(new Color(0,0,0,50)); // Dim it
                    g2.fillRect(pad, pad, w - pad*2, h - pad*2);
                }
            }
        };
        btn.setPreferredSize(new Dimension(100, 100));
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Outer border
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        
        btn.addActionListener(e -> {
            if (!isOccupied) mainFrame.showCard("MENU");
            else JOptionPane.showMessageDialog(this, "Meja sedang dipakai!", "Info", JOptionPane.WARNING_MESSAGE);
        });
        
        return btn;
    }
}
