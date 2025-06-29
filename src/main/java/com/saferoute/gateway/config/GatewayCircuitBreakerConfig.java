package com.saferoute.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class GatewayCircuitBreakerConfig {

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .permittedNumberOfCallsInHalfOpenState(3)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(3))
                .build())
            .build());
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> userServiceCustomizer() {
        return factory -> factory.configure(builder -> builder
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .permittedNumberOfCallsInHalfOpenState(3)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .minimumNumberOfCalls(5)
                .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(3))
                .build()), "user-service-cb");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> orderServiceCustomizer() {
        return factory -> factory.configure(builder -> builder
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .permittedNumberOfCallsInHalfOpenState(3)
                .failureRateThreshold(60)
                .waitDurationInOpenState(Duration.ofSeconds(45))
                .minimumNumberOfCalls(5)
                .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5))
                .build()), "order-service-cb");
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> productServiceCustomizer() {
        return factory -> factory.configure(builder -> builder
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                .slidingWindowSize(15)
                .permittedNumberOfCallsInHalfOpenState(5)
                .failureRateThreshold(40)
                .waitDurationInOpenState(Duration.ofSeconds(20))
                .minimumNumberOfCalls(8)
                .build())
            .timeLimiterConfig(TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build()), "product-service-cb");
    }
}
