package com.hasan.lms_azure_demo.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyCustomActuatorInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app-details", 
            Map.of("author", "Sadik", "version", "1.0", "description", "Custom Actuator Example"));
    }
}
