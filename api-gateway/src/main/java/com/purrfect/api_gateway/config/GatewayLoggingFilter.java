package com.purrfect.api_gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class GatewayLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        logger.info("==========================================");
        logger.info("API GATEWAY REQUEST");
        logger.info("Method: {}", request.getMethod());
        logger.info("Path: {}", request.getURI().getPath());
        logger.info("Query: {}", request.getURI().getQuery());
        logger.info("Headers: {}", request.getHeaders());
        logger.info("==========================================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            if (exchange.getResponse().getStatusCode() != null) {
                logger.info("API GATEWAY RESPONSE: Status {}", exchange.getResponse().getStatusCode());
            }
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

