package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.MenuItemDAO;
import com.kiloux.restopos.gui.dialog.ItemDetailDialog;
import com.kiloux.restopos.model.MenuItem;
import com.kiloux.restopos.ui.RetroMenuItem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;

public class MenuPanel extends JPanel {
    
    private MainFrame mainFrame;
    private MenuItemDAO menuDAO;
    private DeviceOrientation orientation;

    private JPanel gridContainer;
    private JScrollPane scroll; // Standard JScrollPane for Retro feel (no kinetic)
    
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
        setBackground(UIConfig.BACKGROUND_COLOR);
        
        // --- Header Section ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(true);
        topContainer.setBackground(UIConfig.BACKGROUND_COLOR);
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("MENU KAMI", JLabel.CENTER);
        title.setFont(UIConfig.FONT_HEADER);
        title.setForeground(UIConfig.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);
        
        // Search & Filter (Retro Style)
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        
        // Search Box (Squared)
        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.setBorder(BorderFactory.createLineBorder(UIConfig.BORDER_COLOR));
        searchBox.setBackground(Color.WHITE);
        searchBox.setPreferredSize(new Dimension(200, 30));
        
        JTextField searchField = new JTextField("CARI MENU...");
        searchField.setBorder(null);
        searchField.setFont(UIConfig.FONT_BODY);
        searchBox.add(searchField, BorderLayout.CENTER);
        
        filterPanel.add(searchBox);
        filterPanel.add(Box.createVerticalStrut(10));

        // Chips (Retro Buttons)
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chips.setOpaque(false);
        String[] categories = {"ALL", "CHICKEN", "RAMEN", "BEVS"};
        for (String cat : categories) {
            JButton chip = new JButton(cat);
            chip.setFont(UIConfig.FONT_SMALL);
            chip.setBackground(UIConfig.SECONDARY_COLOR);
            chip.setForeground(UIConfig.TEXT_PRIMARY);
            chip.setFocusPainted(false);
            chips.add(chip);
        }
        filterPanel.add(chips);
        
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(filterPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // --- Grid Section ---
        gridContainer = new JPanel();
        gridContainer.setOpaque(true);
        gridContainer.setBackground(UIConfig.BACKGROUND_COLOR);
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        scroll = new JScrollPane(gridContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(UIConfig.BACKGROUND_COLOR);
        add(scroll, BorderLayout.CENTER);
        
        // --- Footer Section (Pagination + Cart) ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(true);
        footer.setBackground(UIConfig.BACKGROUND_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConfig.BORDER_COLOR));
        
        // Pagination Controls
        JPanel pagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pagePanel.setOpaque(false);
        
        prevBtn = new JButton("<");
        nextBtn = new JButton(">");
        pageLabel = new JLabel("PAGE 1");
        pageLabel.setFont(UIConfig.FONT_RETRO_DIGITAL);
        
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
        
        JButton cartBtn = new JButton("CART [0] - Rp 0");
        cartBtn.setFont(UIConfig.FONT_BODY);
        cartBtn.setBackground(UIConfig.PRIMARY_COLOR);
        cartBtn.setForeground(Color.WHITE);
        cartBtn.addActionListener(e -> mainFrame.showCard("CART"));
        footer.add(cartBtn, BorderLayout.EAST);
        
        // Real-time Update
        com.kiloux.restopos.service.CartService.getInstance().addListener(() -> {
            int count = com.kiloux.restopos.service.CartService.getInstance().getItems().stream().mapToInt(com.kiloux.restopos.model.CartItem::getQuantity).sum();
            double total = com.kiloux.restopos.service.CartService.getInstance().getTotal();
            cartBtn.setText(String.format("CART [%d] - Rp %,.0f", count, total));
        });
        
        add(footer, BorderLayout.SOUTH);
        
        applyGridLayout();
        updatePaginationState();
    }

    private void applyGridLayout() {
        gridContainer.removeAll();
        // 4 Columns Fixed (As per user request)
        gridContainer.setLayout(new GridLayout(0, 4, 10, 10)); // 4 Cols
        
        // Pagination Logic
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, allItems.size());
        
        List<MenuItem> pageItems = allItems.subList(start, end);
        int index = start + 1;
        
        for (MenuItem item : pageItems) {
            final int currIndex = index++;
            RetroMenuItem card = new RetroMenuItem(
                currIndex, 
                item.getName().toUpperCase(), 
                String.format("Rp %.0f", item.getPrice()), 
                loadImageFor(item),
                () -> new ItemDetailDialog(mainFrame, item).setVisible(true)
            );
            gridContainer.add(card);
        }
        
        // Fill empty slots to maintain grid structure (optional, but looks better in retro grid)
        int remainder = ITEMS_PER_PAGE - pageItems.size();
        for(int i=0; i<remainder; i++) {
             JPanel placeholder = new JPanel();
             placeholder.setOpaque(false);
             gridContainer.add(placeholder);
        }
        
        updatePaginationState();
        gridContainer.revalidate();
        gridContainer.repaint();
    }
    
    private Image loadImageFor(MenuItem item) {
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
                    return bi.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                }
            } 
        } catch (Exception e) {
            // Null returns default placeholder in RetroMenuItem
        }
        return null; // RetroMenuItem handles null with isometric box
    }
    
    private void updatePaginationState() {
        int maxPage = (int) Math.ceil((double)allItems.size() / ITEMS_PER_PAGE) - 1;
        if (maxPage < 0) maxPage = 0;
        
        pageLabel.setText("PAGE " + (currentPage + 1) + " / " + (maxPage + 1));
        prevBtn.setEnabled(currentPage > 0);
        nextBtn.setEnabled(currentPage < maxPage);
    }
    
    public void applyOrientation(DeviceOrientation newOrientation) {
        this.orientation = newOrientation;
        // Grid is fixed 4 columns per user spec ("Grid (4 kolom)"), so orientation might just change container size
        revalidate();
    }
}
