package com.kiloux.restopos.config;

import java.io.File;

public class DatabaseConfig {
    // MySQL Configuration
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "restopos";
    public static final String DB_USER = "restopos";
    public static final String DB_PASS = "restopos123"; // Password untuk local dev
    
    // JDBC URL construction
    public static final String DB_SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    public static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";

    // Fallback or setup checks
    public static void ensureDbDirectoryExists() {
        // Not needed for MySQL generally, but good for logs if we had file logging
    }
}
