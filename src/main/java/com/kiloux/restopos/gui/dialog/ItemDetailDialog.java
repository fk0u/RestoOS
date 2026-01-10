package com.kiloux.restopos.gui.dialog;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.service.CartService;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import java.awt.*;
import javax.swing.*;

public class ItemDetailDialog extends JDialog {

    private MenuItem item;
    private int quantity = 1;
    private JTextArea noteArea;

    public ItemDetailDialog(Frame owner, MenuItem item) {
        super(owner, "Item Details", true);
        this.item = item;
        setUndecorated(true);
        setBackground(new Color(0,0,0,0));
        setSize(500, 600);
        setLocationRelativeTo(owner);

        JPanel content = new GlassPanel(new BorderLayout());
        ((GlassPanel)content).setBorderRadius(30);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Header (Image + Title)
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        
        // Close Button
        JButton closeBtn = new JButton("X");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> dispose());
        top.add(closeBtn, BorderLayout.EAST);
        
        content.add(top, BorderLayout.NORTH);

        // 2. Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        
        // Image Placeholder
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(200, 200));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Load Image Logic (Simplified)
        try {
             String path = "/images/" + (item.getImagePath() != null ? item.getImagePath() : "default.jpg");
             java.net.URL imgUrl = getClass().getResource(path);
             if(imgUrl==null) imgUrl = new java.io.File("src/main/resources" + path).toURI().toURL();
             if(imgUrl!=null) {
                 ImageIcon icon = new ImageIcon(javax.imageio.ImageIO.read(imgUrl).getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                 imgLabel.setIcon(icon);
             }
        } catch(Exception e) {
             imgLabel.setText(item.getName());
             imgLabel.setForeground(Color.WHITE);
        }
        body.add(imgLabel);
        body.add(Box.createVerticalStrut(20));
        
        JLabel nameLbl = new JLabel(item.getName());
        nameLbl.setFont(UIConfig.FONT_TITLE);
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(nameLbl);
        
        JLabel descLbl = new JLabel("<html><center>" + (item.getDescription() != null ? item.getDescription() : "Delicious and fresh.") + "</center></html>");
        descLbl.setFont(UIConfig.FONT_BODY);
        descLbl.setForeground(UIConfig.TEXT_SECONDARY);
        descLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(descLbl);
        
        body.add(Box.createVerticalStrut(20));
        
        // Notes Section
        JLabel noteTitle = new JLabel("Catatan (Optional):");
        noteTitle.setForeground(Color.WHITE);
        noteTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        body.add(noteTitle);
        
        noteArea = new JTextArea(3, 20);
        noteArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(noteArea);
        scroll.setMaximumSize(new Dimension(400, 80));
        body.add(scroll);
        
        body.add(Box.createVerticalStrut(20));
        
        // Quantity
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        qtyPanel.setOpaque(false);
        JButton minBtn = new JButton("-");
        JLabel qtyLbl = new JLabel("1");
        qtyLbl.setForeground(Color.WHITE);
        qtyLbl.setFont(UIConfig.FONT_HEADER);
        JButton plusBtn = new JButton("+");
        
        minBtn.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                qtyLbl.setText(String.valueOf(quantity));
            }
        });
        plusBtn.addActionListener(e -> {
            quantity++;
            qtyLbl.setText(String.valueOf(quantity));
        });
        
        qtyPanel.add(minBtn);
        qtyPanel.add(qtyLbl);
        qtyPanel.add(plusBtn);
        body.add(qtyPanel);
        
        content.add(body, BorderLayout.CENTER);
        
        // 3. Footer (Action)
        AnimatedButton addBtn = new AnimatedButton("Add to Order - Rp " + item.getPrice(), UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        addBtn.setPreferredSize(new Dimension(200, 50));
        addBtn.addActionListener(e -> {
            CartService.getInstance().addItemWithNotes(item, quantity, noteArea.getText());
            dispose();
        });
        
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.add(addBtn);
        content.add(footer, BorderLayout.SOUTH);

        setContentPane(content);
    }
}
