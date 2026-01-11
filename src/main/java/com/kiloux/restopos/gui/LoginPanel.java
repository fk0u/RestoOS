package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.service.UserService;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class LoginPanel extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField userField;
    private JPasswordField passField;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        
        setLayout(new GridBagLayout()); // Centered
        setOpaque(false); // Let Desktop gradient show through if frame is transparent, mostly frame content
        
        // Main Glass Box
        com.kiloux.restopos.ui.AeroPanel window = new com.kiloux.restopos.ui.AeroPanel(new BorderLayout(15, 15));
        window.setPreferredSize(new Dimension(380, 240));
        window.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        title.setForeground(new Color(30, 50, 80));
        window.add(title, BorderLayout.NORTH);
        
        // Form
        JPanel form = new JPanel(new GridLayout(4, 1, 5, 5));
        form.setOpaque(false);
        
        form.add(new JLabel("Username:"));
        userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(userField);
        
        form.add(new JLabel("Password:"));
        passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        form.add(passField);
        
        window.add(form, BorderLayout.CENTER);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        
        RetroButton loginBtn = new RetroButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginBtn.setPreferredSize(new Dimension(100, 30));
        
        RetroButton regBtn = new RetroButton("Register"); 
        regBtn.setPreferredSize(new Dimension(80, 30));

        loginBtn.addActionListener(e -> performLogin());
        regBtn.addActionListener(e -> mainFrame.showCard("REGISTER"));

        btnPanel.add(regBtn);
        btnPanel.add(loginBtn);
        
        window.add(btnPanel, BorderLayout.SOUTH);
        
        add(window);
    }
    
    // ... performLogin (unchanged)
    private void performLogin() {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        
        UserService userService = UserService.getInstance();
        if (userService.login(u, p)) {
            String role = userService.getCurrentRole();
            if ("CLIENT".equalsIgnoreCase(role)) {
                mainFrame.showCard("MONITOR");
            } else if ("ADMIN".equalsIgnoreCase(role)) {
                mainFrame.showCard("ADMIN");
            } else {
                // Cashier or Staff
                mainFrame.showCard("ONBOARDING");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
