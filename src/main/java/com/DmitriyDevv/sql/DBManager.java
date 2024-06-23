package com.DmitriyDevv.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static final String DB_URL = "com/DmitriyDevv/db/CurrencyExchange.sqlite";
    private static final String DB_DRIVER = "jdbc:sqlite:";
    private static final HikariDataSource dataSource;

    static {
        try {
            ClassLoader classLoader = DBManager.class.getClassLoader();
            if (classLoader.getResource(DB_URL) != null) {
                String dbUrl = DB_DRIVER + classLoader.getResource(DB_URL).getPath();
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(dbUrl);
                config.setDriverClassName("org.sqlite.JDBC");
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                dataSource = new HikariDataSource(config);
            } else {
                throw new SQLException("Database URL not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database connection pool", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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
