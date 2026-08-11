package com.pustakaalay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Pustakaalay Backend is running successfully!";
    }

    @GetMapping("/api/health")
    public String health() {
        return "OK";
    }
}
