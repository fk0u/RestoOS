package com.kiloux.restopos.dao;

import com.kiloux.restopos.model.Table;
import com.kiloux.restopos.utils.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    
    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables ORDER BY table_number";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Table table = new Table(
                    rs.getInt("id"),
                    rs.getInt("table_number"),
                    rs.getInt("capacity"),
                    rs.getString("status")
                );
                table.setxPos(rs.getInt("x_pos"));
                table.setyPos(rs.getInt("y_pos"));
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
    
    public void updateStatus(int tableId, String status) {
        String sql = "UPDATE tables SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, tableId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
