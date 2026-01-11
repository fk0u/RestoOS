package com.kiloux.restopos.utils;

import com.kiloux.restopos.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);
    }
    
    private static void ensureDatabaseExists() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.DB_SERVER_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);
             Statement stmt = conn.createStatement()) {
             System.out.println("Checking database existence...");
             stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.DB_NAME);
             System.out.println("Database '" + DatabaseConfig.DB_NAME + "' confirmed.");
        } catch (SQLException e) {
             System.err.println("Failed to create database: " + e.getMessage());
             e.printStackTrace();
        }
    }

    public static void initializeDatabase() {
        ensureDatabaseExists();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
             System.out.println("Connected to database. Initializing tables...");

            // Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER')");

            // Restaurant Tables
            stmt.execute("CREATE TABLE IF NOT EXISTS tables (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "table_number INTEGER UNIQUE NOT NULL," +
                    "capacity INTEGER NOT NULL," +
                    "status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'," + // AVAILABLE, OCCUPIED, RESERVED
                    "x_pos INTEGER DEFAULT 0," +
                    "y_pos INTEGER DEFAULT 0)");

            // Menu Items
            stmt.execute("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "category VARCHAR(50) NOT NULL," +
                    "price DECIMAL(10,2) NOT NULL," +
                    "stock INTEGER DEFAULT 0," +
                    "image_path VARCHAR(255)," +
                    "description TEXT)");

            // Orders
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "table_id INTEGER," +
                    "order_type VARCHAR(20) DEFAULT 'DINE_IN'," + 
                    "status VARCHAR(20) NOT NULL," + // PENDING, CONFIRMED, COMPLETED, CANCELLED
                    "total_amount DECIMAL(10,2) DEFAULT 0.0," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "payment_status VARCHAR(20) DEFAULT 'UNPAID'," +
                    "customer_name VARCHAR(100)," + 
                    "queue_number INTEGER)");

            // Order Items
            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "order_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "quantity INTEGER NOT NULL," +
                    "subtotal DECIMAL(10,2) NOT NULL," +
                    "notes TEXT," +
                    "FOREIGN KEY(order_id) REFERENCES orders(id)," +
                    "FOREIGN KEY(menu_item_id) REFERENCES menu_items(id))");

            // Payments
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "order_id INTEGER," +
                    "method VARCHAR(20) NOT NULL," + 
                    "amount DECIMAL(10,2) NOT NULL," +
                    "transaction_id VARCHAR(100)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Wishlist
            stmt.execute("CREATE TABLE IF NOT EXISTS wishlist (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "user_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Seed Data if needed
            seedData(stmt);
            
            // Ensure default admin user
            seedUsers(stmt);

             System.out.println("Database initialization completed.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void seedUsers(Statement stmt) throws SQLException {
        if (stmt.executeQuery("SELECT count(*) FROM users").next()) {
             // Basic check logic, robust check:
             java.sql.ResultSet rs = stmt.executeQuery("SELECT count(*) FROM users");
             rs.next();
             if (rs.getInt(1) == 0) {
                 System.out.println("Seeding default users...");
                 stmt.execute("INSERT INTO users (username, password, role) VALUES ('admin', 'admin', 'ADMIN')");
                 stmt.execute("INSERT INTO users (username, password, role) VALUES ('kasir', 'kasir', 'CASHIER')");
                 stmt.execute("INSERT INTO users (username, password, role) VALUES ('client', 'client', 'CLIENT')");
             }
        }
    }

    private static void seedData(Statement stmt) throws SQLException {
        // Simple Count Check
        java.sql.ResultSet rs = stmt.executeQuery("SELECT count(*) FROM menu_items");
        rs.next();
        if (rs.getInt(1) == 0) {
             System.out.println("Seeding menu items...");
             stmt.execute("INSERT INTO menu_items (name, category, price, stock, description, image_path) VALUES " +
                     "('Kou Signature Chicken', 'Main Course', 45000, 50, 'Ayam goreng khas Kou dengan bumbu rahasia', 'chicken_sig.jpg')," +
                     "('Sapporo Ramen', 'Main Course', 55000, 30, 'Ramen kuah kaldu sapi asli', 'ramen.jpg')," +
                     "('Matcha Latte', 'Beverage', 28000, 40, 'Premium matcha dengan susu segar', 'matcha.jpg')," +
                     "('Ebi Furai', 'Side Dish', 35000, 25, 'Udang goreng tepung renyah', 'ebi.jpg')," +
                     "('Ocha Cold', 'Beverage', 12000, 100, 'Teh hijau dingin menyegarkan', 'ocha.jpg')," +
                     "('Beef Teriyaki Set', 'Main Course', 65000, 20, 'Daging sapi panggang saus teriyaki + nasi', 'beef_teri.jpg')," +
                     "('Salmon Sashimi', 'Main Course', 85000, 15, 'Salmon segar impor Norwegia', 'salmon.jpg')," +
                     "('Takoyaki', 'Side Dish', 30000, 40, 'Bola gurita khas Osaka (6 pcs)', 'takoyaki.jpg')," +
                     "('Gyoza', 'Side Dish', 28000, 35, 'Dumpling ayam panggang (5 pcs)', 'gyoza.jpg')," +
                     "('Lychee Tea', 'Beverage', 25000, 50, 'Teh leci dengan buah asli', 'lychee.jpg')," +
                     "('Mango Smoothie', 'Beverage', 32000, 25, 'Smoothie mangga harum manis', 'mango.jpg')," +
                     "('Chicken Katsu Curry', 'Main Course', 50000, 45, 'Nasi kari Jepang dengan katsu ayam', 'katsu.jpg')");
        }
        
        rs = stmt.executeQuery("SELECT count(*) FROM tables");
        rs.next();
        if (rs.getInt(1) == 0) {
             System.out.println("Seeding tables...");
             for(int i=1; i<=16; i++) {
                 int capacity = (i % 4 == 0) ? 6 : 4; 
                 stmt.execute("INSERT INTO tables (table_number, capacity) VALUES (" + i + ", " + capacity + ")");
             }
        }
    }
    
    public static void main(String[] args) {
        initializeDatabase();
    }
}
