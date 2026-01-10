package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private DeviceOrientation orientation;

    private OnboardingPanel onboarding;
    private TablePanel tableSelect;
    private MenuPanel menu;
    private CartPanel cart;
    private KitchenPanel kitchen;
    private AdminPanel admin;
    private OrderProcessPanel orderProcess;
    private CheckoutPanel checkoutPanel;

    public MainFrame() {
        initUI();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateOrientationForSize(getSize());
            }
        });
    }

    private Image backgroundImage;

    private int parallaxX = 0;
    private int parallaxY = 0;

    private void initUI() {
        // Window Setup (Full Screen)
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Force Full Screen via GraphicsDevice
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }
        
        // Determine Screen Size & Orientation
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // If width > height, it's Landscape.
        orientation = screenSize.width > screenSize.height ? DeviceOrientation.LANDSCAPE : DeviceOrientation.PORTRAIT;
        
        // Auto-configure UIConfig based on screen? 
        // For now, we utilize the responsive layout logic we built.
        
        // Load Background
        try {
            String path = "/images/app_background.png";
            java.net.URL imgUrl = getClass().getResource(path);
            if (imgUrl == null) {
                 imgUrl = new java.io.File("src/main/resources" + path).toURI().toURL();
            }
            if (imgUrl != null) backgroundImage = javax.imageio.ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.err.println("Background image not found: " + e.getMessage());
        }

        // Parallax Listener
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int w = getWidth();
                int h = getHeight();
                int mx = e.getX();
                int my = e.getY();
                parallaxX = (int) (((mx / (float)w) * 20) - 10);
                parallaxY = (int) (((my / (float)h) * 20) - 10);
                repaint();
            }
        });

        // Global Key Listener for F1, F2, F12
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    // EXIT
                    int confirm = JOptionPane.showConfirmDialog(this, "Keluar Aplikasi?", "Exit", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_F12) {
                    // KITCHEN MODE
                    showCard("KITCHEN");
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_F2) {
                    // ADMIN/STAFF MODE (New)
                    // Ensure AdminPanel is created
                    showCard("ADMIN");
                    return true;
                }
            }
            return false;
        });

        // Main Container
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
               
                // Full Screen - No Clipping needed usually, but if we want round corners on screen edges? 
                // No, requested "Full Screen".
                
                if (backgroundImage != null) {
                    int imgW = getWidth() + 40;
                    int imgH = getHeight() + 40;
                    g2.drawImage(backgroundImage, -20 - parallaxX, -20 - parallaxY, imgW, imgH, null);
                } else {
                    g2.setColor(UIConfig.BACKGROUND_BASE);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Dark Gradient Overlay
                GradientPaint overlay = new GradientPaint(
                    0, 0, new Color(15, 23, 42, 200),
                    0, getHeight(), new Color(15, 23, 42, 100)
                );
                g2.setPaint(overlay);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };
        contentPane.setLayout(new BorderLayout());
        contentPane.setOpaque(false);
        setContentPane(contentPane);
        
        // HUD Bar for branding + exit
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

        JLabel titleLabel = new JLabel("Kou Restaurant OS", JLabel.LEFT);
        titleLabel.setFont(UIConfig.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel, BorderLayout.WEST);

        JButton closeBtn = new JButton("Tutup");
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        closeBtn.addActionListener(e -> System.exit(0));
        titleBar.add(closeBtn, BorderLayout.EAST);

        contentPane.add(titleBar, BorderLayout.NORTH);

        // Card Layout Area
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        // Initialize Panels
        onboarding = new OnboardingPanel(this);
        tableSelect = new TablePanel(this, orientation);
        menu = new MenuPanel(this, orientation);
        cart = new CartPanel(this, orientation);
        kitchen = new KitchenPanel(this);
        admin = new AdminPanel(this); // NEW
        orderProcess = new OrderProcessPanel(this);
        checkoutPanel = new CheckoutPanel(this);
        
        cardPanel.add(onboarding, "ONBOARDING");
        cardPanel.add(tableSelect, "TABLE_SELECT");
        cardPanel.add(menu, "MENU");
        cardPanel.add(cart, "CART");
        cardPanel.add(kitchen, "KITCHEN");
        cardPanel.add(admin, "ADMIN"); // NEW
        cardPanel.add(orderProcess, "ORDER_PROCESS");
        cardPanel.add(checkoutPanel, "CHECKOUT");

        contentPane.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "ONBOARDING"); // Set initial view
        applyOrientation(orientation);
        
        // F12 Global Listener
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_F12) {
                    showCard("KITCHEN");
                    return true;
                }
                return false;
            });
    }

    private JPanel createPlaceholderPanel(String name, Color bg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(bg);
        p.add(new JLabel(name));
        return p;
    }

    private void updateOrientationForSize(Dimension size) {
        if (size == null) return;
        DeviceOrientation newOrientation = size.width >= size.height ? DeviceOrientation.LANDSCAPE : DeviceOrientation.PORTRAIT;
        if (newOrientation != this.orientation) {
            applyOrientation(newOrientation);
        }
    }

    private void applyOrientation(DeviceOrientation newOrientation) {
        this.orientation = newOrientation;
        int pad = UIConfig.SAFE_PADDING;
        // Landscape has less bottom pad usually as dock might be side or bottom
        int bottomPad = pad + (newOrientation == DeviceOrientation.LANDSCAPE ? 32 : 80); 
        
        cardPanel.setBorder(BorderFactory.createEmptyBorder(pad, pad, bottomPad, pad));

        // Propagate to child panels
        if (menu != null) menu.applyOrientation(newOrientation);
        if (tableSelect != null) tableSelect.applyOrientation(newOrientation);
        if (cart != null) cart.applyOrientation(newOrientation);

        revalidate();
        repaint();
    }
    
    public void showCard(String name) {
        cardLayout.show(cardPanel, name);
        // Hook to refresh components if needed
        Component val = null;
        for (Component comp : cardPanel.getComponents()) {
            if (comp.isVisible()) {
                val = comp;
                break;
            }
        }
        
        // If we want to refresh cart when showing it (hacky but works for simple app)
        if ("CART".equals(name)) {
             // Find CartPanel instance
             for (Component comp : cardPanel.getComponents()) {
                 if (comp instanceof CartPanel) {
                     ((CartPanel) comp).refreshCart();
                 }
             }
        }
    }

    public OrderProcessPanel getOrderProcessPanel() {
        return orderProcess;
    }

    public CheckoutPanel getCheckoutPanel() {
        return checkoutPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
