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

    private static final int DEFAULT_SLIDING_WINDOW_SIZE = 10;
    private static final int DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN = 3;
    private static final float DEFAULT_FAILURE_RATE_THRESHOLD = 50.0f;
    private static final Duration DEFAULT_WAIT_DURATION_IN_OPEN = Duration.ofSeconds(30);
    private static final float DEFAULT_SLOW_CALL_RATE_THRESHOLD = 50.0f;
    private static final Duration DEFAULT_SLOW_CALL_DURATION_THRESHOLD = Duration.ofSeconds(2);
    private static final Duration DEFAULT_TIMEOUT_DURATION = Duration.ofSeconds(3);

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        var circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowSize(DEFAULT_SLIDING_WINDOW_SIZE)
            .permittedNumberOfCallsInHalfOpenState(DEFAULT_PERMITTED_CALLS_IN_HALF_OPEN)
            .failureRateThreshold(DEFAULT_FAILURE_RATE_THRESHOLD)
            .waitDurationInOpenState(DEFAULT_WAIT_DURATION_IN_OPEN)
            .slowCallRateThreshold(DEFAULT_SLOW_CALL_RATE_THRESHOLD)
            .slowCallDurationThreshold(DEFAULT_SLOW_CALL_DURATION_THRESHOLD)
            .build();

        var timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(DEFAULT_TIMEOUT_DURATION)
            .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .circuitBreakerConfig(circuitBreakerConfig)
            .timeLimiterConfig(timeLimiterConfig)
            .build());
    }
}
