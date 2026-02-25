package com.kiloux.restopos.config;

import java.io.File;

public class DatabaseConfig {
    // MySQL Configuration
    public static final String DB_TYPE = "SQLITE"; // Changed to SQLITE default for portability
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "restopos";
    public static final String DB_USER = "root"; // Username untuk local dev
    public static final String DB_PASS = ""; // Password untuk local dev
    
    // JDBC URL construction
    public static final String DB_SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    
    public static String getDbUrl() {
        if ("SQLITE".equals(DB_TYPE)) {
            return "jdbc:sqlite:restopos.db";
        }
        return "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }

    public static final String DB_URL = getDbUrl();

    // Fallback or setup checks
    public static void ensureDbDirectoryExists() {
        // Not needed for MySQL generally, but good for logs if we had file logging
    }
}
