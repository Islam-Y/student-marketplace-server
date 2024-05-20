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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getURI().getPath().startsWith("/ws/")) {
            // WebSocket handshake should be handled differently
            return chain.filter(exchange);
        }

        if (validator.isSecured.test(request)) {
            var authHeader = request.getHeaders().getOrEmpty("Authorization");
            if (authHeader.isEmpty() || !authHeader.get(0).startsWith("Bearer ")) {
                log.info("{}: Auth header not valid!", exchange.getRequest().getPath());
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String jwt = authHeader.get(0).substring(7);

            try {
                if (jwtService.isTokenExpired(jwt)) {
                    log.info("{}: JWT Token is expired!", exchange.getRequest().getPath());
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception ex) {
                log.info("{}: JWT Token is not own-signed!", exchange.getRequest().getPath());
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            exchange.getAttributes().put("jwt", jwt);
        }

        log.info("{}: Filter passed!", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
