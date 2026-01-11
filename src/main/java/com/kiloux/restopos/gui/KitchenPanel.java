package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class KitchenPanel extends JPanel {
    
    private OrderDAO orderDAO;
    private Timer refreshTimer;
    private JPanel ticketContainer; 

    public KitchenPanel(MainFrame frame) {
        this.orderDAO = new OrderDAO();
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Retro CRT Black
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.DARK_GRAY);
        header.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        JLabel title = new JLabel(" KITCHEN DISPLAY SYSTEM (KDS) - LIVE");
        title.setFont(new Font("Monospaced", Font.BOLD, 20));
        title.setForeground(Color.GREEN);
        header.add(title, BorderLayout.WEST);
        
        RetroButton refreshBtn = new RetroButton("REFRESH");
        refreshBtn.addActionListener(e -> refreshOrders());
        header.add(refreshBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        // --- Ticket Grid ---
        ticketContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        ticketContainer.setBackground(Color.BLACK);
        
        JScrollPane scroll = new JScrollPane(ticketContainer);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.BLACK);
        add(scroll, BorderLayout.CENTER);
        
        // Auto Refresh
        refreshTimer = new Timer(5000, e -> refreshOrders());
        refreshTimer.start();
        refreshOrders(); 
    }
    
    private void refreshOrders() {
        ticketContainer.removeAll();
        // Uses the update OrderDAO method I fixed (PENDING/COOKING)
        List<Order> orders = orderDAO.getPendingOrders();
        
        if (orders.isEmpty()) {
            JLabel empty = new JLabel("NO ACTIVE ORDERS");
            empty.setForeground(Color.GREEN.darker());
            empty.setFont(new Font("Monospaced", Font.BOLD, 24));
            ticketContainer.add(empty);
        } else {
            for (Order o : orders) {
                ticketContainer.add(createTicket(o));
            }
        }
        ticketContainer.revalidate();
        ticketContainer.repaint();
    }
    
    private JPanel createTicket(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 350));
        card.setBackground(Color.BLACK);
        card.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        
        // Header
        JPanel head = new JPanel(new BorderLayout());
        head.setBackground(Color.GREEN);
        JLabel qLbl = new JLabel(" #" + order.getQueueNumber());
        qLbl.setFont(new Font("Arial", Font.BOLD, 24));
        qLbl.setForeground(Color.BLACK);
        
        JLabel timeLbl = new JLabel(order.getOrderType() + " ");
        timeLbl.setFont(new Font("Arial", Font.BOLD, 12));
        timeLbl.setForeground(Color.BLACK);
        
        head.add(qLbl, BorderLayout.WEST);
        head.add(timeLbl, BorderLayout.EAST);
        card.add(head, BorderLayout.NORTH);
        
        // Body (Items)
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.BLACK);
        body.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel cust = new JLabel("Customer: " + (order.getCustomerName() == null ? "-" : order.getCustomerName()));
        cust.setForeground(Color.WHITE);
        cust.setFont(new Font("Monospaced", Font.PLAIN, 12));
        body.add(cust);
        body.add(new JSeparator());
        
        if (order.getItems() != null) {
            for (CartItem item : order.getItems()) {
                JLabel l = new JLabel(item.getQuantity() + "Xx " + item.getMenuItem().getName().toUpperCase());
                l.setForeground(Color.GREEN);
                l.setFont(new Font("Monospaced", Font.BOLD, 14));
                body.add(l);
                
                if (item.getNotes() != null && !item.getNotes().isEmpty()) {
                    JLabel n = new JLabel("  *" + item.getNotes());
                    n.setForeground(Color.YELLOW);
                    n.setFont(new Font("Monospaced", Font.ITALIC, 12));
                    body.add(n);
                }
                body.add(Box.createVerticalStrut(5));
            }
        }
        card.add(body, BorderLayout.CENTER);
        
        // Footer (Done)
        RetroButton doneBtn = new RetroButton("DONE");
        doneBtn.setBackground(Color.DARK_GRAY);
        doneBtn.setForeground(Color.WHITE);
        doneBtn.addActionListener(e -> {
            orderDAO.updateStatus(order.getId(), "COMPLETED");
            refreshOrders();
        });
        card.add(doneBtn, BorderLayout.SOUTH);
        
        return card;
    }
}
