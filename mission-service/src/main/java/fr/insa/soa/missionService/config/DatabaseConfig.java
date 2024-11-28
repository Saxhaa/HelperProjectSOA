package fr.insa.soa.missionService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_047")
                .username("projet_gei_047")
                .password("Aiboot5i")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
