package com.hasan.lms_azure_demo.config;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id = "myendpoint")  // This becomes /actuator/myendpoint
public class MyCustomActuatorEndpoint {

    @ReadOperation
    public Map<String, Object> customData() {
        return Map.of(
                "status", "running",
                "timestamp", System.currentTimeMillis(),
                "custom-message", "Hello from custom actuator!"
        );
    }
}
