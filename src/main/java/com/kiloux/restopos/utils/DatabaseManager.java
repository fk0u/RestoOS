package com.kiloux.restopos.utils;

import com.kiloux.restopos.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    static {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        DatabaseConfig.ensureDbDirectoryExists();
        return DriverManager.getConnection(DatabaseConfig.DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Users Table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role TEXT NOT NULL DEFAULT 'CUSTOMER')");

            // Restaurant Tables
            stmt.execute("CREATE TABLE IF NOT EXISTS tables (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "table_number INTEGER UNIQUE NOT NULL," +
                    "capacity INTEGER NOT NULL," +
                    "status TEXT NOT NULL DEFAULT 'AVAILABLE'," + // AVAILABLE, OCCUPIED, RESERVED
                    "x_pos INTEGER DEFAULT 0," +
                    "y_pos INTEGER DEFAULT 0)");

            // Menu Items
            stmt.execute("CREATE TABLE IF NOT EXISTS menu_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "price REAL NOT NULL," +
                    "stock INTEGER DEFAULT 0," +
                    "image_path TEXT," +
                    "description TEXT)");

            // Orders
            stmt.execute("CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "table_id INTEGER," +
                    "status TEXT NOT NULL," + // PENDING, CONFIRMED, COMPLETED, CANCELLED
                    "total_amount REAL DEFAULT 0.0," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "payment_status TEXT DEFAULT 'UNPAID')");

            // Order Items
            stmt.execute("CREATE TABLE IF NOT EXISTS order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "quantity INTEGER NOT NULL," +
                    "subtotal REAL NOT NULL," +
                    "notes TEXT," +
                    "FOREIGN KEY(order_id) REFERENCES orders(id)," +
                    "FOREIGN KEY(menu_item_id) REFERENCES menu_items(id))");

            // Payments
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER," +
                    "method TEXT NOT NULL," + // CASH, QRIS
                    "amount REAL NOT NULL," +
                    "transaction_id TEXT," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Seed Data if needed
            seedData(stmt);

             System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void seedData(Statement stmt) throws SQLException {
        // Check if data exists
        if (stmt.executeQuery("SELECT count(*) FROM menu_items").getInt(1) == 0) {
             System.out.println("Seeding menu items...");
             stmt.execute("INSERT INTO menu_items (name, category, price, stock, description) VALUES " +
                     "('Kou Signature Chicken', 'Main Course', 45000, 50, 'Ayam goreng khas Kou dengan bumbu rahasia')," +
                     "('Sapporo Ramen', 'Main Course', 55000, 30, 'Ramen kuah kaldu sapi asli')," +
                     "('Matcha Latte', 'Beverage', 28000, 40, 'Premium matcha dengan susu segar')," +
                     "('Ebi Furai', 'Side Dish', 35000, 25, 'Udang goreng tepung renyah')," +
                     "('Ocha Cold', 'Beverage', 12000, 100, 'Teh hijau dingin menyegarkan')");
        }
        
        if (stmt.executeQuery("SELECT count(*) FROM tables").getInt(1) == 0) {
             System.out.println("Seeding tables...");
             for(int i=1; i<=16; i++) {
                 int capacity = (i % 4 == 0) ? 6 : 4; // Vary capacity
                 stmt.execute("INSERT INTO tables (table_number, capacity) VALUES (" + i + ", " + capacity + ")");
             }
        }
    }
}
