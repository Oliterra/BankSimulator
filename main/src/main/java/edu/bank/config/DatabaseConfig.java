package edu.bank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.ConnectionHolder;

import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public ConnectionHolder connectionHolder() {
        try {
            return new ConnectionHolder(DriverManager.getConnection(url, username, password));
        } catch (SQLException e) {
            throw new InternalError("Connection to database is refused");
        }
    }
}
