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

    public MainFrame() {
        initUI();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateOrientationForSize(getSize());
            }
        });
    }

    private void initUI() {
        // Window Setup (fullscreen, adaptive to orientation)
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        orientation = screen.width >= screen.height ? DeviceOrientation.LANDSCAPE : DeviceOrientation.PORTRAIT;

        setUndecorated(true);
        setResizable(true);
        setBackground(new Color(0, 0, 0, 0));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(screen);
        setLocation(0, 0);
        
        // Main Container with rounded corners shape
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(
                        0, 0, UIConfig.BACKGROUND_GRADIENT_START,
                        getWidth(), getHeight(), UIConfig.BACKGROUND_GRADIENT_END);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(UIConfig.HUD_OVERLAY);
                g2.fillRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 30, 30);
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

        // Create Views (orientation-aware)
        onboarding = new OnboardingPanel(this);
        tableSelect = new TablePanel(this, orientation);
        menu = new MenuPanel(this, orientation);
        cart = new CartPanel(this, orientation);
        kitchen = new KitchenPanel(this);

        cardPanel.add(onboarding, "ONBOARDING");
        cardPanel.add(tableSelect, "TABLE_SELECT");
        cardPanel.add(menu, "MENU");
        cardPanel.add(cart, "CART");
        cardPanel.add(kitchen, "KITCHEN");

        contentPane.add(cardPanel, BorderLayout.CENTER);
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
        int pad = newOrientation == DeviceOrientation.LANDSCAPE ? UIConfig.SAFE_PADDING_LANDSCAPE : UIConfig.SAFE_PADDING;
        int bottomPad = pad + (newOrientation == DeviceOrientation.LANDSCAPE ? 32 : 64);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(pad, pad, bottomPad, pad));

        tableSelect.applyOrientation(newOrientation);
        menu.applyOrientation(newOrientation);
        cart.applyOrientation(newOrientation);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
