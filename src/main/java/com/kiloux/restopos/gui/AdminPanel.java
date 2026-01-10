package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.MenuItemDAO;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.ui.GlassPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private MenuItemDAO menuDAO;

    public AdminPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.menuDAO = new MenuItemDAO();
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);
        
        JButton closeBtn = new JButton("Close (Main Menu)");
        closeBtn.addActionListener(e -> mainFrame.showCard("ONBOARDING"));
        header.add(closeBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Content (Tabs)
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIConfig.FONT_TITLE);
        
        tabs.addTab("Inventory", createInventoryPanel());
        tabs.addTab("Sales Report", createSalesPanel());
        
        add(tabs, BorderLayout.CENTER);
    }
    
    private JPanel createInventoryPanel() {
        GlassPanel p = new GlassPanel(new BorderLayout());
        p.setBorderRadius(20);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Name", "Price", "Stock", "Category"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(UIConfig.FONT_BODY);
        
        List<MenuItem> items = menuDAO.getAllMenuItems();
        for (MenuItem m : items) {
             model.addRow(new Object[]{m.getId(), m.getName(), m.getPrice(), m.getStock(), m.getCategory()});
        }
        
        JScrollPane scroll = new JScrollPane(table);
        p.add(scroll, BorderLayout.CENTER);
        
        JPanel actions = new JPanel();
        actions.setOpaque(false);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            model.setRowCount(0);
             List<MenuItem> newItems = menuDAO.getAllMenuItems();
            for (MenuItem m : newItems) {
                 model.addRow(new Object[]{m.getId(), m.getName(), m.getPrice(), m.getStock(), m.getCategory()});
            }
        });
        actions.add(refresh);
        p.add(actions, BorderLayout.SOUTH);
        
        return p;
    }
    
    private JPanel createSalesPanel() {
        GlassPanel p = new GlassPanel(new BorderLayout());
        p.setBorderRadius(20);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lbl = new JLabel("<html><h2>Sales Overview</h2><p>Today's Total: Rp 2,500,000 (Simulated)</p><p>Total Orders: 45</p></html>");
        lbl.setForeground(Color.WHITE);
        p.add(lbl, BorderLayout.NORTH);
        
        return p; // Placeholder for now
    }
}
