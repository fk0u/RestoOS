package com.kiloux.restopos.gui.dialog;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class ItemDetailDialog extends JDialog {

    private MenuItem item;
    private int quantity = 1;
    private JTextArea noteArea;

    public ItemDetailDialog(Frame owner, MenuItem item) {
        super(owner, "Item Properties", true);
        this.item = item;
        setUndecorated(true);
        setSize(400, 450);
        setLocationRelativeTo(owner);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConfig.WIN98_GREY);
        content.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // --- Title Bar ---
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(UIConfig.WIN98_TITLE_ACTIVE_START);
        titleBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        
        JLabel titleLbl = new JLabel("Properties: " + item.getName());
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
        
        JButton closeIcon = new JButton("X");
        closeIcon.setMargin(new Insets(0,0,0,0));
        closeIcon.setPreferredSize(new Dimension(16, 16));
        closeIcon.setBackground(UIConfig.WIN98_GREY);
        closeIcon.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        closeIcon.addActionListener(e -> dispose());
        
        titleBar.add(titleLbl, BorderLayout.WEST);
        titleBar.add(closeIcon, BorderLayout.EAST);
        content.add(titleBar, BorderLayout.NORTH);

        // --- Body (Tabbed Pane Style or just Form) ---
        JPanel body = new JPanel(new GridBagLayout());
        body.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        body.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Item Name
        gbc.gridx = 0; gbc.gridy = 0;
        body.add(new JLabel("Item Name:"), gbc);
        
        gbc.gridx = 1;
        JTextField nameField = new JTextField(item.getName());
        nameField.setEditable(false);
        nameField.setBackground(Color.WHITE);
        nameField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        body.add(nameField, gbc);
        
        // Price
        gbc.gridx = 0; gbc.gridy = 1;
        body.add(new JLabel("Price Unit:"), gbc);
        
        gbc.gridx = 1;
        JTextField priceField = new JTextField(String.format("Rp %.0f", item.getPrice()));
        priceField.setEditable(false);
        priceField.setBackground(Color.WHITE);
        priceField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        body.add(priceField, gbc);
        
        // Description (Large Text)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        body.add(new JLabel("Description:"), gbc);
        
        gbc.gridy = 3;
        JTextArea descArea = new JTextArea(item.getDescription() != null ? item.getDescription() : "No description available.");
        descArea.setRows(4);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Larger text
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        body.add(descScroll, gbc);
        
        // Notes
        gbc.gridy = 4;
        body.add(new JLabel("User Notes:"), gbc);
        
        gbc.gridy = 5;
        noteArea = new JTextArea(3, 20);
        JScrollPane noteScroll = new JScrollPane(noteArea);
        noteScroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        body.add(noteScroll, gbc);
        
        // Quantity
        gbc.gridy = 6; gbc.gridwidth = 1;
        body.add(new JLabel("Quantity:"), gbc);
        
        gbc.gridx = 1;
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        qtyPanel.setOpaque(false);
        
        JTextField qtyField = new JTextField("1", 3);
        qtyField.setHorizontalAlignment(JTextField.CENTER);
        qtyField.setEditable(false);
        
        RetroButton upBtn = new RetroButton("+");
        upBtn.setPreferredSize(new Dimension(20, 20));
        RetroButton downBtn = new RetroButton("-");
        downBtn.setPreferredSize(new Dimension(20, 20));
        
        upBtn.addActionListener(e -> {
            quantity++;
            qtyField.setText(String.valueOf(quantity));
        });
        
        downBtn.addActionListener(e -> {
            if(quantity > 1) {
                quantity--;
                qtyField.setText(String.valueOf(quantity));
            }
        });
        
        qtyPanel.add(downBtn);
        qtyPanel.add(qtyField);
        qtyPanel.add(upBtn);
        body.add(qtyPanel, gbc);
        
        content.add(body, BorderLayout.CENTER);
        
        // --- Footer Buttons ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        RetroButton okBtn = new RetroButton("Add to Order");
        okBtn.addActionListener(e -> {
            CartService.getInstance().addItemWithNotes(item, quantity, noteArea.getText());
            dispose();
        });
        
        RetroButton cancelBtn = new RetroButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        
        footer.add(okBtn);
        footer.add(cancelBtn);
        
        content.add(footer, BorderLayout.SOUTH);

        setContentPane(content);
    }
}
