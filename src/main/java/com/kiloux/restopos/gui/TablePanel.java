package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.TableDAO;
import com.kiloux.restopos.model.Table;
import com.kiloux.restopos.ui.GlassPanel;

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
        setLayout(new BorderLayout(0, 20));
        setOpaque(false);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Pilih Meja", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

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

        int cols = orientation == DeviceOrientation.LANDSCAPE ? 4 : 3;
        JPanel grid = new JPanel(new GridLayout(0, cols, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        List<Table> tables = tableDAO.getAllTables();
        for (Table t : tables) {
            JButton tableBtn = createTableButton(t);
            grid.add(tableBtn);
        }

        GlassPanel wrapper = new GlassPanel(new BorderLayout());
        wrapper.setBorderRadius(24);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        wrapper.add(grid, BorderLayout.CENTER);

        body.add(wrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private JButton createTableButton(Table t) {
        boolean isOccupied = "OCCUPIED".equalsIgnoreCase(t.getStatus());
        Color baseColor = isOccupied ? UIConfig.DANGER_COLOR : UIConfig.PRIMARY_COLOR;
        
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Draw Chairs (Circles around)
                g2.setColor(new Color(255, 255, 255, 100));
                int chairSize = 20;
                // Top/Bottom chairs
                g2.fillOval(w/2 - chairSize/2, 5, chairSize, chairSize);
                g2.fillOval(w/2 - chairSize/2, h - 5 - chairSize, chairSize, chairSize);
                // Left/Right chairs
                if (t.getCapacity() > 2) {
                     g2.fillOval(5, h/2 - chairSize/2, chairSize, chairSize);
                     g2.fillOval(w - 5 - chairSize, h/2 - chairSize/2, chairSize, chairSize);
                }
                
                // Draw Table Surface
                int pad = 15;
                g2.setColor(isOccupied ? new Color(239, 68, 68, 150) : new Color(16, 185, 129, 150));
                g2.fillRoundRect(pad, pad, w - pad*2, h - pad*2, 20, 20);
                
                // Border
                g2.setColor(baseColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(pad, pad, w - pad*2, h - pad*2, 20, 20);
                
                // Text
                String text = "Meja " + t.getTableNumber();
                g2.setFont(UIConfig.FONT_BODY);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(text);
                int th = fm.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.drawString(text, (w - tw)/2, (h + th)/2 - 5);
                
                String caps = t.getCapacity() + " Kursi";
                g2.setFont(UIConfig.FONT_SMALL);
                fm = g2.getFontMetrics(); // Update metrics for small font
                tw = fm.stringWidth(caps);
                g2.setColor(new Color(255,255,255,200));
                g2.drawString(caps, (w - tw)/2, (h + th)/2 + 15);
            }
        };
        btn.setPreferredSize(new Dimension(100, 100)); // Fixed preference
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setEnabled(!isOccupied); 
        
        btn.addActionListener(e -> {
            mainFrame.showCard("MENU");
        });
        
        return btn;
    }
}
