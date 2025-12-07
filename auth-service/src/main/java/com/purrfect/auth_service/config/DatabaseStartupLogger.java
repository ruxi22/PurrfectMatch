package com.purrfect.auth_service.config;

import com.purrfect.auth_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Component
@Order(1)
public class DatabaseStartupLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStartupLogger.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Override
    public void run(String... args) throws Exception {
        logger.info("==========================================");
        logger.info("AUTH SERVICE STARTUP - DATABASE CONFIGURATION");
        logger.info("==========================================");
        logger.info("Datasource URL: {}", datasourceUrl);
        logger.info("Datasource Username: {}", datasourceUsername);
        logger.info("Datasource Password: [HIDDEN]");
        logger.info("==========================================");

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            logger.info("Database Connection Test: SUCCESS");
            logger.info("Database Product: {}", metaData.getDatabaseProductName());
            logger.info("Database Version: {}", metaData.getDatabaseProductVersion());
            logger.info("Driver Name: {}", metaData.getDriverName());
            logger.info("Driver Version: {}", metaData.getDriverVersion());
            logger.info("URL: {}", metaData.getURL());
            logger.info("Username: {}", metaData.getUserName());

            //test query to check if users table exists and get counlt
            try {
                ResultSet tables = metaData.getTables(null, null, "users", null);
                if (tables.next()) {
                    logger.info("Users table exists: YES");
                    
                    //count existing users
                    long userCount = userRepository.count();
                    logger.info("Current user count in database: {}", userCount);
                    
                    //list all users
                    if (userCount > 0) {
                        logger.info("Existing users:");
                        userRepository.findAll().forEach(user -> {
                            logger.info("  - ID: {}, Username: {}, Email: {}, Role: {}", 
                                user.getId(), user.getUsername(), user.getEmail(), user.getRole());
                        });
                    } else {
                        logger.info("No users found in database (database is empty or fresh)");
                    }
                } else {
                    logger.warn("Users table does NOT exist yet - Hibernate will create it on first use");
                }
            } catch (Exception e) {
                logger.error("Error checking users table: {}", e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error("==========================================");
            logger.error("DATABASE CONNECTION TEST: FAILED");
            logger.error("Error: {}", e.getMessage(), e);
            logger.error("==========================================");
            throw e;
        }

        logger.info("==========================================");
        logger.info("AUTH SERVICE READY");
        logger.info("==========================================");
    }
}

