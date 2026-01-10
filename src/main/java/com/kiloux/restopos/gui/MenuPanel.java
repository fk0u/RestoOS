package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.MenuItemDAO;
import com.kiloux.restopos.gui.dialog.ItemDetailDialog;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.ui.AnimatedButton;
import com.kiloux.restopos.ui.GlassPanel;
import com.kiloux.restopos.ui.KineticScrollPane;
import com.kiloux.restopos.ui.Toast;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;

public class MenuPanel extends JPanel {
    
    private MainFrame mainFrame;
    private MenuItemDAO menuDAO;
    private DeviceOrientation orientation;

    private JPanel gridContainer;
    private KineticScrollPane scroll;
    
    // Pagination
    private List<MenuItem> allItems;
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 8;
    private JLabel pageLabel;
    private JButton prevBtn, nextBtn;

    public MenuPanel(MainFrame frame, DeviceOrientation orientation) {
        this.mainFrame = frame;
        this.menuDAO = new MenuItemDAO();
        this.orientation = orientation;
        this.allItems = menuDAO.getAllMenuItems();
        
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // --- Header Section ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Menu Kami", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);
        
        // Search & Filter
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        
        GlassPanel searchBg = new GlassPanel(new BorderLayout());
        searchBg.setBorderRadius(20);
        searchBg.setPreferredSize(new Dimension(200, 40));
        JTextField searchField = new JTextField("Cari menu...");
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchBg.add(searchField, BorderLayout.CENTER);
        
        filterPanel.add(searchBg);
        filterPanel.add(Box.createVerticalStrut(10));

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        chips.setOpaque(false);
        String[] categories = {"All", "Chicken", "Ramen", "Bevs"};
        for (String cat : categories) {
            JButton chip = new JButton(cat);
            chip.setFocusPainted(false);
            chip.setBackground(new Color(255, 255, 255, 30));
            chip.setForeground(Color.WHITE);
            chip.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1));
            chip.setContentAreaFilled(false);
            chips.add(chip);
        }
        filterPanel.add(chips);
        
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(filterPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // --- Grid Section ---
        gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 120, 10)); // Bottom padding for footer
        
        scroll = new KineticScrollPane(gridContainer);
        add(scroll, BorderLayout.CENTER);
        
        // applyGridLayout(); // Moved to end of constructor
        
        // --- Footer Section (Pagination + Cart) ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Pagination Controls
        JPanel pagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pagePanel.setOpaque(false);
        
        prevBtn = createNavBtn("<");
        nextBtn = createNavBtn(">");
        pageLabel = new JLabel("Page 1");
        pageLabel.setForeground(Color.WHITE);
        pageLabel.setFont(UIConfig.FONT_BODY);
        
        prevBtn.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                applyGridLayout();
            }
        });
        
        nextBtn.addActionListener(e -> {
            int maxPage = (int) Math.ceil((double)allItems.size() / ITEMS_PER_PAGE) - 1;
            if (currentPage < maxPage) {
                currentPage++;
                applyGridLayout();
            }
        });
        
        pagePanel.add(prevBtn);
        pagePanel.add(pageLabel);
        pagePanel.add(nextBtn);
        
        footer.add(pagePanel, BorderLayout.WEST);
        
        AnimatedButton cartBtn = new AnimatedButton("🛒 Cart", UIConfig.ACCENT_COLOR, UIConfig.ACCENT_COLOR);
        cartBtn.startPulse();
        cartBtn.addActionListener(e -> mainFrame.showCard("CART"));
        footer.add(cartBtn, BorderLayout.EAST);
        
        add(footer, BorderLayout.SOUTH);
        
        applyGridLayout();
        updatePaginationState();
    }
    
    private JButton createNavBtn(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setBackground(new Color(255, 255, 255, 50));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        btn.setContentAreaFilled(false);
        return btn;
    }

    private void applyGridLayout() {
        gridContainer.removeAll();
        // 2 Cols for Portrait, 4 for Landscape
        int cols = (orientation == DeviceOrientation.PORTRAIT) ? 2 : 4;
        gridContainer.setLayout(new GridLayout(0, cols, 10, 10));
        
        // Pagination Logic
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allItems.size());
        
        List<MenuItem> pageItems = allItems.subList(start, end);
        for (MenuItem item : pageItems) {
            gridContainer.add(createMenuItemCard(item));
        }
        
        updatePaginationState();
        gridContainer.revalidate();
        gridContainer.repaint();
    }
    
    private void updatePaginationState() {
        int maxPage = (int) Math.ceil((double)allItems.size() / ITEMS_PER_PAGE) - 1;
        if (maxPage < 0) maxPage = 0;
        
        pageLabel.setText("Page " + (currentPage + 1) + " / " + (maxPage + 1));
        prevBtn.setEnabled(currentPage > 0);
        nextBtn.setEnabled(currentPage < maxPage);
    }
    
    public void applyOrientation(DeviceOrientation newOrientation) {
        this.orientation = newOrientation;
        applyGridLayout(); // Re-flow grid
    }
    
    private JPanel createMenuItemCard(MenuItem item) {
        GlassPanel card = new GlassPanel(new BorderLayout());
        card.setBorderRadius(20);
        card.setPreferredSize(new Dimension(180, 240));
        
        // Make whole card clickable
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ItemDetailDialog(mainFrame, item).setVisible(true);
            }
        });
        
        // Image Layer
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(180, 140));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        try {
            String path = "/images/" + (item.getImagePath() != null ? item.getImagePath() : "default.jpg");
            java.net.URL imgUrl = getClass().getResource(path);
             if (imgUrl == null) {
                  java.io.File file = new java.io.File("src/main/resources" + path);
                  if (file.exists()) imgUrl = file.toURI().toURL();
             }
            
            if (imgUrl != null) {
                BufferedImage bi = javax.imageio.ImageIO.read(imgUrl);
                if (bi != null) {
                    Image img = bi.getScaledInstance(180, 140, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(img));
                }
            } else {
                 throw new Exception("No image");
            }
        } catch (Exception e) {
            // Fallback Avatar
            imgLabel.setText(item.getName().substring(0, 1));
            imgLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
            imgLabel.setForeground(Color.WHITE);
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(
                Math.abs(item.getName().hashCode() * 12345) % 255,
                Math.abs(item.getName().hashCode() * 67890) % 255, 
                Math.abs(item.getName().hashCode() * 54321) % 255).darker());
        }
        
        card.add(imgLabel, BorderLayout.NORTH);
        
        // Info Layer
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        JLabel nameLbl = new JLabel(item.getName());
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLbl.setForeground(Color.WHITE);
        JLabel priceLbl = new JLabel(String.format("Rp %.0f", item.getPrice()));
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priceLbl.setForeground(UIConfig.ACCENT_COLOR);
        textPanel.add(nameLbl);
        textPanel.add(priceLbl);
        
        AnimatedButton addBtn = new AnimatedButton("+", UIConfig.PRIMARY_COLOR, UIConfig.SECONDARY_COLOR);
        addBtn.setPreferredSize(new Dimension(32, 32));
        addBtn.addActionListener(e -> {
             // Open Detail Dialog even on + button for consistent "Industrial" flow (Note taking)
             new ItemDetailDialog(mainFrame, item).setVisible(true);
        });
        
        info.add(textPanel, BorderLayout.CENTER);
        info.add(addBtn, BorderLayout.EAST);
        
        card.add(info, BorderLayout.SOUTH);
        return card;
    }
}
