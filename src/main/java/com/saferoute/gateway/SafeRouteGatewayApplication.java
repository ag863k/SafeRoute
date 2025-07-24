package com.saferoute.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Optional;

@SpringBootApplication
public class SafeRouteGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafeRouteGatewayApplication.class, args);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                .orElseGet(() -> {
                    var remoteAddress = exchange.getRequest().getRemoteAddress();
                    return (remoteAddress != null && remoteAddress.getAddress() != null)
                        ? remoteAddress.getAddress().getHostAddress()
                        : "unknown";
                })
        );
    }
}