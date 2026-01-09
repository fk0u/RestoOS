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
        Color color = isOccupied ? UIConfig.DANGER_COLOR : UIConfig.PRIMARY_COLOR;
        
        JButton btn = new JButton("<html><center>Meja " + t.getTableNumber() + "<br/><small>" + t.getCapacity() + " Kursi</small></center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isOccupied ? new Color(239, 68, 68, 80) : new Color(16, 185, 129, 80)); // Low opacity fill
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                super.paintComponent(g);
            }
        };
        btn.setFont(UIConfig.FONT_BODY);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setEnabled(!isOccupied); // Disable occupied for now
        
        btn.addActionListener(e -> {
            // Select table logic
            // For now, jump to menu
            mainFrame.showCard("MENU");
        });
        
        return btn;
    }
}
