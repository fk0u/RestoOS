package com.kiloux.restopos.dao;

import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.Order;
import com.kiloux.restopos.utils.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order, List<CartItem> items) {
        int orderId = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Transaction

            // 1. Get Next Queue Number
            int nextQueue = getNextQueueNumber(conn);
            order.setQueueNumber(nextQueue);

            // 2. Insert Order
            String sqlVal = "INSERT INTO orders (table_id, order_type, status, total_amount, payment_status, customer_name, queue_number, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlVal, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getTableId());
            pstmt.setString(2, order.getOrderType());
            pstmt.setString(3, order.getStatus());
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setString(5, order.getPaymentStatus());
            pstmt.setString(6, order.getCustomerName());
            pstmt.setInt(7, nextQueue);
            pstmt.setObject(8, LocalDateTime.now()); // Simple TS

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                    order.setId(orderId);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
            pstmt.close();

            // 3. Insert Order Items
            String sqlItem = "INSERT INTO order_items (order_id, menu_item_id, quantity, subtotal, notes) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlItem);

            for (CartItem item : items) {
                pstmt.setInt(1, orderId);
                pstmt.setInt(2, item.getMenuItem().getId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setDouble(4, item.getSubtotal());
                pstmt.setString(5, item.getNotes());
                pstmt.addBatch();
            }
            pstmt.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return -1;
        } finally {
             try { if(pstmt!=null) pstmt.close(); if(conn!=null) conn.close(); } catch(Exception e){}
        }
        return orderId;
    }
    
    // Logic: Count orders today + 1. Simple but enough for prototype.
    private int getNextQueueNumber(Connection conn) throws SQLException {
        // In real SQL apps, maybe reset a sequence or query max(queue_number) where date(created_at) = date('now')
        // SQLite: SELECT MAX(queue_number) FROM orders WHERE date(created_at) = date('now')
        // Java handling of date might be safer
        String sql = "SELECT MAX(queue_number) FROM orders WHERE created_at >= date('now')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }
    
    public void updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status IN ('WAITING', 'COOKING') ORDER BY created_at ASC";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setTableId(rs.getInt("table_id")); // Can use 0 for takeaway
                o.setStatus(rs.getString("status"));
                o.setQueueNumber(rs.getInt("queue_number"));
                o.setCustomerName(rs.getString("customer_name"));
                // Timestamp logic simplified
                o.setCreatedAt(LocalDateTime.now()); // Parse if needed, but relative time calc needs logic
                
                // Fetch Items
                o.setItems(getOrderItems(conn, o.getId()));
                
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    private List<CartItem> getOrderItems(Connection conn, int orderId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT oi.quantity, oi.notes, m.name FROM order_items oi " +
                     "JOIN menu_items m ON oi.menu_item_id = m.id " +
                     "WHERE oi.order_id = ?";
        try (PreparedStatement adminPstmt = conn.prepareStatement(sql)) {
             adminPstmt.setInt(1, orderId);
             try (ResultSet rs = adminPstmt.executeQuery()) {
                 while (rs.next()) {
                      // Construct simplified MenuItem for display
                      com.kiloux.restopos.model.MenuItem mi = new com.kiloux.restopos.model.MenuItem();
                      mi.setName(rs.getString("name"));
                      
                      CartItem ci = new CartItem(mi, rs.getInt("quantity"));
                      ci.setNotes(rs.getString("notes"));
                      items.add(ci);
                 }
             }
        } catch (SQLException e) {
             e.printStackTrace();
        }
        return items;
    }
}
