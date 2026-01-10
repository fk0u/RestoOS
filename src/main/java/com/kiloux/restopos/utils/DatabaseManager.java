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
                    "order_type TEXT DEFAULT 'DINE_IN'," + // DINE_IN, TAKEAWAY
                    "status TEXT NOT NULL," + // PENDING, CONFIRMED, COMPLETED, CANCELLED
                    "total_amount REAL DEFAULT 0.0," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "payment_status TEXT DEFAULT 'UNPAID'," +
                    "customer_name TEXT," + 
                    "queue_number INTEGER)");

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

            // Wishlist
            stmt.execute("CREATE TABLE IF NOT EXISTS wishlist (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "menu_item_id INTEGER," +
                    "added_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
            
            // Seed Data if needed
            seedData(stmt);

            // Migration for Existing Install (Industrial Update)
            try { stmt.execute("ALTER TABLE orders ADD COLUMN customer_name TEXT"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE orders ADD COLUMN queue_number INTEGER"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE orders ADD COLUMN order_type TEXT DEFAULT 'DINE_IN'"); } catch (SQLException ignored) {}
            try { stmt.execute("ALTER TABLE orders ADD COLUMN payment_status TEXT DEFAULT 'UNPAID'"); } catch (SQLException ignored) {}

             System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void seedData(Statement stmt) throws SQLException {
        // Check if data exists
        if (stmt.executeQuery("SELECT count(*) FROM menu_items").getInt(1) == 0) {
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
        
        if (stmt.executeQuery("SELECT count(*) FROM tables").getInt(1) == 0) {
             System.out.println("Seeding tables...");
             for(int i=1; i<=16; i++) {
                 int capacity = (i % 4 == 0) ? 6 : 4; // Vary capacity
                 stmt.execute("INSERT INTO tables (table_number, capacity) VALUES (" + i + ", " + capacity + ")");
             }
        }

        // Expanded Menu Update (Industrial Phase)
        if (stmt.executeQuery("SELECT count(*) FROM menu_items").getInt(1) <= 12) {
             System.out.println("Seeding EXTRA menu items...");
             stmt.execute("INSERT INTO menu_items (name, category, price, stock, description, image_path) VALUES " +
                     "('Sushi Platter (Mixed)', 'Main Course', 120000, 15, '12 pcs mixed sushi premium', 'sushi_plat.jpg')," +
                     "('Tempura Udon', 'Main Course', 58000, 25, 'Udon kuah dengan udang tempura', 'udon.jpg')," +
                     "('Edamame', 'Side Dish', 15000, 50, 'Kacang kedelai rebus asin', 'edamame.jpg')," +
                     "('Miso Soup', 'Side Dish', 18000, 60, 'Sup miso hangat dengan tahu', 'miso.jpg')," +
                     "('Chicken Teriyaki Don', 'Main Course', 42000, 40, 'Nasi ayam saus teriyaki mangkuk', 'chk_teri_don.jpg')," +
                     "('Beef Curry', 'Main Course', 62000, 20, 'Kari sapi jepang autentik', 'beef_curry.jpg')," +
                     "('Choco Lava Cake', 'Dessert', 35000, 15, 'Cake coklat leleh dengan es krim', 'lava_cake.jpg')," +
                     "('Vanilla Ice Cream', 'Dessert', 15000, 50, '2 scoop es krim vanilla', 'ice_cream.jpg')," +
                     "('Matcha Ice Cream', 'Dessert', 18000, 50, '2 scoop es krim matcha', 'matcha_ice.jpg')," +
                     "('Iced Lemon Tea', 'Beverage', 18000, 80, 'Teh lemon segar dingin', 'lemon_tea.jpg')," +
                     "('Cappuccino Hot', 'Beverage', 25000, 40, 'Kopi susu panas', 'cappuccino.jpg')," +
                     "('Iced Americano', 'Beverage', 22000, 60, 'Kopi hitam dingin tanpa gula', 'americano.jpg')," +
                     "('Sparkling Water', 'Beverage', 15000, 30, 'Air soda murni', 'sparkling.jpg')," +
                     "('Mineral Water', 'Beverage', 8000, 100, 'Air mineral 600ml', 'water.jpg')," +
                     "('Yakitori Set', 'Side Dish', 45000, 20, 'Sate ayam jepang 5 tusuk', 'yakitori.jpg')");
        }
    }
}
