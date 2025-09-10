package com.hasan.lms_azure_demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String helloAzure() {
        return "🚀 Hello from Azure Demo Service! changes push to azure cloud!!!!!!!!!";
    }

    @GetMapping("/api/health")
    public String healthCheck() {
        return "✅ Application is healthy and running!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
    }
}
