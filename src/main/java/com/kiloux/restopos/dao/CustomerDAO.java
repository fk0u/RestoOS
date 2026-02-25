package com.kiloux.restopos.dao;

import com.kiloux.restopos.model.Customer;
import com.kiloux.restopos.utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public Customer getCustomer(String phoneOrName) {
        Customer c = null;
        String sql = "SELECT * FROM customers WHERE phone = ? OR name LIKE ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phoneOrName);
            pstmt.setString(2, "%" + phoneOrName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setPhone(rs.getString("phone"));
                    c.setMembershipTier(rs.getString("membership_tier"));
                    c.setPoints(rs.getInt("points"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public List<Customer> searchCustomers(String query) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? OR phone LIKE ? ORDER BY name";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String q = "%" + query + "%";
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setPhone(rs.getString("phone"));
                    c.setMembershipTier(rs.getString("membership_tier"));
                    c.setPoints(rs.getInt("points"));
                    list.add(c);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setMembershipTier(rs.getString("membership_tier"));
                c.setPoints(rs.getInt("points"));
                list.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean addCustomer(Customer c) {
        String sql = "INSERT INTO customers (name, phone, membership_tier, points) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setString(2, c.getPhone());
            pstmt.setString(3, c.getMembershipTier());
            pstmt.setInt(4, c.getPoints());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void addPoints(int customerId, int pointsToAdd) {
        String sql = "UPDATE customers SET points = points + ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pointsToAdd);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public String getMemberTier(String phoneOrName) {
        Customer c = getCustomer(phoneOrName);
        return c != null ? c.getMembershipTier() : null;
    }
}
