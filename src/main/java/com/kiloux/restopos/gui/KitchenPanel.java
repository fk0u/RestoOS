package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.dao.TableDAO;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.model.Table;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class KitchenPanel extends JPanel {
    private MainFrame mainFrame;
    private TableDAO tableDAO;

    private OrderDAO orderDAO;
    private Timer refreshTimer;
    private JPanel ticketContainer; // Separate container for tickets

    public KitchenPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.tableDAO = new TableDAO(); // Keep this for createTableMgmtPanel if it's still used somewhere, but the instruction removes the tabbed pane.
        this.orderDAO = new OrderDAO();
        setLayout(new BorderLayout());
        setBackground(UIConfig.BACKGROUND_BASE);
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConfig.GLASS_BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("Kitchen Display System (KDS)");
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        
        // Removed exitBtn and JTabbedPane as per instruction
        // JButton exitBtn = new JButton("Exit Staff Mode (F12)");
        // exitBtn.addActionListener(e -> mainFrame.showCard("ONBOARDING"));
        // header.add(exitBtn, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        
        // Queue Panel
        add(createQueuePanel(), BorderLayout.CENTER);
        
        // Auto Refresh
        refreshTimer = new Timer(5000, e -> refreshOrders());
        refreshTimer.start();
        refreshOrders(); // Initial load
    }
    
    private JPanel createQueuePanel() {
        ticketContainer = new JPanel();
        ticketContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // FlowLayout wraps if width is constrained
        ticketContainer.setBackground(new Color(20, 20, 20));
        
        // Mock Tickets removed, now populated by refreshOrders()
        
        JScrollPane scroll = new JScrollPane(ticketContainer);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scroll);
        return wrapper;
    }

    private void refreshOrders() {
        ticketContainer.removeAll();
        List<Order> orders = orderDAO.getPendingOrders();
        
        if (orders.isEmpty()) {
            JLabel empty = new JLabel("No active orders");
            empty.setForeground(Color.GRAY);
            empty.setFont(UIConfig.FONT_TITLE);
            ticketContainer.add(empty);
        } else {
            for (Order o : orders) {
                ticketContainer.add(createOrderTicket(o));
            }
        }
        ticketContainer.revalidate();
        ticketContainer.repaint();
    }
    
    private JPanel createOrderTicket(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 300));
        card.setBackground(new Color(30, 30, 30));
        card.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        
        // Time logic: diff in minutes
        long minsAgo = java.time.temporal.ChronoUnit.MINUTES.between(
            order.getCreatedAt(), java.time.LocalDateTime.now());
        
        // Header Color based on time
        Color headerColor = minsAgo < 5 ? new Color(22, 163, 74) : (minsAgo < 15 ? new Color(202, 138, 4) : new Color(220, 38, 38));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(headerColor);
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        JLabel title = new JLabel("#" + order.getQueueNumber());
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        
        JLabel time = new JLabel(minsAgo + " min");
        time.setFont(new Font("Segoe UI", Font.BOLD, 14));
        time.setForeground(Color.WHITE);
        
        header.add(title, BorderLayout.WEST);
        header.add(time, BorderLayout.EAST);
        
        // List
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(35, 35, 35));
        body.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String tName = order.getCustomerName() != null && !order.getCustomerName().isEmpty() ? order.getCustomerName() : ("Table " + order.getTableId());
        JLabel tblLbl = new JLabel(tName);
        tblLbl.setForeground(Color.LIGHT_GRAY);
        tblLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        body.add(tblLbl);
        body.add(Box.createVerticalStrut(5));
        body.add(new JSeparator());
        body.add(Box.createVerticalStrut(5));
        
        if (order.getItems() != null) {
            for (CartItem i : order.getItems()) {
                 JLabel il = new JLabel(i.getQuantity() + "x " + i.getMenuItem().getName());
                 il.setForeground(Color.WHITE);
                 il.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                 body.add(il);
                 if (i.getNotes() != null && !i.getNotes().isEmpty()) {
                     JLabel note = new JLabel("- " + i.getNotes());
                     note.setForeground(Color.YELLOW);
                     note.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                     body.add(note);
                 }
                 body.add(Box.createVerticalStrut(2));
            }
        }
        
        JButton doneBtn = new JButton("COMPLETE ORDER");
        doneBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        doneBtn.setBackground(new Color(40, 40, 40));
        doneBtn.setForeground(headerColor); // Match status color
        doneBtn.setFocusPainted(false);
        doneBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        doneBtn.setContentAreaFilled(false);
        doneBtn.setOpaque(true);
        
        doneBtn.addActionListener(e -> {
             orderDAO.updateStatus(order.getId(), "COMPLETED");
             refreshOrders(); // Refresh to remove the completed order
        });
        
        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        card.add(doneBtn, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createTableMgmtPanel() {
        JPanel p = new JPanel(new GridLayout(4, 4, 10, 10));
        List<Table> tables = tableDAO.getAllTables();
        for (Table t : tables) {
            JButton btn = new JButton("Table " + t.getTableNumber() + " [" + t.getStatus() + "]");
            btn.setBackground("OCCUPIED".equals(t.getStatus()) ? Color.RED : Color.GREEN);
            btn.setForeground(Color.BLACK);
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

    private JPanel createAnalyticsPanel() {
        org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
        dataset.addValue(15, "Sales", "10:00");
        dataset.addValue(30, "Sales", "11:00");
        dataset.addValue(60, "Sales", "12:00");
        dataset.addValue(45, "Sales", "13:00");
        dataset.addValue(20, "Sales", "14:00");

        org.jfree.chart.JFreeChart chart = org.jfree.chart.ChartFactory.createBarChart(
                "Hourly Sales Trend",
                "Hour",
                "Orders",
                dataset
        );
        
        return new org.jfree.chart.ChartPanel(chart);
    }
}
