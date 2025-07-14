package com.aa.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Database Manager following Singleton pattern
 * Handles database connections using HikariCP connection pooling
 */
public class DatabaseManager {
    
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static DatabaseManager instance;
    private static final ReentrantLock lock = new ReentrantLock();
    
    private HikariDataSource dataSource;
    private boolean isInitialized = false;
    
    private DatabaseManager() {
        // Private constructor for Singleton
    }
    
    /**
     * Get DatabaseManager instance (Singleton)
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * Initialize database connection pool
     */
    public void initialize() {
        if (!isInitialized) {
            lock.lock();
            try {
                if (!isInitialized) {
                    setupConnectionPool();
                    isInitialized = true;
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Setup HikariCP connection pool
     */
    private void setupConnectionPool() {
        try {
            // Check if database is enabled
            boolean dbEnabled = ConfigManager.getBooleanProperty("db.enabled", false);
            if (!dbEnabled) {
                logger.info("Database is disabled in configuration");
                return;
            }
            
            VaultManager vault = VaultManager.getInstance();
            String jdbcUrl = vault.getSecret("db.url");
            String username = vault.getSecret("db.username");
            String password = vault.getSecret("db.password");
            
            if (jdbcUrl == null) {
                logger.warn("Database URL not configured, skipping database initialization");
                return;
            }
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            
            // Connection pool settings
            config.setMaximumPoolSize(ConfigManager.getIntProperty("db.pool.max.size", 10));
            config.setMinimumIdle(ConfigManager.getIntProperty("db.pool.min.idle", 2));
            config.setConnectionTimeout(ConfigManager.getIntProperty("db.connection.timeout", 30000));
            config.setIdleTimeout(ConfigManager.getIntProperty("db.idle.timeout", 600000));
            config.setMaxLifetime(ConfigManager.getIntProperty("db.max.lifetime", 1800000));
            
            // Additional settings
            config.setPoolName("AA-Test-Pool");
            config.setConnectionTestQuery("SELECT 1");
            config.setLeakDetectionThreshold(60000);
            
            dataSource = new HikariDataSource(config);
            
            // Test connection
            testConnection();
            
            logger.info("Database connection pool initialized successfully");
            
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool: {}", e.getMessage(), e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Test database connection
     */
    private void testConnection() throws SQLException {
        try (Connection connection = getConnection()) {
            if (connection.isValid(5)) {
                logger.info("Database connection test successful");
            } else {
                throw new SQLException("Database connection validation failed");
            }
        }
    }
    
    /**
     * Get database connection from pool
     */
    public Connection getConnection() throws SQLException {
        if (!isInitialized) {
            initialize();
        }
        
        if (dataSource == null) {
            throw new SQLException("Database connection pool is not initialized");
        }
        
        return dataSource.getConnection();
    }
    
    /**
     * Execute SELECT query and return results as List of Maps
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Set parameters
            setParameters(statement, parameters);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        Object value = resultSet.getObject(i);
                        row.put(columnName, value);
                    }
                    results.add(row);
                }
            }
            
            logger.debug("Query executed successfully: {} rows returned", results.size());
            
        } catch (SQLException e) {
            logger.error("Error executing query: {}", e.getMessage(), e);
            throw new RuntimeException("Query execution failed", e);
        }
        
        return results;
    }
    
    /**
     * Execute UPDATE/INSERT/DELETE statement
     */
    public int executeUpdate(String sql, Object... parameters) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            // Set parameters
            setParameters(statement, parameters);
            
            int rowsAffected = statement.executeUpdate();
            logger.debug("Update executed successfully: {} rows affected", rowsAffected);
            
            return rowsAffected;
            
        } catch (SQLException e) {
            logger.error("Error executing update: {}", e.getMessage(), e);
            throw new RuntimeException("Update execution failed", e);
        }
    }
    
    /**
     * Execute batch updates
     */
    public int[] executeBatch(String sql, List<Object[]> parametersList) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            connection.setAutoCommit(false);
            
            for (Object[] parameters : parametersList) {
                setParameters(statement, parameters);
                statement.addBatch();
            }
            
            int[] results = statement.executeBatch();
            connection.commit();
            
            logger.debug("Batch executed successfully: {} statements", results.length);
            
            return results;
            
        } catch (SQLException e) {
            logger.error("Error executing batch: {}", e.getMessage(), e);
            throw new RuntimeException("Batch execution failed", e);
        }
    }
    
    /**
     * Set parameters for prepared statement
     */
    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
    
    /**
     * Get single value from database
     */
    public Object getSingleValue(String sql, Object... parameters) {
        List<Map<String, Object>> results = executeQuery(sql, parameters);
        if (results.isEmpty()) {
            return null;
        }
        
        Map<String, Object> firstRow = results.get(0);
        return firstRow.values().iterator().next();
    }
    
    /**
     * Check if record exists
     */
    public boolean recordExists(String tableName, String whereClause, Object... parameters) {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + whereClause + " LIMIT 1";
        List<Map<String, Object>> results = executeQuery(sql, parameters);
        return !results.isEmpty();
    }
    
    /**
     * Get record count
     */
    public long getRecordCount(String tableName, String whereClause, Object... parameters) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (whereClause != null && !whereClause.trim().isEmpty()) {
            sql += " WHERE " + whereClause;
        }
        
        Object count = getSingleValue(sql, parameters);
        return count != null ? ((Number) count).longValue() : 0;
    }
    
    /**
     * Clean test data (delete records created during testing)
     */
    public void cleanTestData(String tableName, String whereClause, Object... parameters) {
        try {
            String sql = "DELETE FROM " + tableName + " WHERE " + whereClause;
            int deletedRows = executeUpdate(sql, parameters);
            logger.info("Cleaned test data: {} rows deleted from {}", deletedRows, tableName);
        } catch (Exception e) {
            logger.warn("Failed to clean test data from {}: {}", tableName, e.getMessage());
        }
    }
    
    /**
     * Get database metadata
     */
    public Map<String, String> getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();
        
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            info.put("database.product.name", metaData.getDatabaseProductName());
            info.put("database.product.version", metaData.getDatabaseProductVersion());
            info.put("driver.name", metaData.getDriverName());
            info.put("driver.version", metaData.getDriverVersion());
            info.put("url", metaData.getURL());
            info.put("username", metaData.getUserName());
            
        } catch (SQLException e) {
            logger.error("Error getting database info: {}", e.getMessage());
        }
        
        return info;
    }
    
    /**
     * Check database health
     */
    public boolean isDatabaseHealthy() {
        try {
            if (dataSource == null) {
                return false;
            }
            
            try (Connection connection = getConnection()) {
                return connection.isValid(5);
            }
            
        } catch (Exception e) {
            logger.warn("Database health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Close database connection pool
     */
    public void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
        isInitialized = false;
    }
    
    /**
     * Get connection pool statistics
     */
    public Map<String, Object> getPoolStats() {
        Map<String, Object> stats = new HashMap<>();
        
        if (dataSource != null) {
            stats.put("active.connections", dataSource.getHikariPoolMXBean().getActiveConnections());
            stats.put("idle.connections", dataSource.getHikariPoolMXBean().getIdleConnections());
            stats.put("total.connections", dataSource.getHikariPoolMXBean().getTotalConnections());
            stats.put("threads.awaiting.connection", dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        
        return stats;
    }
}