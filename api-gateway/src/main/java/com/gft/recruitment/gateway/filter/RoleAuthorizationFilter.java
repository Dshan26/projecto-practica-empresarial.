package com.gft.recruitment.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RoleAuthorizationFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RoleAuthorizationFilter.class);

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String role = request.getHeaders().getFirst("X-User-Role");
        if (role == null) {
            return chain.filter(exchange);
        }

        HttpMethod method = request.getMethod();

        if (!isAuthorized(path, method, role)) {
            log.warn("Access denied for role {} on {} {}", role, method, path);
            return onForbidden(exchange, "Access denied: insufficient permissions for role " + role);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -90;
    }

    private boolean isAuthorized(String path, HttpMethod method, String role) {
        // /api/job-offers POST/PUT/DELETE → RECRUITER only
        if (path.startsWith("/api/job-offers") && isWriteMethod(method)) {
            return "RECRUITER".equals(role);
        }

        // /api/cv/upload → CANDIDATE only
        if (path.equals("/api/cv/upload") && HttpMethod.POST.equals(method)) {
            return "CANDIDATE".equals(role);
        }

        // /api/tests/submit → CANDIDATE only
        if (path.equals("/api/tests/submit") && HttpMethod.POST.equals(method)) {
            return "CANDIDATE".equals(role);
        }

        // /api/rankings/** → RECRUITER only
        if (path.startsWith("/api/rankings")) {
            return "RECRUITER".equals(role);
        }

        return true;
    }

    private boolean isWriteMethod(HttpMethod method) {
        return HttpMethod.POST.equals(method)
                || HttpMethod.PUT.equals(method)
                || HttpMethod.DELETE.equals(method);
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> onForbidden(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"error\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
