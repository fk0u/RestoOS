package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.utils.DatabaseManager;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class CustomerMonitorPanel extends JPanel {

    private MainFrame mainFrame;
    private OrderDAO orderDAO;
    private Timer refreshTimer;
    
    private JPanel preparingPanel;
    private JPanel readyPanel;
    
    public CustomerMonitorPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.orderDAO = new OrderDAO();
        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 0, 128)); // Navy Blue
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel title = new JLabel("ORDER STATUS MONITOR");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, BorderLayout.CENTER);
        
        // Navigation Hint
        JLabel hint = new JLabel("[ESC] Back");
        hint.setForeground(Color.LIGHT_GRAY);
        header.add(hint, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        // --- Split Main Content ---
        JPanel content = new JPanel(new GridLayout(1, 2, 5, 0));
        content.setBackground(Color.BLACK);
        
        // LEFT: PREPARING
        JPanel exPrep = new JPanel(new BorderLayout());
        exPrep.setBackground(Color.BLACK);
        exPrep.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.DARK_GRAY));
        
        JLabel lblPrep = new JLabel("PREPARING");
        lblPrep.setFont(new Font("Arial", Font.BOLD, 28));
        lblPrep.setForeground(Color.YELLOW);
        lblPrep.setHorizontalAlignment(SwingConstants.CENTER);
        lblPrep.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        exPrep.add(lblPrep, BorderLayout.NORTH);
        
        preparingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        preparingPanel.setBackground(Color.BLACK);
        exPrep.add(preparingPanel, BorderLayout.CENTER); // Wrap in Scroll if needed
        
        // RIGHT: READY
        JPanel exReady = new JPanel(new BorderLayout());
        exReady.setBackground(Color.BLACK);
        
        JLabel lblReady = new JLabel("READY TO SERVE");
        lblReady.setFont(new Font("Arial", Font.BOLD, 28));
        lblReady.setForeground(Color.GREEN);
        lblReady.setHorizontalAlignment(SwingConstants.CENTER);
        lblReady.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        exReady.add(lblReady, BorderLayout.NORTH);
        
        readyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        readyPanel.setBackground(Color.BLACK);
        exReady.add(readyPanel, BorderLayout.CENTER);
        
        content.add(exPrep);
        content.add(exReady);
        add(content, BorderLayout.CENTER);
        
        // Auto Refresh
        refreshTimer = new Timer(5000, e -> fetchData());
        refreshTimer.start();
        
        // Key Binding for Exit
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "back");
        getActionMap().put("back", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mainFrame.showCard("ONBOARDING"); 
            }
        });
    }
    
    // We need ad-hoc queries because OrderDAO might not have specific separation methods yet
    private void fetchData() {
        preparingPanel.removeAll();
        readyPanel.removeAll();
        
        // Fetch Logic
        // PREPARING: Status = PENDING, COOKING
        // READY: Status = COMPLETED (limit to last 10?)
        
        List<String> preparing = getQueueNumbers("PENDING", "COOKING");
        List<String> ready = getQueueNumbers("COMPLETED"); // We assume COMPLETED is "Ready"
        
        for (String q : preparing) {
            JLabel badge = createBadge(q, Color.YELLOW);
            preparingPanel.add(badge);
        }
        
        for (String q : ready) {
            JLabel badge = createBadge(q, Color.GREEN);
            readyPanel.add(badge);
        }
        
        preparingPanel.revalidate(); preparingPanel.repaint();
        readyPanel.revalidate(); readyPanel.repaint();
    }
    
    private List<String> getQueueNumbers(String... statuses) {
        List<String> list = new ArrayList<>();
        StringBuilder q = new StringBuilder("SELECT queue_number FROM orders WHERE status IN (");
        for(int i=0; i<statuses.length; i++) {
            q.append("'").append(statuses[i]).append("'");
            if(i < statuses.length-1) q.append(",");
        }
        q.append(") AND date(created_at) = CURDATE() ORDER BY id DESC LIMIT 12");
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(q.toString())) {
             while(rs.next()) {
                 list.add(String.format("%03d", rs.getInt(1)));
             }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }
    
    private JLabel createBadge(String num, Color c) {
        JLabel l = new JLabel(num);
        l.setFont(new Font("Monospaced", Font.BOLD, 48));
        l.setForeground(c);
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(c, 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return l;
    }
}
