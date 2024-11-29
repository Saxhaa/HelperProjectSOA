package fr.insa.soa.missionService.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabasePropertiesLogger {

    private static final Logger logger = LoggerFactory.getLogger(DatabasePropertiesLogger.class);

    @Autowired
    private DatabaseProperties databaseProperties;

    @PostConstruct
    public void logProperties() {
        logger.info("Database URL: {}", databaseProperties.getUrl());
        logger.info("Database Username: {}", databaseProperties.getUsername());
    }
}
