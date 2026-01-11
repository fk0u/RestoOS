package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CheckoutPanel extends JPanel {

    private MainFrame mainFrame;
    private CartService cartService;
    private OrderDAO orderDAO;
    
    // UI Elements
    private JTextArea receiptArea;
    private JTextField customerNameField;
    private JRadioButton p58mm, p80mm;
    
    // Payment Elements
    private JTabbedPane paymentTabs;
    private JTextField cashPaidField;
    private JLabel changeLabel;
    private JTextField refNumberField;
    
    private DecimalFormat df = new DecimalFormat("#,###");

    public CheckoutPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.cartService = CartService.getInstance();
        this.orderDAO = new OrderDAO();
        
        setLayout(new BorderLayout(15, 15));
        setOpaque(false); // Transparent for background gradient
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Checkout Counter");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        
        // --- Main Content ---
        JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
        content.setOpaque(false);
        
        // --- LEFT: Interaction (Glass Panel) ---
        JPanel inputPanel = createGlassPanel("Transaction Details");
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        
        // Customer Info
        JPanel custForm = new JPanel(new GridLayout(2, 2, 8, 8));
        custForm.setOpaque(false);
        custForm.add(createLabel("Customer Name:"));
        customerNameField = createTextField();
        custForm.add(customerNameField);
        custForm.add(createLabel("Order Type:"));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Dine In", "Take Away", "Delivery"});
        typeBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        custForm.add(typeBox);
        inputPanel.add(custForm);
        inputPanel.add(Box.createVerticalStrut(15));
        
        // Advanced Actions Grid
        JPanel advancedBox = new JPanel(new GridLayout(2, 2, 8, 8));
        advancedBox.setOpaque(false);
        advancedBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Advanced Actions", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.WHITE));
        
        RetroButton splitBillBtn = new RetroButton("Split Bill");
        splitBillBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Split Bill Feature (Mock)"));
        advancedBox.add(splitBillBtn);
        
        RetroButton voucherBtn = new RetroButton("Apply Voucher");
        voucherBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Voucher Applied: DISC10 (Mock)"));
        advancedBox.add(voucherBtn);
        
        RetroButton memberBtn = new RetroButton("Member Lookup");
        memberBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Member Found: Gold Tier (Mock)"));
        advancedBox.add(memberBtn);
        
        RetroButton noteBtn = new RetroButton("Add Note");
        noteBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Kitchen Note Added."));
        advancedBox.add(noteBtn);
        
        inputPanel.add(advancedBox);
        inputPanel.add(Box.createVerticalStrut(15));
        
        // Payment Tabs
        paymentTabs = new JTabbedPane();
        paymentTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Cash Tab
        JPanel cashPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        cashPanel.setOpaque(false);
        cashPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel totalLabel = new JLabel("Rp 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE); // Make sure this updates
        
        cashPanel.add(createLabel("Total Due:"));
        cashPanel.add(totalLabel);
        cashPanel.add(createLabel("Cash Received:"));
        cashPaidField = createTextField();
        cashPanel.add(cashPaidField);
        cashPanel.add(createLabel("Change:"));
        changeLabel = new JLabel("Rp 0");
        changeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        changeLabel.setForeground(UIConfig.PRIMARY_COLOR);
        cashPanel.add(changeLabel);
        
        paymentTabs.addTab("CASH", cashPanel);
        
        // Non-Cash
        JPanel nonCashPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        nonCashPanel.setOpaque(false);
        nonCashPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nonCashPanel.add(createLabel("Method:"));
        nonCashPanel.add(new JComboBox<>(new String[]{"QRIS", "Debit BCA", "Credit Card"}));
        nonCashPanel.add(createLabel("Ref Number:"));
        refNumberField = createTextField();
        nonCashPanel.add(refNumberField);
        paymentTabs.addTab("NON-CASH", nonCashPanel);
        
        inputPanel.add(paymentTabs);
        
        content.add(inputPanel);
        
        // --- RIGHT: Receipt Preview (Glass Panel) ---
        JPanel previewPanel = createGlassPanel("Live Receipt Preview");
        previewPanel.setLayout(new BorderLayout());
        
        JPanel opts = new JPanel(new FlowLayout(FlowLayout.LEFT));
        opts.setOpaque(false);
        ButtonGroup bg = new ButtonGroup();
        p58mm = new JRadioButton("58mm", true); p58mm.setOpaque(false); p58mm.setForeground(Color.WHITE); p58mm.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p80mm = new JRadioButton("80mm"); p80mm.setOpaque(false); p80mm.setForeground(Color.WHITE); p80mm.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bg.add(p58mm); bg.add(p80mm);
        ActionListener refreshAl = e -> generateReceiptPreview();
        p58mm.addActionListener(refreshAl); p80mm.addActionListener(refreshAl);
        opts.add(createLabel("Paper Size: ")); opts.add(p58mm); opts.add(p80mm);
        previewPanel.add(opts, BorderLayout.NORTH);
        
        receiptArea = new JTextArea();
        receiptArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        receiptArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(receiptArea);
        scroll.setBorder(null); // Flat look
        previewPanel.add(scroll, BorderLayout.CENTER);
        
        content.add(previewPanel);
        add(content, BorderLayout.CENTER);
        
        // --- Footer Buttons ---
        JPanel charBar = new JPanel(new GridLayout(1, 2, 20, 0));
        charBar.setOpaque(false);
        charBar.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        RetroButton cancelBtn = new RetroButton("CANCEL TRANSACTION");
        cancelBtn.setForeground(new Color(200, 50, 50));
        cancelBtn.addActionListener(e -> mainFrame.showCard("CART"));
        
        RetroButton payBtn = new RetroButton("CONFIRM & PRINT");
        payBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        payBtn.setForeground(new Color(0, 100, 0));
        payBtn.addActionListener(e -> processOrder(typeBox.getSelectedItem().toString()));
        
        charBar.add(cancelBtn);
        charBar.add(payBtn);
        add(charBar, BorderLayout.SOUTH);

        // Listeners & Init
        cashPaidField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calcChange(); }
            public void removeUpdate(DocumentEvent e) { calcChange(); }
            public void changedUpdate(DocumentEvent e) { calcChange(); }
        });
        
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent event) { 
                generateReceiptPreview(); 
                totalLabel.setText("Rp " + df.format(cartService.getTotal()));
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Slate/Blue Gradient Background for the Panel itself
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
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), title, TitledBorder.LEFT, TitledBorder.TOP,  new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        return p;
    }
    
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.WHITE);
        return l;
    }
    
    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        return tf;
    }

    private JPanel createSectionPanel(String title) {
        return createGlassPanel(title);
    }

    // ... calcChange, generateReceiptPreview, etc. (Keep Logic) ...
    private void calcChange() {
        try {
            double paid = Double.parseDouble(cashPaidField.getText());
            double total = cartService.getTotal();
            double change = paid - total;
            changeLabel.setText("Rp " + df.format(change));
            if (change < 0) changeLabel.setForeground(new Color(255, 100, 100)); // Light Red
            else changeLabel.setForeground(new Color(100, 255, 100)); // Light Green
        } catch (NumberFormatException e) {
            changeLabel.setText("Rp 0");
        }
    }
    
    private void generateReceiptPreview() {
        boolean is80 = p80mm.isSelected();
        int width = is80 ? 46 : 30; // Approx char width for 80mm vs 58mm monospace
        
        StringBuilder sb = new StringBuilder();
        String line = "-".repeat(width);
        
        centerText(sb, "KOU RESTAURANT", width);
        centerText(sb, "Jl. Restaurant POS No. 1", width);
        sb.append(line).append("\n");
        
        String dt = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(LocalDateTime.now());
        sb.append("Date: ").append(dt).append("\n");
        sb.append("Cust: ").append(customerNameField.getText().isEmpty() ? "Guest" : customerNameField.getText()).append("\n");
        sb.append(line).append("\n");
        
        for (CartItem item : cartService.getItems()) {
            String name = item.getMenuItem().getName();
            int qty = item.getQuantity();
            double sub = item.getMenuItem().getPrice() * qty;
            
            if (name.length() > width - 10) name = name.substring(0, width - 10);
            
            // Format: Name on one line, Qty x Price on next? Or simple table?
            // Simple: Name .... Price
            sb.append(name).append("\n");
            String qtyPrice = qty + " x " + df.format(item.getMenuItem().getPrice());
            String totalStr = df.format(sub);
            
            // Align right
            sb.append(String.format(" %-15s %" + (width - 17) + "s\n", qtyPrice, totalStr));
        }
        
        sb.append(line).append("\n");
        String sub = df.format(cartService.getSubtotal());
        String tax = df.format(cartService.getTax());
        String tot = df.format(cartService.getTotal());
        
        sb.append(padRight("Subtotal", width/2)).append(padLeft(sub, width - width/2)).append("\n");
        sb.append(padRight("PPN (11%)", width/2)).append(padLeft(tax, width - width/2)).append("\n");
        sb.append(padRight("TOTAL", width/2)).append(padLeft(tot, width - width/2)).append("\n");
        sb.append(line).append("\n");
        
        centerText(sb, "THANK YOU!", width);
        
        receiptArea.setText(sb.toString());
    }
    
    private void centerText(StringBuilder sb, String text, int width) {
        int pad = (width - text.length()) / 2;
        if (pad > 0) sb.append(" ".repeat(pad));
        sb.append(text).append("\n");
    }
    
    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
    
    private String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
    
    private void processOrder(String orderType) {
        if (cartService.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        
        String method = "CASH";
        if (paymentTabs.getSelectedIndex() == 1) {
             method = "NON_CASH"; // Or specific value from combo
        } else {
             // Validate Cash
             try {
                 double v = Double.parseDouble(cashPaidField.getText());
                 if (v < cartService.getTotal()) {
                     JOptionPane.showMessageDialog(this, "Insufficient Cash!", "Error", JOptionPane.WARNING_MESSAGE);
                     return;
                 }
             } catch(Exception e) {
                 JOptionPane.showMessageDialog(this, "Invalid Cash Amount", "Error", JOptionPane.WARNING_MESSAGE);
                 return;
             }
        }
        
        Order ord = new Order();
        ord.setTableId(cartService.getSelectedTableId() == -1 ? 0 : cartService.getSelectedTableId());
        ord.setOrderType(orderType);
        ord.setStatus("PENDING");
        ord.setCustomerName(customerNameField.getText());
        ord.setTotalAmount(cartService.getTotal());
        ord.setPaymentStatus("PAID");
        
        if (orderDAO.createOrder(ord, cartService.getItems()) != -1) {
            JOptionPane.showMessageDialog(this, "Transaction Successful!\nPrinting Receipt...", "Success", JOptionPane.INFORMATION_MESSAGE);
            cartService.clearCart();
            mainFrame.showCard("ONBOARDING"); 
        } else {
            JOptionPane.showMessageDialog(this, "Transaction Failed Db Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
