package com.kiloux.restopos.apps;

import com.kiloux.restopos.config.UIConfig;
import com.kiloux.restopos.dao.CustomerDAO;
import com.kiloux.restopos.model.Customer;
import com.kiloux.restopos.ui.IconAssets;
import com.kiloux.restopos.ui.RetroButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemberManagementApp extends JInternalFrame {
    
    private CustomerDAO customerDAO;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    
    // Form fields
    private JTextField nameField;
    private JTextField phoneField;
    private JComboBox<String> tierBox;
    
    public MemberManagementApp() {
        super("Member Center", true, true, true, true);
        customerDAO = new CustomerDAO();
        setFrameIcon(IconAssets.getFrameIcon("user")); // Reuse generic user icon
        setSize(800, 500);
        
        initComponents();
        refreshTable();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // --- Toolbar ---
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbar.setBackground(new Color(240, 240, 240));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel lblSearch = new JLabel("Search:");
        toolbar.add(lblSearch);
        
        JTextField searchField = new JTextField(15);
        toolbar.add(searchField);
        
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchMembers(searchField.getText()));
        toolbar.add(btnSearch);
        
        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });
        toolbar.add(btnRefresh);

        // Enter key to search
        searchField.addActionListener(e -> searchMembers(searchField.getText()));
        
        add(toolbar, BorderLayout.NORTH);
        
        // --- Split Pane ---
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(500);
        
        // Left: Table
        String[] cols = {"ID", "Name", "Phone", "Tier", "Points"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(25);
        memberTable.setShowGrid(true);
        memberTable.setGridColor(Color.LIGHT_GRAY);
        
        JScrollPane scroll = new JScrollPane(memberTable);
        split.setLeftComponent(scroll);
        
        // Right: Register Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        JLabel title = new JLabel("New Registration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));
        
        formPanel.add(createLabel("Full Name:"));
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(nameField);
        
        formPanel.add(createLabel("Phone Number:"));
        phoneField = new JTextField();
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(phoneField);
        
        formPanel.add(createLabel("Initial Tier:"));
        tierBox = new JComboBox<>(new String[]{"BRONZE", "SILVER", "GOLD"});
        tierBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        formPanel.add(tierBox);
        
        formPanel.add(Box.createVerticalStrut(20));
        
        RetroButton btnRegister = new RetroButton("Register Member");
        btnRegister.setBackground(UIConfig.PRIMARY_COLOR);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.addActionListener(e -> registerMember());
        formPanel.add(btnRegister);
        
        // Filler
        formPanel.add(Box.createVerticalGlue());
        
        split.setRightComponent(formPanel);
        
        add(split, BorderLayout.CENTER);
    }
    
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return l;
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Customer> list = customerDAO.getAllCustomers();
        for(Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getId(), c.getName(), c.getPhone(), c.getMembershipTier(), c.getPoints()
            });
        }
    }
    
    private void searchMembers(String query) {
        if (query == null || query.trim().isEmpty()) {
            refreshTable();
            return;
        }
        tableModel.setRowCount(0);
        List<Customer> list = customerDAO.searchCustomers(query.trim());
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No members found for: " + query, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            // Don't clear the empty table, let them see it's empty or refresh
        }
        for(Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getId(), c.getName(), c.getPhone(), c.getMembershipTier(), c.getPoints()
            });
        }
    }
    
    private void registerMember() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Customer c = new Customer();
        c.setName(name);
        c.setPhone(phone);
        c.setMembershipTier((String) tierBox.getSelectedItem());
        c.setPoints(0); // New members start with 0
        
        if (customerDAO.addCustomer(c)) {
            JOptionPane.showMessageDialog(this, "Member Registered Successfully!");
            nameField.setText("");
            phoneField.setText("");
            refreshTable();
        } else {
             JOptionPane.showMessageDialog(this, "Registration Failed. Phone/Name might be duplicate.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
