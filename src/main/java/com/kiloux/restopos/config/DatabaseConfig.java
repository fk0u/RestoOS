package com.kiloux.restopos.config;

import java.io.File;

public class DatabaseConfig {
    // Determine the database file path
    // In production/jar, we would move this to user.home or app data folder.
    // For this assignment's structure, we target the resources folder relative to working dir or use a designated folder
    public static final String DB_FOLDER = "src/main/resources/database"; 
    public static final String DB_NAME = "restaurant.db";
    public static final String DB_URL = "jdbc:sqlite:" + DB_FOLDER + "/" + DB_NAME;

    public static void ensureDbDirectoryExists() {
        File dir = new File(DB_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}
