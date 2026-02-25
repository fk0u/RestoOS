package com.kiloux.restopos.gui;

import com.kiloux.restopos.apps.*;
import com.kiloux.restopos.config.DeviceOrientation;
import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.gui.forms.NbDesignerFrameForm;
import com.kiloux.restopos.gui.forms.SwingDesignerFrameForm;
import com.kiloux.restopos.ui.IconAssets;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private JInternalFrame memberWindow;

    // Immersive visual engine
    private Timer ambienceTimer;
    private float auroraShift = 0f;
    private float vignettePulse = 0f;
    private Point2D.Float[] ambientParticles;
    private float[] particleSpeed;
    private float[] particleSize;

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
        
        // 2. SHUTDOWN SCREEN
        contentPane.add(new ShutdownPanel(), "SHUTDOWN");
        
        // 2. DESKTOP ENVIRONMENT
        JPanel osPanel = new JPanel(new BorderLayout());
        osPanel.setBackground(UIConfig.BACKGROUND_COLOR); // Light base
        
        // Wallpaper Layer - AERO VISTA STYLE
        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();

                float oscillation = (float) ((Math.sin(auroraShift) + 1.0) * 0.5);
                int topB = 40 + (int) (50 * oscillation);
                int bottomB = 95 + (int) (45 * (1.0 - oscillation));
                
                // Base Gradient (Vista Teal/Green)
                GradientPaint base = new GradientPaint(0, 0, new Color(0, 35, topB), 0, h, new Color(0, bottomB, 120));
                g2.setPaint(base);
                g2.fillRect(0, 0, w, h);
                
                // Aurora Curves
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.setStroke(new BasicStroke(1));
                
                // Curve 1 (Cyan)
                GeneralPath curve1 = new GeneralPath();
                curve1.moveTo(0, h * 0.6);
                curve1.curveTo(w * 0.4, h * 0.3, w * 0.6, h * 0.8, w, h * 0.4);
                curve1.lineTo(w, h);
                curve1.lineTo(0, h);
                curve1.closePath();
                g2.setPaint(new GradientPaint(0, 0, new Color(100, 255, 255, 0), 0, h, new Color(100, 255, 255, 150)));
                g2.fill(curve1);
                
                // Curve 2 (Green)
                GeneralPath curve2 = new GeneralPath();
                curve2.moveTo(0, h * 0.8);
                curve2.curveTo(w * 0.3, h * 0.5, w * 0.7, h * 0.9, w, h * 0.6);
                curve2.lineTo(w, h);
                curve2.lineTo(0, h);
                curve2.closePath();
                g2.setPaint(new GradientPaint(0, h/2, new Color(100, 255, 100, 0), 0, h, new Color(50, 255, 100, 100)));
                g2.fill(curve2);

                if (UIConfig.isImmersiveMode() && ambientParticles != null) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
                    for (int i = 0; i < ambientParticles.length; i++) {
                        Point2D.Float point = ambientParticles[i];
                        float size = particleSize[i];
                        GradientPaint orb = new GradientPaint(
                            point.x,
                            point.y,
                            new Color(130, 255, 220, 130),
                            point.x,
                            point.y + size,
                            new Color(130, 255, 220, 0)
                        );
                        g2.setPaint(orb);
                        g2.fill(new Ellipse2D.Float(point.x, point.y, size, size));
                    }
                }

                if (UIConfig.isImmersiveMode()) {
                    float vignetteStrength = 0.20f + (float) (0.08f * Math.sin(vignettePulse));
                    RadialGradientPaint vignette = new RadialGradientPaint(
                        new Point2D.Float(w / 2f, h / 2f),
                        Math.max(w, h) * 0.75f,
                        new float[]{0.55f, 1.0f},
                        new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, (int) (255 * vignetteStrength))}
                    );
                    g2.setComposite(AlphaComposite.SrcOver);
                    g2.setPaint(vignette);
                    g2.fillRect(0, 0, w, h);
                }
                
                g2.setComposite(AlphaComposite.SrcOver);
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

        // Start immersive desktop ambience
        initImmersiveAmbience();
        
        // Start Global Key Listeners
        initGlobalKeys();
    }
    
    private void showLoginWindow() {
        // Create Login as internal frame
        JInternalFrame loginFrame = new JInternalFrame("Welcome to RestoOS", false, false, false, false);
        loginFrame.setFrameIcon(IconAssets.getFrameIcon("user"));
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

        if (UIConfig.isImmersiveMode()) {
            playCinematicReveal("RestoOS Ready - " + role.toUpperCase());
        }
        
        // Launch Welcome Center
        WelcomeCenterApp welcome = new WelcomeCenterApp();
        showApp(welcome);

        // Open specific windows based on role
        if ("CLIENT".equalsIgnoreCase(role)) {
            openApp(monitorWindow);
            try { monitorWindow.setMaximum(true); } catch(Exception e){}
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            openApp(adminWindow);
            openApp(memberWindow);
            try { memberWindow.setMaximum(false); memberWindow.setSize(900, 600); } catch(Exception e){}
            try { adminWindow.setMaximum(true); } catch(Exception e){}
        } else {
            // Cashier - Full POS
            openApp(posWindow);
             try { posWindow.setMaximum(true); } catch(Exception e){}
        }
    }
    
    public void openKitchen() {
        openApp(kitchenWindow);
        try { kitchenWindow.setMaximum(true); } catch(Exception e){}
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
        posWindow.setFrameIcon(IconAssets.getFrameIcon("pos"));
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
        adminWindow.setFrameIcon(IconAssets.getFrameIcon("admin"));
        admin = new AdminPanel(this);
        adminWindow.setContentPane(admin);
        adminWindow.setSize(800, 600);
        
        // Kitchen Window
        kitchenWindow = new JInternalFrame("Kitchen Display System", true, true, true, true);
        kitchenWindow.setFrameIcon(IconAssets.getFrameIcon("kitchen"));
        kitchen = new KitchenPanel(this);
        kitchenWindow.setContentPane(kitchen);
        kitchenWindow.setSize(600, 400);
        
        // Monitor Window
        monitorWindow = new JInternalFrame("Customer Monitor", true, true, true, true);
        monitorWindow.setFrameIcon(IconAssets.getFrameIcon("monitor"));
        monitorPanel = new CustomerMonitorPanel(this);
        monitorWindow.setContentPane(monitorPanel);
        monitorWindow.setSize(800, 600);
        
        // Member Window
        memberWindow = new com.kiloux.restopos.apps.MemberManagementApp();
    }

    private void initTaskbar(JPanel parent) {
        // --- Windows Vista Taskbar ---
        JPanel taskbarPanel = new JPanel(new BorderLayout());
        taskbarPanel.setPreferredSize(new Dimension(getWidth(), 40));
        taskbarPanel.setBackground(UIConfig.AERO_TASKBAR);
        
        // Vista Glossy Paint
        JPanel taskbarBg = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Dark Glass Body + Blur Tint
                g2.setColor(UIConfig.AERO_TASKBAR);
                g2.fillRect(0, 0, w, h);
                
                // Top Highlight (Glass Edge)
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 120), 0, 1, new Color(255, 255, 255, 0)));
                g2.fillRect(0, 0, w, 1);
                
                // Bottom Reflection (Gloss)
                g2.setPaint(new GradientPaint(0, h/2, new Color(255, 255, 255, 0), 0, h, new Color(255, 255, 255, 30)));
                g2.fillRect(0, h/2, w, h/2);
            }
        };
        taskbarBg.setOpaque(false);
        
        // --- START ORB ---
        JButton startOrb = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int s = Math.min(getWidth(), getHeight()) - 4; // slight padding
                int x = (getWidth() - s) / 2;
                int y = (getHeight() - s) / 2;
                
                // Outer Glow ring
                if (getModel().isRollover()) {
                    g2.setPaint(new RadialGradientPaint(
                        new Point2D.Float(getWidth()/2, getHeight()/2), s/2 + 4,
                        new float[]{0.6f, 1.0f},
                        new Color[]{new Color(100, 220, 255, 100), new Color(0, 0, 0, 0)}
                    ));
                    g2.fillOval(x-4, y-4, s+8, s+8);
                }
                
                // Orb Body (Dark Blue Gradient)
                RadialGradientPaint kp = new RadialGradientPaint(
                    new Point2D.Float(getWidth()/2, getHeight()/2), s/2,
                    new float[]{ 0.3f, 1.0f},
                    new Color[]{new Color(10, 60, 90), new Color(0, 20, 30)}
                );
                g2.setPaint(kp);
                g2.fillOval(x, y, s, s);
                
                // Glass Shine
                GradientPaint shine = new GradientPaint(0, y, new Color(255, 255, 255, 180), 0, y + s/2, new Color(255, 255, 255, 0));
                g2.setPaint(shine);
                g2.fillOval(x, y, s, s/2);
                
                // Logo (Simplified Windows Flag - 4 color squares)
                int logoS = s / 2;
                int lx = (getWidth() - logoS) / 2;
                int ly = (getHeight() - logoS) / 2;
                
                g2.setColor(new Color(242, 80, 34)); // Red
                g2.fillRect(lx, ly, logoS/2 -1, logoS/2 -1);
                g2.setColor(new Color(127, 186, 0)); // Green
                g2.fillRect(lx + logoS/2, ly, logoS/2, logoS/2 -1);
                g2.setColor(new Color(0, 164, 239)); // Blue
                g2.fillRect(lx, ly + logoS/2, logoS/2 -1, logoS/2);
                g2.setColor(new Color(255, 185, 0)); // Yellow
                g2.fillRect(lx + logoS/2, ly + logoS/2, logoS/2, logoS/2);
            }
        };
        startOrb.setPreferredSize(new Dimension(50, 40));
        startOrb.setBorderPainted(false);
        startOrb.setContentAreaFilled(false);
        startOrb.setFocusPainted(false);
        startOrb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Windows Vista Start Menu
        final JPopupMenu startMenu = createVistaStartMenu();
        startOrb.addActionListener(e -> startMenu.show(startOrb, 0, -startMenu.getPreferredSize().height + 10));
        
        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftBox.setOpaque(false);
        leftBox.add(startOrb);
        
        // Quick Launch (Icons)
        JPanel quickLaunch = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 8));
        quickLaunch.setOpaque(false);
        quickLaunch.add(createTaskbarButton("POS", "pos", e -> openApp(posWindow)));
        quickLaunch.add(createTaskbarButton("Web", "browser", e -> { /* browser */ }));
        
        leftBox.add(quickLaunch);

        // Right Tray
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightBox.setOpaque(false);

        JLabel statusLabel = new JLabel("\u25CF SYSTEM ONLINE");
        statusLabel.setForeground(new Color(70, 255, 170));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        JLabel clock = new JLabel();
        clock.setForeground(Color.WHITE);
        clock.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel dateLabel = new JLabel();
        dateLabel.setForeground(new Color(200, 210, 222));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");
        new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            clock.setText(timeFormatter.format(now));
            dateLabel.setText(dateFormatter.format(now));
        }).start();
        
        rightBox.add(statusLabel);
        rightBox.add(dateLabel);
        rightBox.add(clock);
        
        taskbarBg.add(leftBox, BorderLayout.WEST);
        taskbarBg.add(rightBox, BorderLayout.EAST);
        taskbarPanel.add(taskbarBg, BorderLayout.CENTER);
        
        parent.add(taskbarPanel, BorderLayout.SOUTH);
    }
    
    private JPopupMenu createVistaStartMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.setBorder(BorderFactory.createEmptyBorder());
        menu.setBackground(new Color(0,0,0,0));
        menu.setOpaque(false);
        
        // Main Frame (Aero Glass Style)
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glass Background
                g2.setPaint(new GradientPaint(0, 0, new Color(40, 60, 75, 240), 0, getHeight(), new Color(20, 30, 40, 250)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                // Inner Border
                g2.setColor(new Color(255, 255, 255, 60));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 8, 8);
                // Outer Rim
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6)); // Glass Padding
        
        JPanel contentGrid = new JPanel(new GridLayout(1, 2));
        contentGrid.setOpaque(false);
        
        // --- LEFT PANE (White) ---
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.setBackground(Color.WHITE);
        leftPane.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120)));
        
        // Programs
        JPanel progList = new JPanel();
        progList.setLayout(new javax.swing.BoxLayout(progList, javax.swing.BoxLayout.Y_AXIS));
        progList.setBackground(Color.WHITE);
        progList.setBorder(BorderFactory.createEmptyBorder(8, 2, 2, 2));
        
        progList.add(createStartMenuItem("Internet Explorer", "browser", e -> {}));
        progList.add(createStartMenuItem("E-mail", "mail", e -> {}));
        progList.add(Box.createVerticalStrut(10));
        progList.add(createStartMenuItem("RestoPOS Terminal", "pos", e -> openApp(posWindow)));
        progList.add(createStartMenuItem("Settings", "settings", e -> showApp(new SettingsApp(this))));
        progList.add(createStartMenuItem("Notepad", "notepad", e -> showApp(new NotepadApp())));
        progList.add(createStartMenuItem("Command Prompt", "terminal", e -> showApp(new TerminalApp())));
        progList.add(createStartMenuItem("Swing Designer", "designer", e -> showApp(new DragDropDesignerApp())));
        progList.add(createStartMenuItem("Swing Designer Frame", "designer-frame", e -> SwingDesignerFrameForm.open()));
        progList.add(createStartMenuItem("NetBeans GUI Form", "netbeans", e -> NbDesignerFrameForm.open()));
        
        leftPane.add(progList, BorderLayout.CENTER);
        
        // Search & All Programs
        JPanel searchArea = new JPanel(new BorderLayout(0, 5));
        searchArea.setBackground(Color.WHITE);
        searchArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, new Color(220,225,230)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        JButton allProgs = new JButton("All Programs  \u25B6");
        allProgs.setHorizontalAlignment(SwingConstants.LEFT);
        allProgs.setContentAreaFilled(false);
        allProgs.setBorderPainted(false);
        allProgs.setFont(new Font("Segoe UI", Font.BOLD, 11));
        allProgs.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JTextField searchBar = new JTextField("Start Search");
        searchBar.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        searchBar.setForeground(Color.GRAY);
        searchBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 190, 200)),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        
        searchArea.add(allProgs, BorderLayout.NORTH);
        searchArea.add(searchBar, BorderLayout.CENTER);
        
        leftPane.add(searchArea, BorderLayout.SOUTH);
        
        // --- RIGHT PANE (Darker/System) ---
        JPanel rightPane = new JPanel(new BorderLayout());
        rightPane.setBackground(new Color(230, 240, 250)); // Vista light blue-grey
        rightPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, new Color(120, 120, 120)));
        
        // User Pic
        JPanel userBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        userBox.setOpaque(false);
        
        JLabel userPic = new JLabel() {
             @Override
             protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g;
                 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                 // Frame
                 g2.setColor(new Color(240, 240, 240));
                 g2.fillRoundRect(0, 0, 50, 50, 8, 8);
                 g2.setColor(new Color(180, 180, 180));
                 g2.drawRoundRect(0, 0, 49, 49, 8, 8);
                 // Avatar placeholder
                 g2.setColor(new Color(255, 140, 0)); // Orange flower?
                 g2.fillOval(10, 10, 30, 30);
             }
        };
        userPic.setPreferredSize(new Dimension(50, 50));
        
        JLabel userName = new JLabel("User");
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setForeground(new Color(50, 70, 90));
        
        userBox.add(userName); // Name next to pic or not? Usually name is a button. Just Pic is fine.
        userBox.add(userPic);
        
        JPanel linkList = new JPanel();
        linkList.setLayout(new javax.swing.BoxLayout(linkList, javax.swing.BoxLayout.Y_AXIS));
        linkList.setOpaque(false);
        linkList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        linkList.add(createSysLink("Documents", "explorer", true, e -> showApp(new ExplorerApp())));
        linkList.add(createSysLink("Pictures", "explorer", false, e -> {}));
        linkList.add(createSysLink("Music", "explorer", false, e -> {}));
        linkList.add(Box.createVerticalStrut(10));
        linkList.add(createSysLink("Computer", "explorer", true, e -> showApp(new ExplorerApp())));
        linkList.add(createSysLink("Control Panel", "control", true, e -> showApp(new ControlPanelApp(desktop))));
        linkList.add(Box.createVerticalStrut(15));
        linkList.add(createSysLink("Help and Support", "settings", false, e -> {}));

        rightPane.add(userBox, BorderLayout.NORTH);
        rightPane.add(linkList, BorderLayout.CENTER);
        
        // Power Buttons (Bottom Right)
        JPanel powerBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        powerBox.setOpaque(false);
        
        JButton lockBtn = new JButton("\uD83D\uDD12"); // Lock icon
        lockBtn.setPreferredSize(new Dimension(30, 30));
        lockBtn.setToolTipText("Lock");
        
        JButton powerBtn = new JButton("\u23FB"); // Power icon
        powerBtn.setBackground(new Color(200, 50, 50));
        powerBtn.setForeground(Color.WHITE);
        powerBtn.setPreferredSize(new Dimension(30, 30));
        powerBtn.setToolTipText("Shut Down");
        powerBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "SHUTDOWN");
             for(Component c : getContentPane().getComponents()) {
                if(c instanceof ShutdownPanel) ((ShutdownPanel)c).startShutdown();
            }
        });
        
        powerBox.add(lockBtn);
        powerBox.add(powerBtn);
        
        rightPane.add(powerBox, BorderLayout.SOUTH);
        
        contentGrid.add(leftPane);
        contentGrid.add(rightPane);
        mainPanel.add(contentGrid, BorderLayout.CENTER);
        
        menu.add(mainPanel);
        menu.setPreferredSize(new Dimension(500, 520));
        return menu;
    }
    
    private JButton createStartMenuItem(String text, String iconKey, ActionListener a) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setForeground(Color.BLACK);
        b.setIcon(IconAssets.getMenuIcon(iconKey));
        b.setIconTextGap(10);
        b.addActionListener(a);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setContentAreaFilled(true); b.setBackground(new Color(225, 240, 255)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setContentAreaFilled(false); }
        });
        b.setMaximumSize(new Dimension(280, 40));
        return b;
    }

    private JButton createSysLink(String text, String iconKey, boolean bold, ActionListener a) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setForeground(new Color(50, 60, 70));
        b.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 12));
        b.setIcon(IconAssets.getFrameIcon(iconKey));
        b.setIconTextGap(8);
        b.addActionListener(a);
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setContentAreaFilled(true); b.setBackground(new Color(200, 230, 250)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setContentAreaFilled(false); }
        });
        b.setMaximumSize(new Dimension(280, 28));
        return b;
    }
    
    // Helper to launch new instance apps
    private void showApp(JInternalFrame app) {
        desktop.add(app);
        app.setVisible(true);
        app.setLocation(100 + (int)(Math.random()*50), 100);
        desktop.moveToFront(app);
    }

    private JButton createTaskbarButton(String name, String iconKey, ActionListener action) {
        JButton btn = new JButton(name);
        btn.setPreferredSize(new Dimension(40, 24));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btn.setForeground(Color.WHITE);
        // Glassy Button Look
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setIcon(IconAssets.getTaskbarIcon(iconKey));
        btn.setIconTextGap(4);
        btn.addActionListener(action);
        return btn;
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

    private void initImmersiveAmbience() {
        int particleCount = 44;
        ambientParticles = new Point2D.Float[particleCount];
        particleSpeed = new float[particleCount];
        particleSize = new float[particleCount];

        for (int i = 0; i < particleCount; i++) {
            ambientParticles[i] = new Point2D.Float((float) (Math.random() * 1800), (float) (Math.random() * 900));
            particleSpeed[i] = 0.2f + (float) (Math.random() * 0.9f);
            particleSize[i] = 2.5f + (float) (Math.random() * 6.5f);
        }

        ambienceTimer = new Timer(40, e -> {
            if (!UIConfig.isImmersiveMode()) {
                return;
            }

            auroraShift += 0.015f;
            vignettePulse += 0.02f;

            int w = Math.max(1, desktop.getWidth());
            int h = Math.max(1, desktop.getHeight());

            for (int i = 0; i < ambientParticles.length; i++) {
                Point2D.Float point = ambientParticles[i];
                point.y += particleSpeed[i];
                point.x += (float) Math.sin((auroraShift * 0.9f) + (i * 0.13f)) * 0.28f;

                if (point.y > h + 10) {
                    point.y = -10;
                    point.x = (float) (Math.random() * w);
                }
            }

            desktop.repaint();
        });
        ambienceTimer.start();
    }

    private void playCinematicReveal(String message) {
        JComponent overlay = new JComponent() {
            private float alpha = 0f;
            private boolean fadingIn = true;

            {
                Timer timer = new Timer(24, e -> {
                    if (fadingIn) {
                        alpha += 0.06f;
                        if (alpha >= 0.82f) {
                            alpha = 0.82f;
                            fadingIn = false;
                        }
                    } else {
                        alpha -= 0.04f;
                        if (alpha <= 0f) {
                            ((Timer) e.getSource()).stop();
                            desktop.remove(this);
                            desktop.repaint();
                        }
                    }
                    repaint();
                });
                timer.start();
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.60f));
                g2.setPaint(new GradientPaint(0, 0, new Color(2, 12, 20), 0, h, new Color(0, 38, 48)));
                g2.fillRect(0, 0, w, h);

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(new Color(180, 255, 240));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
                FontMetrics fm = g2.getFontMetrics();
                int textX = (w - fm.stringWidth(message)) / 2;
                int textY = (h / 2) - 8;
                g2.drawString(message, textX, textY);

                g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2.setColor(new Color(220, 255, 245));
                String sub = "Immersive Environment Loaded";
                int subX = (w - g2.getFontMetrics().stringWidth(sub)) / 2;
                g2.drawString(sub, subX, textY + 30);

                g2.dispose();
            }
        };

        overlay.setOpaque(false);
        overlay.setBounds(0, 0, desktop.getWidth(), desktop.getHeight());
        desktop.add(overlay, JLayeredPane.DRAG_LAYER);
        desktop.moveToFront(overlay);
    }

    public void applyVisualProfile(boolean immersive) {
        UIConfig.setImmersiveMode(immersive);

        if (immersive) {
            if (ambienceTimer == null || !ambienceTimer.isRunning()) {
                initImmersiveAmbience();
            }
        } else if (ambienceTimer != null && ambienceTimer.isRunning()) {
            ambienceTimer.stop();
        }

        if (desktop != null) {
            desktop.repaint();
        }
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
