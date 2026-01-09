package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.MenuItemDAO;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import com.kiloux.restopos.ui.KineticScrollPane;
import com.kiloux.restopos.ui.Toast;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class MenuPanel extends JPanel {
    
    private MainFrame mainFrame;
    private MenuItemDAO menuDAO;
    private DeviceOrientation orientation;

    private JPanel gridContainer;
    private KineticScrollPane scroll;

    public MenuPanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.menuDAO = new MenuItemDAO();
        this.orientation = orientation;
        setLayout(new BorderLayout());
        setOpaque(false);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Menu Kami", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
        
        gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 120, 10));
        applyGridLayout();

        List<MenuItem> items = menuDAO.getAllMenuItems();
        for (MenuItem item : items) {
            gridContainer.add(createMenuItemCard(item));
        }
        
        scroll = new KineticScrollPane(gridContainer);
        add(scroll, BorderLayout.CENTER);
        
        JPanel bottomAction = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomAction.setOpaque(false);
        AnimatedButton cartBtn = new AnimatedButton("🛒 Cart", UIConfig.ACCENT_COLOR, UIConfig.ACCENT_COLOR);
        cartBtn.addActionListener(e -> mainFrame.showCard("CART"));
        bottomAction.add(cartBtn);
        add(bottomAction, BorderLayout.SOUTH); 
    }

    public void applyOrientation(DeviceOrientation newOrientation) {
        if (newOrientation == null) return;
        this.orientation = newOrientation;
        applyGridLayout();
        // Scroll unit logic removed as KineticScroll handles it differently (pixel based)
        revalidate();
        repaint();
    }

    private void applyGridLayout() {
        int cols = orientation == DeviceOrientation.LANDSCAPE ? 3 : 2;
        gridContainer.setLayout(new GridLayout(0, cols, 18, 18));
    }
    
    private JPanel createMenuItemCard(MenuItem item) {
        GlassPanel card = new GlassPanel(new BorderLayout());
        card.setBorderRadius(15);
        card.setPreferredSize(new Dimension(180, 220));
        
        // Image Placeholder
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(150, 120));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBackground(Color.LIGHT_GRAY);
        imgLabel.setOpaque(false); // Image loading logic would go here
        imgLabel.setText(item.getName().substring(0, 1));
        imgLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        card.add(imgLabel, BorderLayout.NORTH);
        
        // Info
        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel nameLbl = new JLabel(item.getName());
        nameLbl.setFont(UIConfig.FONT_BODY);
        nameLbl.setForeground(UIConfig.TEXT_PRIMARY);
        
        JLabel priceLbl = new JLabel(String.format("Rp %.0f", item.getPrice()));
        priceLbl.setFont(UIConfig.FONT_TITLE);
        priceLbl.setForeground(UIConfig.PRIMARY_COLOR);
        
        AnimatedButton addBtn = new AnimatedButton("+ Add", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        addBtn.setFont(UIConfig.FONT_SMALL);
        addBtn.addActionListener(e -> {
            com.kiloux.restopos.service.CartService.getInstance().addItem(item);
            Toast.show(mainFrame, item.getName() + " added to cart", Toast.Type.SUCCESS);
        });
        
        info.add(nameLbl);
        info.add(priceLbl);
        info.add(addBtn);
        
        card.add(info, BorderLayout.CENTER);
        
        return card;
    }
}
