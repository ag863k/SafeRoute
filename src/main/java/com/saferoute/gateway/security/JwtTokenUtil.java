package com.saferoute.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {

    private final SecretKey secretKey;

    public JwtTokenUtil(@Value("${saferoute.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extract username from JWT token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extract roles from JWT token
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("roles", List.class);
    }

    /**
     * Extract user ID from JWT token
     */
    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", String.class);
    }

    /**
     * Extract all claims from JWT token
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Generate a new JWT token (for testing purposes)
     */
    public String generateToken(String username, List<String> roles, String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000); // 24 hours

        return Jwts.builder()
            .subject(username)
            .claim("roles", roles)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }
}
