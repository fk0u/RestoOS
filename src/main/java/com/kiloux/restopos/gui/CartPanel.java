package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

public class CartPanel extends JPanel {

    private MainFrame mainFrame;
    private CartService cartService;
    private DeviceOrientation orientation;
    
    // UI
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    
    public CartPanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.cartService = CartService.getInstance();
        this.orientation = orientation;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(UIConfig.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Window padding
        
        // --- Header ---
        JLabel title = new JLabel("SHOPPING CART");
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);
        
        // --- Central Grid (The List) ---
        // Using JTable for true 2000s application feel
        String[] columns = {"Count", "Item Name", "Price", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read only
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setFont(UIConfig.FONT_BODY);
        cartTable.setRowHeight(24);
        cartTable.setShowGrid(true);
        cartTable.setGridColor(Color.LIGHT_GRAY);
        cartTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scroll = new JScrollPane(cartTable);
        // Win98 Inset Border for visual depth
        scroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scroll.getViewport().setBackground(Color.WHITE); 
        
        add(scroll, BorderLayout.CENTER);
        
        // --- Footer (Actions & Total) ---
        JPanel footer = new JPanel(new BorderLayout(10, 10));
        footer.setOpaque(false);
        
        // Total Box
        JPanel funcPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        funcPanel.setOpaque(false);
        
        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(UIConfig.FONT_TITLE);
        totalLabel.setForeground(UIConfig.TEXT_PRIMARY);
        funcPanel.add(totalLabel);
        
        footer.add(funcPanel, BorderLayout.NORTH);
        
        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        btnPanel.setOpaque(false);
        
        RetroButton backBtn = new RetroButton("<< Back");
        backBtn.addActionListener(e -> mainFrame.showCard("MENU"));
        
        RetroButton clearBtn = new RetroButton("Clear", UIConfig.DANGER_COLOR, null);
        clearBtn.setForeground(Color.RED.darker());
        clearBtn.addActionListener(e -> {
            cartService.clearCart();
            refreshCart();
        });
        
        RetroButton checkoutBtn = new RetroButton("CHECKOUT >>");
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.addActionListener(e -> {
            if (cartService.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.showCard("CHECKOUT");
        });
        
        btnPanel.add(backBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(checkoutBtn);
        
        footer.add(btnPanel, BorderLayout.SOUTH);
        
        add(footer, BorderLayout.SOUTH);
    }
    
    public void applyOrientation(DeviceOrientation newOrientation) {
        this.orientation = newOrientation;
        // Adjust if needed (layout handles most)
    }
    
    public void refreshCart() {
        tableModel.setRowCount(0);
        List<CartItem> items = cartService.getItems();
        double total = 0;
        
        for (CartItem item : items) {
            double sub = item.getMenuItem().getPrice() * item.getQuantity();
            total += sub;
            tableModel.addRow(new Object[]{
                item.getQuantity(),
                item.getMenuItem().getName(),
                String.format("%.0f", item.getMenuItem().getPrice()),
                String.format("%.0f", sub)
            });
        }
        
        totalLabel.setText(String.format("Total: Rp %.0f", total));
    }
}
