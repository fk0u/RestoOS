package com.kiloux.restopos.service;

import com.kiloux.restopos.utils.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    
    private static UserService instance;
    private String currentUser;
    private String currentRole;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Should be hashed in prod
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                this.currentUser = username;
                this.currentRole = rs.getString("role");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password) {
         String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'STAFF')"; // Default to STAFF
         try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        currentUser = null;
        currentRole = null;
    }
    
    public java.util.List<Object[]> getAllUsers() {
        java.util.List<Object[]> users = new java.util.ArrayList<>();
        String query = "SELECT id, username, role FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while(rs.next()) {
                users.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public String getCurrentUser() { return currentUser; }
    public String getCurrentRole() { return currentRole; }
    public boolean isLoggedIn() { return currentUser != null; }
}
