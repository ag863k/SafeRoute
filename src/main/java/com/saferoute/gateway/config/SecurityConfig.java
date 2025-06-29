package com.saferoute.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                // Public endpoints
                .pathMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api-docs/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/fallback/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/health").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                
                // All other requests are allowed for demo purposes
                .anyExchange().permitAll()
            )
            .build();
    }
}
