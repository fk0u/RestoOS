package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.TableDAO;
import com.kiloux.restopos.model.Table;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class KitchenPanel extends JPanel {
    private MainFrame mainFrame;
    private TableDAO tableDAO;

    public KitchenPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.tableDAO = new TableDAO();
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY); // Dark mode for kitchen
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 30, 30));
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("KITCHEN DASHBOARD (Staff Only)");
        title.setForeground(Color.WHITE);
        title.setFont(UIConfig.FONT_HEADER);
        
        JButton exitBtn = new JButton("Exit Staff Mode (F12)");
        exitBtn.addActionListener(e -> mainFrame.showCard("ONBOARDING"));
        
        header.add(title, BorderLayout.WEST);
        header.add(exitBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        
        // Dashboard Content
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Orders Queue", createQueuePanel());
        tabs.addTab("Table Management", createTableMgmtPanel());
        tabs.addTab("Inventory", new JPanel()); // Placeholder
        
        add(tabs, BorderLayout.CENTER);
    }
    
    private JPanel createQueuePanel() {
        JPanel p = new JPanel();
        p.add(new JLabel("No active orders"));
        return p;
    }
    
    private JPanel createTableMgmtPanel() {
        JPanel p = new JPanel(new GridLayout(4, 4, 10, 10));
        List<Table> tables = tableDAO.getAllTables();
        for (Table t : tables) {
            JButton btn = new JButton("Table " + t.getTableNumber() + " [" + t.getStatus() + "]");
            btn.setBackground("OCCUPIED".equals(t.getStatus()) ? Color.RED : Color.GREEN);
            btn.addActionListener(e -> {
                String newStatus = "AVAILABLE".equals(t.getStatus()) ? "OCCUPIED" : "AVAILABLE";
                tableDAO.updateStatus(t.getId(), newStatus);
                btn.setText("Table " + t.getTableNumber() + " [" + newStatus + "]");
                btn.setBackground("OCCUPIED".equals(newStatus) ? Color.RED : Color.GREEN);
            });
            p.add(btn);
        }
        return p;
    }
}
