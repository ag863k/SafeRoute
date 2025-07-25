package com.saferoute.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "SafeRoute API Gateway");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("uptime", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
