package com.purrfect.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${PET_SERVICE_URL:http://localhost:8082}")
    private String petServiceUrl;

    @Value("${ADOPTION_SERVICE_URL:http://localhost:8083}")
    private String adoptionServiceUrl;

    @Value("${NOTIFICATION_SERVICE_URL:http://localhost:8084}")
    private String notificationServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri(authServiceUrl))
                .route("pet-service", r -> r
                        .path("/api/pets/**")
                        .uri(petServiceUrl))
                .route("pet-service-static", r -> r
                        .path("/uploads/**")
                        .filters(f -> f.stripPrefix(0))
                        .uri(petServiceUrl))        
                .route("adoption-service", r -> r
                        .path("/api/adoptions/**")
                        .uri(adoptionServiceUrl))
                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .uri(notificationServiceUrl))
                .build();

    }
}

