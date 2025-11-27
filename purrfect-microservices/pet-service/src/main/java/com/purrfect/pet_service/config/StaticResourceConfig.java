package com.purrfect.pet_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(StaticResourceConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fileUrl = "file:uploads/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(fileUrl)
                .setCachePeriod(3600); // Cache for 1 hour
        
        String absolutePath = Paths.get("uploads").toAbsolutePath().toString();
        logger.info("StaticResourceConfig initialized: Serving /uploads/** from {} (absolute: {})", fileUrl, absolutePath);
    }
}
