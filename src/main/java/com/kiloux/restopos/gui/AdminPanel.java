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
    private JPanel revPanel;
    
    public AdminPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("System Administration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
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
        
        // 4. Financials
        createFinancialsView();
        contentPanel.add(revPanel, "FINANCE");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // --- Sidebar / Navigation (Left) ---
        // Replacing footer with Sidebar for better UX? Or keeping footer buttons.
        // User asked for "open all pages", so footer buttons must switch cards.
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Changed to Center/Flow
        footer.setOpaque(false);
        
        RetroButton usersBtn = new RetroButton("Manage Users");
        usersBtn.addActionListener(e -> contentLayout.show(contentPanel, "USERS"));
        
        RetroButton menuBtn = new RetroButton("Manage Menu");
        menuBtn.addActionListener(e -> contentLayout.show(contentPanel, "MENU"));
        
        RetroButton finBtn = new RetroButton("Financials");
        finBtn.addActionListener(e -> contentLayout.show(contentPanel, "FINANCE"));
        
        RetroButton logoutBtn = new RetroButton("Logout");
        logoutBtn.setForeground(Color.RED);
        logoutBtn.addActionListener(e -> {
             // Quick logout logic
             com.kiloux.restopos.service.UserService.getInstance().logout();
             mainFrame.showCard("LOGIN");
        });
        
        footer.add(usersBtn);
        footer.add(menuBtn);
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
    
    private void createFinancialsView() {
        revPanel = createGlassPanel("Financial Reports");
        revPanel.setLayout(new BorderLayout(10, 10));
        
        JTextArea report = new JTextArea();
        report.setFont(new Font("Consolas", Font.PLAIN, 14));
        report.setText("--- FINANCIAL REPORT ---\n\n" +
                       "Date: " + java.time.LocalDate.now() + "\n\n" +
                       "Total Revenue (Gross): Rp 15,000,000\n" +
                       "Total Expenses: Rp 8,000,000\n" +
                       "Net Profit: Rp 7,000,000\n\n" + 
                       "Projected Growth: +15%\n");
        revPanel.add(new JScrollPane(report), BorderLayout.CENTER);
        
        JPanel kpis = new JPanel(new GridLayout(1, 3, 10, 0));
        kpis.setOpaque(false);
        createGlassCard(kpis, "GROSS MARGIN", "46%");
        createGlassCard(kpis, "OPEX", "Rp 8M");
        createGlassCard(kpis, "NET INCOME", "Rp 7M");
        
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
}
