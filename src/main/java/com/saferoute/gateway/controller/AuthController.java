package com.saferoute.gateway.controller;

import com.saferoute.gateway.dto.LoginRequest;
import com.saferoute.gateway.security.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // In a real application, you would validate credentials against a database
        // This is just for demo purposes
        if (isValidCredentials(loginRequest.getUsername(), loginRequest.getPassword())) {
            List<String> roles = getUserRoles(loginRequest.getUsername());
            String userId = getUserId(loginRequest.getUsername());
            
            String token = jwtTokenUtil.generateToken(loginRequest.getUsername(), roles, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", loginRequest.getUsername());
            response.put("roles", roles);
            response.put("userId", userId);
            response.put("timestamp", LocalDateTime.now());
            response.put("expiresIn", 86400); // 24 hours in seconds
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody TokenValidationRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        if (jwtTokenUtil.validateToken(request.getToken())) {
            String username = jwtTokenUtil.getUsernameFromToken(request.getToken());
            List<String> roles = jwtTokenUtil.getRolesFromToken(request.getToken());
            String userId = jwtTokenUtil.getUserIdFromToken(request.getToken());
            
            response.put("valid", true);
            response.put("username", username);
            response.put("roles", roles);
            response.put("userId", userId);
            response.put("timestamp", LocalDateTime.now());
        } else {
            response.put("valid", false);
            response.put("error", "Invalid or expired token");
            response.put("timestamp", LocalDateTime.now());
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            if (jwtTokenUtil.validateToken(token)) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                List<String> roles = jwtTokenUtil.getRolesFromToken(token);
                String userId = jwtTokenUtil.getUserIdFromToken(token);
                
                Map<String, Object> response = new HashMap<>();
                response.put("username", username);
                response.put("roles", roles);
                response.put("userId", userId);
                response.put("timestamp", LocalDateTime.now());
                
                return ResponseEntity.ok(response);
            }
        }
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid or missing token");
        errorResponse.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Demo validation - In real app, this would check against database
    private boolean isValidCredentials(String username, String password) {
        return ("admin".equals(username) && "admin123".equals(password)) ||
               ("user".equals(username) && "user123".equals(password)) ||
               ("demo".equals(username) && "demo123".equals(password));
    }

    // Demo role assignment - In real app, this would come from database
    private List<String> getUserRoles(String username) {
        return switch (username) {
            case "admin" -> List.of("ADMIN", "USER");
            case "user" -> List.of("USER");
            case "demo" -> List.of("USER");
            default -> List.of("GUEST");
        };
    }

    // Demo user ID generation - In real app, this would come from database
    private String getUserId(String username) {
        return switch (username) {
            case "admin" -> "1";
            case "user" -> "2";
            case "demo" -> "3";
            default -> "0";
        };
    }

    // Request DTOs
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class TokenValidationRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
