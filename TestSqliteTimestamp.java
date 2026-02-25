import java.sql.*;
import java.time.LocalDateTime;

public class TestSqliteTimestamp {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS test_orders (id INTEGER PRIMARY KEY, created_at TIMESTAMP)");
        
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO test_orders (created_at) VALUES (?)");
        pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        pstmt.executeUpdate();
        
        ResultSet rs = stmt.executeQuery("SELECT created_at FROM test_orders");
        while (rs.next()) {
            try {
                System.out.println("Timestamp: " + rs.getTimestamp("created_at"));
            } catch (Exception e) {
                System.out.println("Error reading timestamp: " + e.getMessage());
            }
            try {
                System.out.println("String: " + rs.getString("created_at"));
            } catch (Exception e) {
                System.out.println("Error reading string: " + e.getMessage());
            }
        }
        conn.close();
    }
}
