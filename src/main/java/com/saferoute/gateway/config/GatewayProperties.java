package com.saferoute.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for SafeRoute Gateway
 * 
 * @author SafeRoute Team
 */
@Component
@ConfigurationProperties(prefix = "saferoute")
public class GatewayProperties {

    private final Jwt jwt = new Jwt();
    private final RateLimit rateLimit = new RateLimit();

    public Jwt getJwt() {
        return jwt;
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public static class Jwt {
        private String secret = "mySecretKey123456789abcdefghijklmnopqrstuvwxyz";
        private long expiration = 86400000; // 24 hours

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }
    }

    public static class RateLimit {
        private final Default defaultConfig = new Default();
        private final Map<String, ServiceConfig> services = new HashMap<>();

        public Default getDefault() {
            return defaultConfig;
        }

        public Map<String, ServiceConfig> getServices() {
            return services;
        }

        public static class Default {
            private int requestsPerMinute = 60;
            private int burstCapacity = 100;

            public int getRequestsPerMinute() {
                return requestsPerMinute;
            }

            public void setRequestsPerMinute(int requestsPerMinute) {
                this.requestsPerMinute = requestsPerMinute;
            }

            public int getBurstCapacity() {
                return burstCapacity;
            }

            public void setBurstCapacity(int burstCapacity) {
                this.burstCapacity = burstCapacity;
            }
        }

        public static class ServiceConfig {
            private int requestsPerMinute;
            private int burstCapacity;

            public int getRequestsPerMinute() {
                return requestsPerMinute;
            }

            public void setRequestsPerMinute(int requestsPerMinute) {
                this.requestsPerMinute = requestsPerMinute;
            }

            public int getBurstCapacity() {
                return burstCapacity;
            }

            public void setBurstCapacity(int burstCapacity) {
                this.burstCapacity = burstCapacity;
            }
        }
    }
}
