package com.jinyou.rtc.realtimechat.global.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class H2ServerConfig {

    private Server server = null;

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource setupH2Server() throws SQLException {
        server = Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort",
                "9092"
        ).start();

        return new HikariDataSource();
    }

    @PreDestroy
    public void destroy() {
        if (server != null) {
            server.stop();
        }
    }
}
