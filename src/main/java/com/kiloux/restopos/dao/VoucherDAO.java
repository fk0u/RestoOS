package com.kiloux.restopos.dao;

import com.kiloux.restopos.utils.DatabaseManager;
import java.sql.*;

public class VoucherDAO {

    public double getDiscountValue(String code, double subtotal) {
        String sql = "SELECT discount_type, value FROM vouchers WHERE code = ? AND active = 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("discount_type"); // PERCENT, FLAT
                    double val = rs.getDouble("value");
                    
                    if ("PERCENT".equalsIgnoreCase(type)) {
                        return subtotal * (val / 100.0);
                    } else {
                        return val;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
