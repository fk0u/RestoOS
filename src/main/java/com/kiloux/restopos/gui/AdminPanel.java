package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.ui.RetroButton;
import com.kiloux.restopos.utils.DatabaseManager;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {

    private MainFrame mainFrame;
    private DecimalFormat df = new DecimalFormat("#,###");
    
    // UI
    private JLabel totalSalesLbl;
    private JLabel totalOrdersLbl;
    private JPanel chartPanel;
    private JPanel topItemsPanel;

    private CardLayout contentLayout;
    private JPanel contentPanel;
    
    // Sub-panels
    private JPanel dashboardPanel;
    private JPanel usersPanel;
    private JPanel menuPanel;
    private JPanel ordersPanel;
    private JPanel tablesPanel;
    private JPanel revPanel;
    
    public AdminPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                com.kiloux.restopos.ui.PlexUtils.drawPlexHeader((Graphics2D)g, getWidth(), getHeight());
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel title = new JLabel("System Administration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConfig.TEXT_PRIMARY); // Use Config White
        header.add(title, BorderLayout.WEST);
        
        RetroButton homeBtn = new RetroButton("Dashboard");
        homeBtn.addActionListener(e -> contentLayout.show(contentPanel, "DASH"));
        header.add(homeBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        // --- Content Area (Cards) ---
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.setOpaque(false);
        
        // 1. Dashboard
        createDashboard();
        contentPanel.add(dashboardPanel, "DASH");
        
        // 2. Users
        createUsersView();
        contentPanel.add(usersPanel, "USERS");
        
        // 3. Menu
        createMenuView();
        contentPanel.add(menuPanel, "MENU");
        
        // 4. Orders History
        createOrdersView();
        contentPanel.add(ordersPanel, "ORDERS");
        
        // 5. Tables Management
        createTablesView();
        contentPanel.add(tablesPanel, "TABLES");
        
        // 6. Financials
        createFinancialsView();
        contentPanel.add(revPanel, "FINANCE");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // --- Footer Navigation ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footer.setOpaque(false);
        
        RetroButton usersBtn = new RetroButton("Users");
        usersBtn.addActionListener(e -> contentLayout.show(contentPanel, "USERS"));
        
        RetroButton menuBtn = new RetroButton("Menu");
        menuBtn.addActionListener(e -> contentLayout.show(contentPanel, "MENU"));
        
        RetroButton ordersBtn = new RetroButton("Orders");
        ordersBtn.addActionListener(e -> contentLayout.show(contentPanel, "ORDERS"));
        
        RetroButton tablesBtn = new RetroButton("Tables");
        tablesBtn.addActionListener(e -> contentLayout.show(contentPanel, "TABLES"));
        
        RetroButton finBtn = new RetroButton("Finance");
        finBtn.addActionListener(e -> contentLayout.show(contentPanel, "FINANCE"));
        
        RetroButton logoutBtn = new RetroButton("Logout");
        logoutBtn.setForeground(Color.RED);
        logoutBtn.addActionListener(e -> {
             com.kiloux.restopos.service.UserService.getInstance().logout();
             mainFrame.showCard("LOGIN");
        });
        
        footer.add(usersBtn);
        footer.add(menuBtn);
        footer.add(ordersBtn);
        footer.add(tablesBtn);
        footer.add(finBtn);
        footer.add(logoutBtn);
        add(footer, BorderLayout.SOUTH);

        // Init Data
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) { reloadData(); }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }
    
    private void createDashboard() {
        dashboardPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        dashboardPanel.setOpaque(false);
        
        // KPI Row
        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 15, 0));
        kpiRow.setOpaque(false);
        totalSalesLbl = createGlassCard(kpiRow, "TODAY'S REVENUE", "Rp 0");
        totalOrdersLbl = createGlassCard(kpiRow, "TOTAL ORDERS", "0");
        createGlassCard(kpiRow, "ACTIVE STAFF", "3");
        createGlassCard(kpiRow, "AVG. TIME", "12m");
        dashboardPanel.add(kpiRow);
        
        // Analytics
        JPanel analyticsRow = new JPanel(new GridLayout(1, 2, 15, 0));
        analyticsRow.setOpaque(false);
        chartPanel = createGlassPanel("Monthly Performance");
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        analyticsRow.add(chartPanel);
        topItemsPanel = createGlassPanel("Top Selling Items");
        topItemsPanel.setLayout(new BoxLayout(topItemsPanel, BoxLayout.Y_AXIS));
        analyticsRow.add(topItemsPanel);
        dashboardPanel.add(analyticsRow);
    }
    


    private void createUsersView() {
        usersPanel = createGlassPanel("User Management");
        usersPanel.setLayout(new BorderLayout(10, 10));
        
        String[] cols = {"ID", "Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        applyDarkTableStyle(table);
        
        Runnable refreshUsers = () -> {
            model.setRowCount(0);
            List<Object[]> users = com.kiloux.restopos.service.UserService.getInstance().getAllUsers();
            for(Object[] u : users) model.addRow(u);
        };
        refreshUsers.run();
        
        usersPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setOpaque(false);
        
        RetroButton addBtn = new RetroButton("Add User");
        addBtn.addActionListener(e -> {
            JTextField uField = new JTextField();
            String[] roles = {"ADMIN", "CASHIER", "KITCHEN", "CLIENT"};
            JComboBox<String> rCombo = new JComboBox<>(roles);
            Object[] msg = {"Username:", uField, "Role:", rCombo};
            if (JOptionPane.showConfirmDialog(this, msg, "New User", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                com.kiloux.restopos.service.UserService.getInstance().register(uField.getText(), "1234"); // Default pass
                refreshUsers.run();
            }
        });
        
        RetroButton delBtn = new RetroButton("Delete User");
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String name = (String) model.getValueAt(row, 1);
                // Execute Delete SQL (Inline for speed)
                try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE username=?")) {
                    ps.setString(1, name);
                    ps.executeUpdate();
                    refreshUsers.run();
                } catch(Exception ex) { ex.printStackTrace(); }
            }
        });
        
        acts.add(addBtn);
        acts.add(delBtn);
        usersPanel.add(acts, BorderLayout.SOUTH);
    }
    
    private void createMenuView() {
        menuPanel = createGlassPanel("Menu Management");
        menuPanel.setLayout(new BorderLayout(10, 10));
        
        String[] cols = {"ID", "Name", "Price", "Category"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        applyDarkTableStyle(table);
        
        Runnable refreshMenu = () -> {
            model.setRowCount(0);
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM menu_items")) {
                while(rs.next()) {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category")});
                }
            } catch(Exception e) { e.printStackTrace(); }
        };
        refreshMenu.run();
        
        menuPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setOpaque(false);
        
        RetroButton addBtn = new RetroButton("Add Item");
        addBtn.addActionListener(e -> {
            JTextField n = new JTextField();
            JTextField p = new JTextField();
            String[] cats = {"Main Course", "Beverage", "Dessert"};
            JComboBox<String> c = new JComboBox<>(cats);
            Object[] msg = {"Name:", n, "Price:", p, "Category:", c};
            if (JOptionPane.showConfirmDialog(this, msg, "New Menu Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                 try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement("INSERT INTO menu_items (name, price, category, stock) VALUES (?, ?, ?, 100)")) {
                    ps.setString(1, n.getText());
                    ps.setDouble(2, Double.parseDouble(p.getText()));
                    ps.setString(3, (String)c.getSelectedItem());
                    ps.executeUpdate();
                    refreshMenu.run();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
        
        RetroButton delBtn = new RetroButton("Delete Item");
        delBtn.addActionListener(e -> {
             int row = table.getSelectedRow();
             if (row != -1) {
                 int id = (int) model.getValueAt(row, 0);
                 try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM menu_items WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    refreshMenu.run();
                 } catch(Exception ex) { ex.printStackTrace(); }
             }
        });
        
        acts.add(addBtn);
        acts.add(delBtn);
        menuPanel.add(acts, BorderLayout.SOUTH);
    }
    
    private void createOrdersView() {
        ordersPanel = createGlassPanel("Order History");
        ordersPanel.setLayout(new BorderLayout(10, 10));
        
        String[] cols = {"ID", "Date", "Type", "Total", "Status", "Payment"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        applyDarkTableStyle(table);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(30, 30, 35));
        ordersPanel.add(scroll, BorderLayout.CENTER);
        
        Runnable refreshOrders = () -> {
            model.setRowCount(0);
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM orders ORDER BY created_at DESC LIMIT 100")) {
                while(rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getTimestamp("created_at"),
                        rs.getString("order_type"),
                        "Rp " + df.format(rs.getDouble("total_amount")),
                        rs.getString("status"),
                        rs.getString("payment_status")
                    });
                }
            } catch(Exception e) { e.printStackTrace(); }
        };
        refreshOrders.run();
        

        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setOpaque(false);
        RetroButton refreshBtn = new RetroButton("Refresh");
        refreshBtn.addActionListener(e -> refreshOrders.run());
        acts.add(refreshBtn);
        ordersPanel.add(acts, BorderLayout.SOUTH);
    }
    
    private void createTablesView() {
        tablesPanel = createGlassPanel("Table Management");
        tablesPanel.setLayout(new BorderLayout(10, 10));
        
        String[] cols = {"ID", "Table #", "Capacity", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        applyDarkTableStyle(table);
        
        Runnable refreshTables = () -> {
            model.setRowCount(0);
            try (Connection conn = DatabaseManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM tables ORDER BY table_number")) {
                while(rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("table_number"),
                        rs.getInt("capacity"),
                        rs.getString("status")
                    });
                }
            } catch(Exception e) { e.printStackTrace(); }
        };
        refreshTables.run();
        
        tablesPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        acts.setOpaque(false);
        
        RetroButton addBtn = new RetroButton("Add Table");
        addBtn.addActionListener(e -> {
            JTextField num = new JTextField();
            JTextField cap = new JTextField();
            Object[] msg = {"Table Number:", num, "Capacity:", cap};
            if (JOptionPane.showConfirmDialog(this, msg, "New Table", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try (Connection conn = DatabaseManager.getConnection(); 
                     PreparedStatement ps = conn.prepareStatement("INSERT INTO tables (table_number, capacity) VALUES (?, ?)")) {
                    ps.setInt(1, Integer.parseInt(num.getText()));
                    ps.setInt(2, Integer.parseInt(cap.getText()));
                    ps.executeUpdate();
                    refreshTables.run();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });
        
        RetroButton delBtn = new RetroButton("Delete Table");
        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                try (Connection conn = DatabaseManager.getConnection(); 
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM tables WHERE id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    refreshTables.run();
                } catch(Exception ex) { ex.printStackTrace(); }
            }
        });
        
        RetroButton statusBtn = new RetroButton("Toggle Status");
        statusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                String curr = (String) model.getValueAt(row, 3);
                String next = "AVAILABLE".equals(curr) ? "OCCUPIED" : "AVAILABLE";
                try (Connection conn = DatabaseManager.getConnection(); 
                     PreparedStatement ps = conn.prepareStatement("UPDATE tables SET status=? WHERE id=?")) {
                    ps.setString(1, next);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    refreshTables.run();
                } catch(Exception ex) { ex.printStackTrace(); }
            }
        });
        
        acts.add(addBtn);
        acts.add(delBtn);
        acts.add(statusBtn);
        tablesPanel.add(acts, BorderLayout.SOUTH);
    }
    
    private void createFinancialsView() {
        revPanel = createGlassPanel("Financial Reports");
        revPanel.setLayout(new BorderLayout(10, 10));
        
        JTextArea report = new JTextArea();
        report.setFont(new Font("Consolas", Font.PLAIN, 14));
        report.setEditable(false);
        // Fix Contrast
        report.setBackground(new Color(40, 44, 52)); // Dark Grey
        report.setForeground(Color.WHITE);
        report.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Query real data
        double totalRevenue = 0;
        int totalOrders = 0;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount) as rev, COUNT(*) as cnt FROM orders WHERE payment_status='PAID'");
            if (rs.next()) {
                totalRevenue = rs.getDouble("rev");
                totalOrders = rs.getInt("cnt");
            }
        } catch(Exception e) { e.printStackTrace(); }
        
        report.setText("--- FINANCIAL REPORT ---\n\n" +
                       "Date: " + java.time.LocalDate.now() + "\n\n" +
                       "Total Revenue (Paid Orders): Rp " + df.format(totalRevenue) + "\n" +
                       "Total Paid Orders: " + totalOrders + "\n" +
                       "Avg. Order Value: Rp " + (totalOrders > 0 ? df.format(totalRevenue/totalOrders) : "0") + "\n");
        revPanel.add(new JScrollPane(report), BorderLayout.CENTER);
        
        JPanel kpis = new JPanel(new GridLayout(1, 3, 10, 0));
        kpis.setOpaque(false);
        createGlassCard(kpis, "REVENUE", "Rp " + df.format(totalRevenue/1000000) + "M");
        createGlassCard(kpis, "ORDERS", String.valueOf(totalOrders));
        createGlassCard(kpis, "AVG/ORDER", "Rp " + (totalOrders > 0 ? df.format(totalRevenue/totalOrders) : "0"));
        
        revPanel.add(kpis, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, new Color(40, 60, 80), 0, getHeight(), new Color(20, 30, 40));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
    
    private JPanel createGlassPanel(String title) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10,10,10,10), title, TitledBorder.LEFT, TitledBorder.TOP,  new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        return p;
    }
    
    private JLabel createGlassCard(JPanel parent, String title, String val) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g;
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 50), 0, getHeight(), new Color(255, 255, 255, 10));
                 g2.setPaint(gp);
                 g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                 g2.setColor(new Color(255, 255, 255, 100));
                 g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(new Color(200, 220, 255));
        card.add(t, BorderLayout.NORTH);
        
        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 32));
        v.setForeground(Color.WHITE);
        v.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(v, BorderLayout.SOUTH);
        
        parent.add(card);
        return v;
    }

    private void reloadData() {
        if (chartPanel == null || topItemsPanel == null) return;
        chartPanel.removeAll();
        topItemsPanel.removeAll();
        
        // Reload KPI/Charts logic (Same as before)
         double revenue = 0;
        int orders = 0;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
             // 1. KPI
             ResultSet rs = stmt.executeQuery("SELECT SUM(total_amount), COUNT(*) FROM orders WHERE DATE(created_at) = CURDATE()");
             if (rs.next()) {
                 revenue = rs.getDouble(1);
                 orders = rs.getInt(2);
             }
             if (totalSalesLbl != null) totalSalesLbl.setText("Rp " + df.format(revenue));
             if (totalOrdersLbl != null) totalOrdersLbl.setText(String.valueOf(orders));
             // 2. Charts...
             addBar(chartPanel, "Week 1", 120, 200);
             addBar(chartPanel, "Week 2", 150, 200);
             
             rs = stmt.executeQuery("SELECT m.name, SUM(oi.quantity) as qty FROM order_items oi " +
                                    "JOIN orders o ON oi.order_id = o.id " +
                                    "JOIN menu_items m ON oi.menu_item_id = m.id " +
                                    "WHERE DATE(o.created_at) = CURDATE() " +
                                    "GROUP BY m.name ORDER BY qty DESC LIMIT 5");
             while(rs.next()) {
                 addBar(topItemsPanel, rs.getString("name"), rs.getInt("qty"), 50); 
             }
        } catch(Exception e) { e.printStackTrace(); }
        
        chartPanel.revalidate(); chartPanel.repaint();
        topItemsPanel.revalidate(); topItemsPanel.repaint();
    }
    
    private void addBar(JPanel p, String label, int value, int scale) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(4, 5, 4, 5));
        JLabel l = new JLabel(label);
        l.setPreferredSize(new Dimension(100, 20));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.WHITE);
        row.add(l, BorderLayout.WEST);
        JProgressBar bar = new JProgressBar(0, scale);
        bar.setValue(value);
        bar.setString(String.valueOf(value));
        bar.setStringPainted(true);
        bar.setForeground(new Color(50, 200, 150));
        bar.setBackground(new Color(255, 255, 255, 20));
        bar.setBorderPainted(false);
        row.add(bar, BorderLayout.CENTER);
        p.add(row);
    }
    private void applyDarkTableStyle(JTable table) {
        table.setBackground(new Color(40, 44, 52));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(80, 80, 80));
        table.setSelectionBackground(UIConfig.PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(20, 25, 30));
        table.getTableHeader().setForeground(Color.WHITE);
        if (table.getParent() instanceof JViewport) {
             ((JViewport)table.getParent()).setBackground(new Color(30, 30, 35));
        }
    }
}
