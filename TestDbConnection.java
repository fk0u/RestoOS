import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDbConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/restopos?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "";

        System.out.println("Attempting connection to: " + url);
        System.out.println("User: " + user);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("SUCCESS: Connection established!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: MySQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("ERROR: Connection failed!");
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }
}