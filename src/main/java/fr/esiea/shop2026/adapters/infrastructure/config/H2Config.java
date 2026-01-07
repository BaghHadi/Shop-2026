package fr.esiea.shop2026.adapters.infrastructure.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
public class H2Config {

    // Cette méthode force le démarrage d'un serveur H2 accessible
    // même si la console Spring bug.
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        // Lance H2 sur le port 9092 (port standard H2)
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}