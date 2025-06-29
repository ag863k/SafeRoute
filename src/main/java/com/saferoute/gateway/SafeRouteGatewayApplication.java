package com.saferoute.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SafeRouteGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeRouteGatewayApplication.class, args);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String clientIp = exchange.getRequest()
                .getHeaders()
                .getFirst("X-Forwarded-For");
            
            if (clientIp == null || clientIp.isEmpty()) {
                if (exchange.getRequest().getRemoteAddress() != null && 
                    exchange.getRequest().getRemoteAddress().getAddress() != null) {
                    clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
                } else {
                    clientIp = "unknown";
                }
            }
            
            return Mono.just(clientIp);
        };
    }
}