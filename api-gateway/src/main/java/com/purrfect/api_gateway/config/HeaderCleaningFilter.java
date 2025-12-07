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


@Component
public class HeaderCleaningFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(HeaderCleaningFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        boolean needsCleaning = headers.entrySet().stream()
                .anyMatch(entry -> {
                    if (entry.getValue() == null) {
                        return true;
                    }
                    return entry.getValue().stream().anyMatch(v -> v == null || v.trim().isEmpty());
                });
        
        if (needsCleaning) {
            HttpHeaders cleanedHeaders = new HttpHeaders();
            headers.forEach((key, values) -> {
                if (values != null && !values.isEmpty()) {
                    values.stream()
                            .filter(v -> v != null && !v.trim().isEmpty())
                            .forEach(v -> cleanedHeaders.add(key, v));
                }
            });

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

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

