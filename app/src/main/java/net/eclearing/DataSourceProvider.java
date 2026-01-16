package net.eclearing;

import io.github.cdimascio.dotenv.Dotenv;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceProvider {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.load();
        
        config.setJdbcUrl(dotenv.get("DB_URL"));
        config.setUsername(dotenv.get("DB_USERNAME"));
        config.setPassword(dotenv.get("DB_PASSWORD"));
        config.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(config);
    }

    private DataSourceProvider() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
