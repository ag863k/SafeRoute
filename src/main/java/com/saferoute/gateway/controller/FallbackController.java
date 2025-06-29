package com.saferoute.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        return createFallbackResponse("User Service", 
            "User service is currently unavailable. Please try again later.");
    }

    @GetMapping("/order-service")
    public ResponseEntity<Map<String, Object>> orderServiceFallback() {
        return createFallbackResponse("Order Service", 
            "Order service is currently unavailable. Please try again later.");
    }

    @GetMapping("/product-service")
    public ResponseEntity<Map<String, Object>> productServiceFallback() {
        return createFallbackResponse("Product Service", 
            "Product service is currently unavailable. Please try again later.");
    }

    @GetMapping("/{service}")
    public ResponseEntity<Map<String, Object>> genericFallback(@PathVariable String service) {
        return createFallbackResponse(service, 
            service + " is currently unavailable. Please try again later.");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String service, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("service", service);
        response.put("fallback", true);
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
