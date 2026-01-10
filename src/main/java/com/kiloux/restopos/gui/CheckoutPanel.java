package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.service.PrintService;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import com.kiloux.restopos.ui.Toast;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CheckoutPanel extends JPanel {

    private MainFrame mainFrame;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    // Data
    private String customerName;
    private String paymentMethod; 
    
    // Components
    private JTextField nameField;
    private JTextField cashInput;
    private JLabel totalLabel;
    
    private OrderDAO orderDAO;

    public CheckoutPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.orderDAO = new OrderDAO();
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header (Steps Indicator)
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setOpaque(false);
        JLabel title = new JLabel("Checkout");
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);
        
        // Content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false);
        
        contentPanel.add(createNameStep(), "STEP_NAME");
        contentPanel.add(createMethodStep(), "STEP_METHOD");
        contentPanel.add(createCashStep(), "STEP_CASH");
        contentPanel.add(createQRISStep(), "STEP_QRIS");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Back Button
        JButton backBtn = new JButton("Cancel");
        backBtn.setForeground(UIConfig.TEXT_SECONDARY);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.addActionListener(e -> {
             if (JOptionPane.showConfirmDialog(this, "Cancel Checkout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                 mainFrame.showCard("CART");
             }
        });
        add(backBtn, BorderLayout.SOUTH);
    }
    
    public void reset() {
        customerName = "";
        paymentMethod = "";
        nameField.setText("");
        cashInput.setText("");
        cardLayout.show(contentPanel, "STEP_NAME");
    }
    
    private JPanel createNameStep() {
        JPanel p = new GlassPanel(new GridBagLayout());
        ((GlassPanel)p).setBorderRadius(30);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;
        
        JLabel lbl = new JLabel("Siapa nama anda?");
        lbl.setFont(UIConfig.FONT_HEADER);
        lbl.setForeground(Color.WHITE);
        p.add(lbl, gbc);
        
        gbc.gridy++;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBackground(new Color(255, 255, 255, 20));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UIConfig.PRIMARY_COLOR));
        p.add(nameField, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(30, 10, 10, 10);
        AnimatedButton nextBtn = new AnimatedButton("Lanjut", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        nextBtn.setPreferredSize(new Dimension(200, 60));
        nextBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                Toast.show(mainFrame, "Mohon isi nama!", Toast.Type.ERROR);
                return;
            }
            customerName = nameField.getText().trim();
            cardLayout.show(contentPanel, "STEP_METHOD");
        });
        p.add(nextBtn, gbc);
        
        return p;
    }
    
    private JPanel createMethodStep() {
        JPanel p = new GlassPanel(new GridBagLayout());
        ((GlassPanel)p).setBorderRadius(30);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        
        JLabel lbl = new JLabel("Pilih Metode Pembayaran");
        lbl.setFont(UIConfig.FONT_HEADER);
        lbl.setForeground(Color.WHITE);
        p.add(lbl, gbc);
        
        gbc.gridy++; gbc.gridwidth = 1;
        
        AnimatedButton cashBtn = new AnimatedButton("TUNAI (CASH)", UIConfig.WARNING_COLOR, UIConfig.WARNING_COLOR.brighter());
        cashBtn.setPreferredSize(new Dimension(250, 150));
        cashBtn.setFont(UIConfig.FONT_HEADER);
        cashBtn.addActionListener(e -> {
            paymentMethod = "CASH";
            updateTotalLabel();
            cardLayout.show(contentPanel, "STEP_CASH");
        });
        p.add(cashBtn, gbc);
        
        gbc.gridx++;
        AnimatedButton qrisBtn = new AnimatedButton("QRIS SCAN", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        qrisBtn.setPreferredSize(new Dimension(250, 150));
        qrisBtn.setFont(UIConfig.FONT_HEADER);
        qrisBtn.addActionListener(e -> {
            paymentMethod = "QRIS";
            finalizeOrder(CartService.getInstance().getTotal()); // Immediate for QRIS mockup
        });
        p.add(qrisBtn, gbc);
        
        return p;
    }
    
    private JPanel createCashStep() {
        JPanel p = new GlassPanel(new BorderLayout());
        ((GlassPanel)p).setBorderRadius(30);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Left: Info
        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setOpaque(false);
        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(UIConfig.FONT_HEADER);
        totalLabel.setForeground(Color.WHITE);
        
        cashInput = new JTextField();
        cashInput.setFont(new Font("Segoe UI", Font.BOLD, 36));
        cashInput.setForeground(UIConfig.ACCENT_COLOR);
        cashInput.setBackground(new Color(0,0,0,0));
        cashInput.setBorder(null);
        cashInput.setEditable(false); // Only Numpad
        cashInput.setHorizontalAlignment(JTextField.RIGHT);
        
        info.add(totalLabel);
        info.add(new JLabel("Uang Diterima:"));
        info.add(cashInput);
        
        p.add(info, BorderLayout.NORTH);
        
        // Center: Numpad
        JPanel numpad = new JPanel(new GridLayout(4, 3, 10, 10));
        numpad.setOpaque(false);
        String[] keys = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "C", "0", "PAY"};
        
        for (String k : keys) {
            JButton btn = new JButton(k);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
            btn.setFocusPainted(false);
            if (k.equals("PAY")) {
                btn.setBackground(UIConfig.PRIMARY_COLOR);
                btn.setForeground(Color.WHITE);
            } else if (k.equals("C")) {
                btn.setBackground(UIConfig.DANGER_COLOR);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(255, 255, 255, 30));
                btn.setForeground(Color.WHITE);
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            
            btn.addActionListener(e -> handleNumpad(k));
            numpad.add(btn);
        }
        
        p.add(numpad, BorderLayout.CENTER);
        
        return p;
    }
    
    private JPanel createQRISStep() {
        // Simple placeholder for QRIS
        return new JPanel(); 
    }
    
    private void updateTotalLabel() {
        totalLabel.setText("Total: Rp " + String.format("%.0f", CartService.getInstance().getTotal()));
    }
    
    private void handleNumpad(String key) {
        if ("C".equals(key)) {
            cashInput.setText("");
        } else if ("PAY".equals(key)) {
            try {
                double val = Double.parseDouble(cashInput.getText());
                double total = CartService.getInstance().getTotal();
                if (val >= total) {
                    finalizeOrder(val);
                } else {
                    Toast.show(mainFrame, "Uang Kurang!", Toast.Type.ERROR);
                }
            } catch (Exception e) {
                 Toast.show(mainFrame, "Input Error", Toast.Type.ERROR);
            }
        } else {
            cashInput.setText(cashInput.getText() + key);
        }
    }
    
    private void finalizeOrder(double payAmount) {
        CartService cart = CartService.getInstance();
        Order order = new Order();
        order.setTableId(cart.getSelectedTableId() > 0 ? cart.getSelectedTableId() : 0);
        order.setOrderType("DINE_IN");
        order.setStatus("WAITING");
        order.setTotalAmount(cart.getTotal());
        order.setPaymentStatus("PAID");
        order.setCustomerName(customerName);
        
        int orderId = orderDAO.createOrder(order, cart.getItems());
        
        if (orderId != -1) {
            PrintService.generateReceipt(order, cart.getItems(), payAmount, payAmount - cart.getTotal());
            cart.clear();
            
            mainFrame.getOrderProcessPanel().setQueueNumber(order.getQueueNumber());
            mainFrame.showCard("ORDER_PROCESS");
            reset();
        } else {
            Toast.show(mainFrame, "System Error!", Toast.Type.ERROR);
        }
    }
}
