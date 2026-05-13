package com.gft.recruitment.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Global filter that ensures every request has a correlation ID for distributed tracing.
 * <p>
 * - Reads X-Correlation-Id from incoming request headers
 * - Generates a new UUID if not present
 * - Propagates it to downstream services via request header
 * - Adds it to the response headers
 * - Makes it available in MDC for structured logging
 */
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        final String finalCorrelationId = correlationId;

        // Add correlation ID to the request header for downstream services
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CORRELATION_ID_HEADER, finalCorrelationId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        // Add correlation ID to response headers
        ServerHttpResponse response = mutatedExchange.getResponse();
        response.getHeaders().add(CORRELATION_ID_HEADER, finalCorrelationId);

        return chain.filter(mutatedExchange)
                .contextWrite(ctx -> ctx.put(CORRELATION_ID_MDC_KEY, finalCorrelationId))
                .doFirst(() -> {
                    MDC.put(CORRELATION_ID_MDC_KEY, finalCorrelationId);
                    log.debug("Correlation ID set: {}", finalCorrelationId);
                })
                .doFinally(signalType -> MDC.remove(CORRELATION_ID_MDC_KEY));
    }

    @Override
    public int getOrder() {
        // Run before JWT filter (-100) to ensure correlation ID is available for all logging
        return -200;
    }
}
