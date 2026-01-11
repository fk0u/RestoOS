package com.kiloux.restopos.gui;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.service.UserService;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class RegisterPanel extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField confirmField;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        
        setLayout(new GridBagLayout()); 
        setBackground(UIConfig.BACKGROUND_COLOR);
        
        JPanel window = new JPanel(new BorderLayout());
        window.setPreferredSize(new Dimension(350, 260));
        window.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        window.setBackground(UIConfig.WIN98_GREY);
        
        JPanel titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleBar.setBackground(UIConfig.WIN98_TITLE_ACTIVE_START);
        JLabel title = new JLabel("New User Registration");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 11));
        titleBar.add(title);
        window.add(titleBar, BorderLayout.NORTH);
        
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; 
        userField = new JTextField(15);
        userField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        form.add(userField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        passField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        form.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Confirm Pass:"), gbc);
        gbc.gridx = 1;
        confirmField = new JPasswordField(15);
        confirmField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        form.add(confirmField, gbc);
        
        window.add(form, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        
        RetroButton cancelBtn = new RetroButton("Cancel");
        RetroButton okBtn = new RetroButton("Create");
        
        cancelBtn.addActionListener(e -> mainFrame.showCard("LOGIN"));
        okBtn.addActionListener(e -> register());
        
        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);
        
        window.add(btnPanel, BorderLayout.SOUTH);
        
        add(window);
    }
    
    private void register() {
         String u = userField.getText();
         String p = new String(passField.getPassword());
         String c = new String(confirmField.getPassword());
         
         if (!p.equals(c)) {
             JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }
         
         if(UserService.getInstance().register(u, p)) {
             JOptionPane.showMessageDialog(this, "User Created!", "Success", JOptionPane.INFORMATION_MESSAGE);
             mainFrame.showCard("LOGIN");
         } else {
             JOptionPane.showMessageDialog(this, "Registration Failed (User exists?)", "Error", JOptionPane.ERROR_MESSAGE);
         }
    }
}
