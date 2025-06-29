# SafeRoute API Gateway

Production-ready enterprise API Gateway with rate limiting, circuit breaking, and JWT authentication.

## Features

- API Gateway routing with Spring Cloud Gateway
- Redis-based distributed rate limiting  
- Circuit breaking with Resilience4j
- JWT authentication
- Prometheus metrics and monitoring
- Docker deployment ready

## Tech Stack

- Java 17+, Spring Boot 3.2+
- Spring Cloud Gateway
- Redis, Resilience4j  
- JWT + Spring Security
- Prometheus + Actuator
- Docker + Docker Compose

## Quick Start

1. **Prerequisites**: Java 17+, Docker, Redis

2. **Build**: 
   ```bash
   mvn clean package -DskipTests
   ```

3. **Run with Docker**:
   ```bash
   docker-compose up -d
   ```

4. **Access**:
   - Gateway: http://localhost:8080
   - Health: http://localhost:8080/actuator/health
   - Metrics: http://localhost:8080/actuator/prometheus

## Configuration

Key configuration in `application.yml`:

- **Rate Limiting**: Redis-based, configurable per route
- **Circuit Breaking**: Resilience4j with customizable thresholds  
- **Security**: JWT token validation
- **Monitoring**: Actuator endpoints enabled

## API Routes

- `/api/auth/**` - Authentication endpoints
- `/api/health` - Health check
- `/actuator/**` - Monitoring endpoints
- Other routes proxied to configured services

## Security

- JWT token authentication
- Rate limiting by IP address
- Circuit breaker protection
- Security headers enabled

## Monitoring

- Prometheus metrics at `/actuator/prometheus`
- Health checks at `/actuator/health`
- Custom metrics for rate limiting and circuit breaker

## Production Deployment

1. Configure Redis connection
2. Set JWT secret key
3. Configure downstream service URLs
4. Set appropriate rate limits
5. Deploy with Docker or standalone JAR

## License

MIT License
