package com.purrfect.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filter to clean null header values before forwarding to downstream services.
 * This prevents NullPointerException in NettyRoutingFilter when headers have null values.
 * IMPORTANT: Does NOT consume request body - only cleans headers.
 */
@Component
public class HeaderCleaningFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(HeaderCleaningFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        
        // Check if any headers have null values
        boolean needsCleaning = headers.entrySet().stream()
                .anyMatch(entry -> {
                    if (entry.getValue() == null) {
                        return true;
                    }
                    return entry.getValue().stream().anyMatch(v -> v == null || v.trim().isEmpty());
                });
        
        if (needsCleaning) {
            // Create cleaned headers without null values
            HttpHeaders cleanedHeaders = new HttpHeaders();
            headers.forEach((key, values) -> {
                if (values != null && !values.isEmpty()) {
                    values.stream()
                            .filter(v -> v != null && !v.trim().isEmpty())
                            .forEach(v -> cleanedHeaders.add(key, v));
                }
            });
            
            // Create new request with cleaned headers
            ServerHttpRequest cleanedRequest = request.mutate()
                    .headers(httpHeaders -> {
                        httpHeaders.clear();
                        httpHeaders.addAll(cleanedHeaders);
                    })
                    .build();
            
            ServerWebExchange cleanedExchange = exchange.mutate()
                    .request(cleanedRequest)
                    .build();
            
            logger.debug("Cleaned {} null/empty headers from request", 
                    headers.size() - cleanedHeaders.size());
            
            return chain.filter(cleanedExchange);
        }
        
        // No cleaning needed, forward as-is
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Run after GatewayLoggingFilter but before routing
        return 0;
    }
}

