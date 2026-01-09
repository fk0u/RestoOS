package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import com.kiloux.restopos.ui.KineticScrollPane;

import java.awt.*;
import javax.swing.*;

public class CartPanel extends JPanel {
    private MainFrame mainFrame;
    private JPanel listPanel;
    private JLabel subtotalLbl, taxLbl, totalLbl;
    private DeviceOrientation orientation;

    private GlassPanel footer;
    private JPanel totals;

    public CartPanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.orientation = orientation;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Order Summary", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        
        JButton backBtn = new JButton("Back");
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
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
        
        totals.add(new JLabel("Subtotal"));
        subtotalLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        totals.add(subtotalLbl);
        
        totals.add(new JLabel("PPN (11%)"));
        taxLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        totals.add(taxLbl);
        
        JLabel totalText = new JLabel("Total");
        totalText.setFont(UIConfig.FONT_TITLE);
        totals.add(totalText);
        
        totalLbl = new JLabel("Rp 0", SwingConstants.RIGHT);
        totalLbl.setFont(UIConfig.FONT_TITLE);
        totalLbl.setForeground(UIConfig.PRIMARY_COLOR);
        totals.add(totalLbl);
        
        footer.add(totals, BorderLayout.CENTER);
        
        AnimatedButton checkoutBtn = new AnimatedButton("Checkout & Pay", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        checkoutBtn.setPreferredSize(new Dimension(100, 50));
        checkoutBtn.addActionListener(e -> showPaymentDialog());
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
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false);
            p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0,0,0,30)));
            p.setMaximumSize(new Dimension(400, 70));
            
            JLabel name = new JLabel("<html><b>" + item.getMenuItem().getName() + "</b><br/>Rp " + item.getMenuItem().getPrice() + "</html>");
            
            JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            qtyPanel.setOpaque(false);
            
            JButton minus = new JButton("-");
            JLabel qty = new JLabel(" " + item.getQuantity() + " ");
            JButton plus = new JButton("+");
            
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
            
            p.add(name, BorderLayout.CENTER);
            p.add(qtyPanel, BorderLayout.EAST);
            
            listPanel.add(p);
        }
        
        subtotalLbl.setText(String.format("Rp %.0f", cart.getSubtotal()));
        taxLbl.setText(String.format("Rp %.0f", cart.getTax()));
        totalLbl.setText(String.format("Rp %.0f", cart.getTotal()));
        
        listPanel.revalidate();
        listPanel.repaint();
    }
    
    private void showPaymentDialog() {
        JDialog d = new JDialog(mainFrame, "Payment", true);
        d.setSize(350, 400);
        d.setLocationRelativeTo(mainFrame);
        d.setUndecorated(true);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(UIConfig.ACCENT_COLOR, 2));
        
        JLabel title = new JLabel("Pilih Metode Pembayaran", SwingConstants.CENTER);
        title.setFont(UIConfig.FONT_TITLE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        content.add(title, BorderLayout.NORTH);
        
        JPanel btns = new JPanel(new GridLayout(2, 1, 20, 20));
        btns.setOpaque(false);
        btns.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        AnimatedButton cashBtn = new AnimatedButton("CASH", UIConfig.WARNING_COLOR, UIConfig.WARNING_COLOR.brighter());
        cashBtn.addActionListener(e -> {
            CartService.getInstance().clear();
            JOptionPane.showMessageDialog(d, "Pembayaran Tunai Berhasil!\nStruk sedang dicetak...");
            d.dispose();
            mainFrame.showCard("ONBOARDING");
        });
        
        AnimatedButton qrisBtn = new AnimatedButton("QRIS Scan", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        qrisBtn.addActionListener(e -> {
             CartService.getInstance().clear();
             JOptionPane.showMessageDialog(d, "QRIS Payment Berhasil!\nStruk sedang dicetak...");
             d.dispose();
             mainFrame.showCard("ONBOARDING");
        });
        
        btns.add(cashBtn);
        btns.add(qrisBtn);
        
        content.add(btns, BorderLayout.CENTER);
        
        JButton cancel = new JButton("Batal");
        cancel.addActionListener(e -> d.dispose());
        content.add(cancel, BorderLayout.SOUTH);
        
        d.setContentPane(content);
        d.setVisible(true);
    }
}
