package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.service.PrintService;
import com.kiloux.restopos.dao.OrderDAO;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import com.kiloux.restopos.ui.KineticScrollPane;
import com.kiloux.restopos.ui.Toast;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel listPanel;
    private JLabel subtotalLbl, taxLbl, totalLbl;
    private DeviceOrientation orientation;

    private GlassPanel footer;
    private JPanel totals;
    private OrderDAO orderDAO;

    public CartPanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.orientation = orientation;
        this.orderDAO = new OrderDAO();
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Order Summary", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        
        JButton backBtn = new JButton("Back");
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setForeground(UIConfig.TEXT_SECONDARY);
        backBtn.addActionListener(e -> mainFrame.showCard("MENU"));

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(50), BorderLayout.EAST); // Spacer
        add(header, BorderLayout.NORTH);

        // List
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        
        KineticScrollPane scroll = new KineticScrollPane(listPanel);
        add(scroll, BorderLayout.CENTER);

        // Footer (Totals + Checkout)
        footer = new GlassPanel(new BorderLayout(10, 10));
        footer.setBorderRadius(20);
        footer.setPreferredSize(new Dimension(getWidth(), 180));
        footer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        totals = new JPanel(new GridLayout(3, 2));
        totals.setOpaque(false);
        
        JLabel subtotalText = new JLabel("Subtotal");
        subtotalText.setForeground(UIConfig.TEXT_SECONDARY);
        subtotalText.setFont(UIConfig.FONT_BODY);
        totals.add(subtotalText);

        subtotalLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        subtotalLbl.setForeground(UIConfig.TEXT_PRIMARY);
        subtotalLbl.setFont(UIConfig.FONT_BODY);
        totals.add(subtotalLbl);
        
        JLabel taxText = new JLabel("PPN (11%)");
        taxText.setForeground(UIConfig.TEXT_SECONDARY);
        taxText.setFont(UIConfig.FONT_BODY);
        totals.add(taxText);

        taxLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        taxLbl.setForeground(UIConfig.TEXT_PRIMARY);
        taxLbl.setFont(UIConfig.FONT_BODY);
        totals.add(taxLbl);
        
        JLabel totalText = new JLabel("Total");
        totalText.setFont(UIConfig.FONT_TITLE);
        totalText.setForeground(UIConfig.TEXT_PRIMARY);
        totals.add(totalText);
        
        totalLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        totalLbl.setFont(UIConfig.FONT_TITLE);
        totalLbl.setForeground(UIConfig.PRIMARY_COLOR);
        totals.add(totalLbl);
        
        footer.add(totals, BorderLayout.CENTER);
        
        AnimatedButton checkoutBtn = new AnimatedButton("Checkout & Pay", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        checkoutBtn.setPreferredSize(new Dimension(100, 50));
        checkoutBtn.addActionListener(e -> startCheckoutFlow());
        footer.add(checkoutBtn, BorderLayout.SOUTH);
        
        add(footer, BorderLayout.SOUTH);
    }

    public void applyOrientation(DeviceOrientation newOrientation) {
        if (newOrientation == null) return;
        this.orientation = newOrientation;
        int footerHeight = newOrientation == DeviceOrientation.LANDSCAPE ? 140 : 180;
        footer.setPreferredSize(new Dimension(getWidth(), footerHeight));

        int rows = newOrientation == DeviceOrientation.LANDSCAPE ? 2 : 3;
        int cols = newOrientation == DeviceOrientation.LANDSCAPE ? 3 : 2;
        totals.setLayout(new GridLayout(rows, cols));

        revalidate();
        repaint();
    }

    public void refreshCart() {
        listPanel.removeAll();
        CartService cart = CartService.getInstance();
        
        for (CartItem item : cart.getItems()) {
            GlassPanel card = new GlassPanel(new BorderLayout(15, 0));
            card.setBorderRadius(15);
            card.setPreferredSize(new Dimension(380, 80));
            card.setMaximumSize(new Dimension(380, 80));
            card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Image (Thumbnail)
            JLabel imgLabel = new JLabel();
            imgLabel.setPreferredSize(new Dimension(60, 60));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            try {
                String path = "/images/" + (item.getMenuItem().getImagePath() != null ? item.getMenuItem().getImagePath() : "default.jpg");
                java.net.URL imgUrl = getClass().getResource(path);
                if (imgUrl == null) imgUrl = new java.io.File("src/main/resources" + path).toURI().toURL();
                
                if (imgUrl != null) {
                    java.awt.image.BufferedImage bi = javax.imageio.ImageIO.read(imgUrl);
                    if (bi != null) {
                        Image img = bi.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(img));
                    }
                }
            } catch (Exception e) {
                 // Fallback Avatar
                imgLabel.setText(String.valueOf(item.getMenuItem().getName().charAt(0)));
                imgLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
                imgLabel.setForeground(Color.WHITE);
                imgLabel.setOpaque(true);
                imgLabel.setBackground(new Color(
                    Math.abs(item.getMenuItem().getName().hashCode() * 12345) % 255,
                    Math.abs(item.getMenuItem().getName().hashCode() * 67890) % 255, 
                    Math.abs(item.getMenuItem().getName().hashCode() * 54321) % 255).darker());
            }
            card.add(imgLabel, BorderLayout.WEST);
            
            // Info
            JPanel info = new JPanel(new GridLayout(2, 1));
            info.setOpaque(false);
            JLabel name = new JLabel(item.getMenuItem().getName());
            name.setFont(new Font("Segoe UI", Font.BOLD, 14));
            name.setForeground(Color.WHITE);
            String noteText = item.getNotes() != null && !item.getNotes().isEmpty() ? " (+Note)" : "";
            if(!noteText.isEmpty()) name.setText(name.getText() + noteText);
            
            JLabel price = new JLabel("Rp " + String.format("%.0f", item.getSubtotal()));
            price.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            price.setForeground(UIConfig.ACCENT_COLOR);
            
            info.add(name);
            info.add(price);
            card.add(info, BorderLayout.CENTER);
            
            // Controls
            JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            qtyPanel.setOpaque(false);
            
            JButton minus = createCircleBtn("-", UIConfig.DANGER_COLOR);
            JLabel qty = new JLabel(String.valueOf(item.getQuantity()));
            qty.setFont(new Font("Segoe UI", Font.BOLD, 14));
            qty.setForeground(Color.WHITE);
            qty.setPreferredSize(new Dimension(30, 30));
            qty.setHorizontalAlignment(SwingConstants.CENTER);
            JButton plus = createCircleBtn("+", UIConfig.PRIMARY_COLOR);
            
            minus.addActionListener(e -> {
                cart.updateQuantity(item.getMenuItem(), item.getQuantity() - 1);
                refreshCart();
            });
            plus.addActionListener(e -> {
                cart.updateQuantity(item.getMenuItem(), item.getQuantity() + 1);
                refreshCart();
            });
            
            qtyPanel.add(minus);
            qtyPanel.add(qty);
            qtyPanel.add(plus);
            
            JPanel controlWrapper = new JPanel(new GridBagLayout());
            controlWrapper.setOpaque(false);
            controlWrapper.add(qtyPanel);
            card.add(controlWrapper, BorderLayout.EAST);
            
            listPanel.add(card);
            listPanel.add(Box.createVerticalStrut(10));
        }
        
        subtotalLbl.setText(String.format("Rp %.0f", cart.getSubtotal()));
        taxLbl.setText(String.format("Rp %.0f", cart.getTax()));
        totalLbl.setText(String.format("Rp %.0f", cart.getTotal()));
        
        listPanel.revalidate();
        listPanel.repaint();
    }
    
    private JButton createCircleBtn(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(28, 28));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }
    
    private void startCheckoutFlow() {
        CartService cart = CartService.getInstance();
        if (cart.getItems().isEmpty()) {
            Toast.show(mainFrame, "Keranjang Kosong!", Toast.Type.ERROR);
            return;
        }

        // 1. Ask Name
        String customerName = JOptionPane.showInputDialog(this, "Masukkan Nama Pelanggan:", "Checkout", JOptionPane.QUESTION_MESSAGE);
        if (customerName == null || customerName.trim().isEmpty()) return;

        // 2. Ask Payment
        Object[] options = {"Tunai", "QRIS", "Batal"};
        int choice = JOptionPane.showOptionDialog(this, "Pilih Metode Pembayaran", "Payment",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 2 || choice == -1) return; // Batal
        
        String paymentMethod = (choice == 0) ? "CASH" : "QRIS";
        double total = cart.getTotal();
        double payAmount = total; // Mock exact payment for now (or ask input if cash)
        if (choice == 0) { // Cash input simulation
             String payStr = JOptionPane.showInputDialog(this, "Total: " + String.format("%.0f", total) + "\nMasukkan Uang Tunai:");
             if (payStr == null) return;
             try {
                 payAmount = Double.parseDouble(payStr);
                 if(payAmount < total) {
                      Toast.show(mainFrame, "Uang tidak cukup!", Toast.Type.ERROR);
                      return;
                 }
             } catch(NumberFormatException e) {
                 return;
             }
        }
        
        // 3. Process Order
        Order order = new Order();
        order.setTableId(cart.getSelectedTableId() > 0 ? cart.getSelectedTableId() : 0); // 0 for takeaway/unknown
        order.setOrderType("DINE_IN");
        order.setStatus("WAITING");
        order.setTotalAmount(total);
        order.setPaymentStatus("PAID");
        order.setCustomerName(customerName);
        
        int orderId = orderDAO.createOrder(order, cart.getItems());
        
        if (orderId != -1) {
            // Success
            // Print Receipt
            PrintService.generateReceipt(order, cart.getItems(), payAmount, payAmount - total);
            
            // Clear & Redirect
            cart.clear();
            refreshCart();
            
            mainFrame.getOrderProcessPanel().setQueueNumber(order.getQueueNumber());
            mainFrame.showCard("ORDER_PROCESS");
        } else {
             Toast.show(mainFrame, "Gagal memproses order!", Toast.Type.ERROR);
        }
    }
}
