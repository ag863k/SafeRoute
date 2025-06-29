package com.saferoute.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String method = request.getMethod().toString();
        String path = request.getPath().value();
        String clientIp = getClientIp(request);
        String userAgent = request.getHeaders().getFirst("User-Agent");
        
        logger.info("🚀 [{}] {} {} from {} - User-Agent: {}", 
                   timestamp, method, path, clientIp, userAgent);

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
            .doOnTerminate(() -> {
                long duration = System.currentTimeMillis() - startTime;
                int statusCode = exchange.getResponse().getStatusCode() != null ? 
                    exchange.getResponse().getStatusCode().value() : 0;
                
                logger.info("✅ [{}] {} {} - Status: {} - Duration: {}ms", 
                           timestamp, method, path, statusCode, duration);
            })
            .doOnError(throwable -> {
                long duration = System.currentTimeMillis() - startTime;
                logger.error("❌ [{}] {} {} - Error: {} - Duration: {}ms", 
                            timestamp, method, path, throwable.getMessage(), duration);
            });
    }

    private String getClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        var remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null && remoteAddress.getAddress() != null) {
            return remoteAddress.getAddress().getHostAddress();
        }
        
        return "unknown";
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}
