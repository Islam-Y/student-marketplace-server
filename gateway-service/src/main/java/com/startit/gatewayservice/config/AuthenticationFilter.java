package com.startit.gatewayservice.config;

import com.startit.gatewayservice.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator validator;
    private final JwtService jwtService;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        log.info("Started");

        if (validator.isSecured.test(request)) {
            log.info("Inside");

            var authHeader = request.getHeaders().getOrEmpty("Authorization");
            if (!authHeader.isEmpty() && !authHeader.get(0).startsWith("Bearer "))
                return onError(exchange, HttpStatus.UNAUTHORIZED);

            String jwt = authHeader.get(0).substring(7);

            try {
                if (jwtService.isTokenExpired(jwt))
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
            } catch (Exception ex) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }
        }

        log.info("Finished");

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
