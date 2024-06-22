package com.DmitriyDevv.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static final String DB_URL = "com/DmitriyDevv/db/CurrencyExchange.sqlite";
    private static final String DB_DRIVER = "jdbc:sqlite:";

    public static Connection getConnection() throws SQLException {
        try {
            ClassLoader classLoader = DBManager.class.getClassLoader();
            if (classLoader.getResource(DB_URL) != null) {
                String dbUrl = DB_DRIVER + classLoader.getResource(DB_URL).getPath();
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(dbUrl);
            } else {
                throw new SQLException();
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new SQLException();
        }
    }

    public static void execute(Executor executor) throws SQLException {
        try (Connection conn = getConnection()) {
            try {
                conn.setAutoCommit(false);
                executor.execute(conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
