package com.kiloux.restopos.gui;

import com.kiloux.restopos.apps.*;
import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainFrame extends JFrame {

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private DeviceOrientation orientation;

    // View References
    private OnboardingPanel onboarding;
    private TablePanel tableSelect;
    private MenuPanel menu;
    private CartPanel cart;
    private KitchenPanel kitchen;
    private AdminPanel admin;
    private OrderProcessPanel orderProcess;
    private CheckoutPanel checkoutPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private CustomerMonitorPanel monitorPanel;

    // Environment Components
    private JDesktopPane desktop;
    private JPanel taskbar;
    private JLabel timeLabel;
    
    // Apps (Wrapped)
    private JInternalFrame posWindow;
    private JInternalFrame adminWindow;
    private JInternalFrame kitchenWindow;
    private JInternalFrame monitorWindow;

    public MainFrame() {
        initUI();
        // Orientation listener kept for resizing if needed
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateOrientationForSize(getSize());
            }
        });
    }

    private void initUI() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        orientation = screenSize.width > screenSize.height ? DeviceOrientation.LANDSCAPE : DeviceOrientation.PORTRAIT;
        
        // --- Root Container ---
        JPanel contentPane = new JPanel(new CardLayout());
        setContentPane(contentPane);
        
        // 1. BOOT SCREEN
        BootPanel bootCheck = new BootPanel(() -> {
            CardLayout cl = (CardLayout) contentPane.getLayout();
            cl.show(contentPane, "DESKTOP");
            showLoginWindow();
        });
        contentPane.add(bootCheck, "BOOT");
        
        // 2. DESKTOP ENVIRONMENT
        JPanel osPanel = new JPanel(new BorderLayout());
        osPanel.setBackground(new Color(0, 0, 0)); // Black base
        
        // Wallpaper Layer (JLayeredPane ideally, but JDesktopPane is opaque usually)
        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Longhorn Slate/Plex Gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(58, 110, 165), 0, getHeight(), new Color(20, 40, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Curve (Abstract Vista)
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillOval(-100, getHeight()/2, getWidth() + 200, getHeight());
            }
        };
        osPanel.add(desktop, BorderLayout.CENTER);
        
        // Sidebar (Vista Style)
        osPanel.add(new SidebarPanel(), BorderLayout.EAST);
        
        // Taskbar
        initTaskbar(osPanel);
        
        contentPane.add(osPanel, "DESKTOP");
        
        // Prepare App Windows (Lazy Load or Init Hidden)
        initAppWindows();
        
        // Start Global Key Listeners
        initGlobalKeys();
    }
    
    private void showLoginWindow() {
        // Create Login as internal frame
        JInternalFrame loginFrame = new JInternalFrame("Welcome to RestoOS", false, false, false, false);
        loginPanel = new LoginPanel(this);
        loginFrame.setContentPane(loginPanel);
        loginFrame.pack();
        
        // Center it
        Dimension d = desktop.getSize();
        if (d.width == 0) d = Toolkit.getDefaultToolkit().getScreenSize(); // Fallback
        loginFrame.setLocation((d.width - loginFrame.getWidth())/2, (d.height - loginFrame.getHeight())/2);
        loginFrame.setVisible(true);
        desktop.add(loginFrame);
        try { loginFrame.setSelected(true); } catch(Exception e){}
    }
    
    public void unlockEnvironment(String role) {
        // Remove Login Window
        for(JInternalFrame f : desktop.getAllFrames()) {
            if (f.getTitle().contains("Welcome")) f.dispose();
        }
        
        // Open specific windows based on role
        if ("CLIENT".equalsIgnoreCase(role)) {
            openApp(monitorWindow);
            try { monitorWindow.setMaximum(true); } catch(Exception e){}
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            openApp(adminWindow);
             try { adminWindow.setMaximum(true); } catch(Exception e){}
        } else {
            // Cashier - Full POS
            openApp(posWindow);
             try { posWindow.setMaximum(true); } catch(Exception e){}
        }
    }
    
    private void openApp(JInternalFrame app) {
        if (!app.isVisible()) {
            app.setVisible(true);
            desktop.add(app);
        }
        try { app.setSelected(true); } catch(Exception e){}
        desktop.moveToFront(app);
    }
    
    private void initAppWindows() {
        // POS Window (CardLayout inside)
        posWindow = new JInternalFrame("RestoPOS - Cashier Station", true, true, true, true);
        posWindow.setFrameIcon(null);
        posWindow.setSize(800, 600);
        posWindow.setLayout(new BorderLayout());
        
        // We reuse CardLayout logic for POS flow inside this window
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout); // Reuse this field for the POS internal flow
        
        // Init Panels
        onboarding = new OnboardingPanel(this);
        tableSelect = new TablePanel(this, orientation);
        menu = new MenuPanel(this, orientation);
        cart = new CartPanel(this, orientation);
        orderProcess = new OrderProcessPanel(this);
        checkoutPanel = new CheckoutPanel(this);
        registerPanel = new RegisterPanel(this); 
        
        cardPanel.add(onboarding, "ONBOARDING");
        cardPanel.add(tableSelect, "TABLE_SELECT");
        cardPanel.add(menu, "MENU");
        cardPanel.add(cart, "CART");
        cardPanel.add(orderProcess, "ORDER_PROCESS");
        cardPanel.add(checkoutPanel, "CHECKOUT");
        cardPanel.add(registerPanel, "REGISTER"); // Inside POS logic
        
        posWindow.add(cardPanel, BorderLayout.CENTER);
        
        // Admin Window
        adminWindow = new JInternalFrame("System Administration", true, true, true, true);
        admin = new AdminPanel(this);
        adminWindow.setContentPane(admin);
        adminWindow.setSize(800, 600);
        
        // Kitchen Window
        kitchenWindow = new JInternalFrame("Kitchen Display System", true, true, true, true);
        kitchen = new KitchenPanel(this);
        kitchenWindow.setContentPane(kitchen);
        kitchenWindow.setSize(600, 400);
        
        // Monitor Window
        monitorWindow = new JInternalFrame("Customer Monitor", true, true, true, true);
        monitorPanel = new CustomerMonitorPanel(this);
        monitorWindow.setContentPane(monitorPanel);
        monitorWindow.setSize(800, 600);
    }

    private void initTaskbar(JPanel parent) {
        // --- GNOME/Mac Style Top Bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 30));
        topBar.setBackground(new Color(0, 0, 0, 200)); // Dark Translucent
        
        // Custom Paint for Top Bar
        topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setComposite(AlphaComposite.SrcOver);
                g2.setColor(new Color(10, 10, 10, 220));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom Line
                g2.setColor(new Color(255, 255, 255, 50));
                g2.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        topBar.setPreferredSize(new Dimension(getWidth(), 28));
        
        // "Activities" / Start
        JButton activitiesBtn = new JButton(" Activities ");
        activitiesBtn.setForeground(Color.WHITE);
        activitiesBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        activitiesBtn.setBorderPainted(false);
        activitiesBtn.setContentAreaFilled(false);
        activitiesBtn.setFocusPainted(false);
        activitiesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Show "Expose" or Menu
        JPopupMenu startMenu = new JPopupMenu();
        startMenu.setBackground(new Color(30, 30, 30));
        startMenu.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        
        addMenuItem(startMenu, "Tile Windows", e -> tileWindows());
        startMenu.addSeparator();
        addMenuItem(startMenu, "Log Off", e -> {
            desktop.removeAll(); desktop.repaint();
            com.kiloux.restopos.service.UserService.getInstance().logout();
            showLoginWindow();
        });
        addMenuItem(startMenu, "Shut Down", e -> System.exit(0));
        
        activitiesBtn.addActionListener(e -> startMenu.show(activitiesBtn, 0, activitiesBtn.getHeight()));
        
        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftBox.setOpaque(false);
        leftBox.add(activitiesBtn);
        
        // Center Clock
        JLabel clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clock.setHorizontalAlignment(SwingConstants.CENTER);
        new Timer(1000, e -> clock.setText(java.time.format.DateTimeFormatter.ofPattern("MMM d  HH:mm").format(java.time.LocalDateTime.now()))).start();
        
        JPanel centerBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerBox.setOpaque(false);
        centerBox.add(clock);

        // Right Tray
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBox.setOpaque(false);
        JLabel userLbl = new JLabel("Guest");
        if (com.kiloux.restopos.service.UserService.getInstance().isLoggedIn()) {
             userLbl.setText(com.kiloux.restopos.service.UserService.getInstance().getCurrentUser());
        }
        userLbl.setForeground(Color.WHITE);
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        rightBox.add(userLbl);
        
        topBar.add(leftBox, BorderLayout.WEST);
        topBar.add(centerBox, BorderLayout.CENTER);
        topBar.add(rightBox, BorderLayout.EAST);
        
        parent.add(topBar, BorderLayout.NORTH);
        
        // --- Floating Dock (South) ---
        JPanel dockWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Centers the dock
        dockWrapper.setOpaque(false);
        
        com.kiloux.restopos.ui.AeroPanel dock = new com.kiloux.restopos.ui.AeroPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        dock.setTintColor(new Color(255, 255, 255, 40));
        dock.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Dock Icons
        createDockIcon(dock, "POS", e -> openApp(posWindow));
        createDockIcon(dock, "Admin", e -> openApp(adminWindow));
        createDockIcon(dock, "KDS", e -> openApp(kitchenWindow));
        createDockIcon(dock, "Media", e -> {
            MediaPlayerApp app = new MediaPlayerApp(); showApp(app);
        });
        createDockIcon(dock, "Note", e -> {
            NotepadApp app = new NotepadApp(); showApp(app);
        });
        createDockIcon(dock, "Calc", e -> {
            CalculatorApp app = new CalculatorApp(); showApp(app);
        });
        
        dockWrapper.add(dock);
        parent.add(dockWrapper, BorderLayout.SOUTH);
    }
    
    // Helper to launch new instance apps
    private void showApp(JInternalFrame app) {
        desktop.add(app);
        app.setVisible(true);
        app.setLocation(100 + (int)(Math.random()*50), 100);
        desktop.moveToFront(app);
    }

    private void createDockIcon(JPanel parent, String name, ActionListener action) {
        JButton btn = new JButton(name);
        btn.setPreferredSize(new Dimension(55, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btn.setForeground(Color.WHITE);
        // Glassy Button Look
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2, true));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1, true),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
            }
        });
        btn.addActionListener(action);
        parent.add(btn);
    }
    
    private void tileWindows() {
        JInternalFrame[] frames = desktop.getAllFrames();
        if (frames.length == 0) return;
        int cols = (int) Math.sqrt(frames.length);
        int rows = (int) Math.ceil((double)frames.length / cols);
        int w = desktop.getWidth() / cols;
        int h = desktop.getHeight() / rows;
        for (int i = 0; i < frames.length; i++) {
             int r = i / cols;
             int c = i % cols;
             frames[i].reshape(c*w, r*h, w, h);
        }
    }
    
    private void addMenuItem(JPopupMenu menu, String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.addActionListener(action);
        menu.add(item);
    }
    
    // Global F-Keys
    private void initGlobalKeys() {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    if (JOptionPane.showConfirmDialog(this, "Shutdown System?", "Win98 Exec", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_F12) {
                    openApp(kitchenWindow);
                    try { kitchenWindow.setMaximum(true); } catch(Exception ex){}
                    return true;
                } else if (e.getKeyCode() == KeyEvent.VK_F10) {
                     openApp(monitorWindow);
                     try { monitorWindow.setMaximum(true); } catch(Exception ex){}
                     return true;
                }
            }
            return false;
        });
    }

    // Keep this for POS Flow Compatibility (LoginPanel calls showCard("ONBOARDING") etc)
    public void showCard(String name) {
        // Map legacy strings to actions
        if ("ONBOARDING".equals(name) || "ADMIN".equals(name) || "MONITOR".equals(name)) {
             // This is usually called by LoginPanel.performLogin()
             // So we should divert to logic
             String role = com.kiloux.restopos.service.UserService.getInstance().getCurrentRole();
             unlockEnvironment(role);
        } else if ("LOGIN".equals(name)) {
            // Legacy logout
            desktop.removeAll();
            desktop.repaint();
            showLoginWindow();
        } else {
            // It's a flow change INSIDE the POS window (which uses cardLayout)
            if (cardLayout != null) {
                cardLayout.show(cardPanel, name);
                 // Refresh if needed
                for (Component comp : cardPanel.getComponents()) {
                    if (comp.isVisible() && comp instanceof CartPanel) {
                        ((CartPanel) comp).refreshCart();
                    }
                }
            }
        }
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
        // Re-layout or notify children
        if (menu != null) menu.applyOrientation(newOrientation);
        if (tableSelect != null) tableSelect.applyOrientation(newOrientation);
        if (cart != null) cart.applyOrientation(newOrientation);
        
        revalidate();
        repaint();
    }
    


    public OrderProcessPanel getOrderProcessPanel() { return orderProcess; }
    public CheckoutPanel getCheckoutPanel() { return checkoutPanel; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
