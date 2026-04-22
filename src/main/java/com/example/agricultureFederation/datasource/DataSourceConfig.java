package com.example.agricultureFederation.datasource;

import io.github.cdimascio.dotenv.Dotenv;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .ignoreIfMissing()
            .load();

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        String host     = dotenv.get("DB_HOST",     "localhost");
        String port     = dotenv.get("DB_PORT",     "5432");
        String name     = dotenv.get("DB_NAME",     "federation_db");
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;

        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        return new HikariDataSource(config);
    }
}