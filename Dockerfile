# SafeRoute API Gateway Dockerfile
FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace/app

COPY pom.xml .
COPY .mvn .mvn
COPY src src

RUN apk add --no-cache maven

RUN mvn clean package -DskipTests

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production stage
FROM eclipse-temurin:17-jre-alpine

# Add a non-root user
RUN addgroup -g 1000 saferoute && adduser -u 1000 -G saferoute -s /bin/sh -D saferoute

# Create app directory
WORKDIR /app

# Copy the application from build stage
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Change ownership to saferoute user
RUN chown -R saferoute:saferoute /app

# Switch to non-root user
USER saferoute

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Run the application
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.saferoute.gateway.SafeRouteGatewayApplication"]
