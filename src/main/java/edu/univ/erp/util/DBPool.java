package edu.univ.erp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPool {

    private static String url;
    private static String user;
    private static String pass;

    public static void init() {
        // simple defaults, can be overridden by environment
        url = System.getenv().getOrDefault("JDBC_URL", "jdbc:h2:./data/univerp;AUTO_SERVER=TRUE");
        user = System.getenv().getOrDefault("DB_USER", "sa");
        pass = System.getenv().getOrDefault("DB_PASS", "");
    }

    public static Connection getConnection() throws SQLException {
        if (url == null) init();
        return DriverManager.getConnection(url, user, pass);
    }
}