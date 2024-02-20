package com.startit.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat-service", r -> r
                        .path("/api/v1/chat/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://chat-service"))
                .route("item-service", r -> r.path("/api/v1/item/**",
                                "/api/v1/feedback/**",
                                "/api/v1/category/**",
                                "/api/v1/location/**",
                                "/api/v1/status/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://item-service"))
                .route("user-service", r -> r.path("/api/v1/user/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .build();
    }
}
