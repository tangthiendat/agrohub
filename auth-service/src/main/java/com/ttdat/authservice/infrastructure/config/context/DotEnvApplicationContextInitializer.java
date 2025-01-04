package com.ttdat.authservice.infrastructure.config.context;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotEnvApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Dotenv dotenv = Dotenv.load();
        Map<String, Object> dotenvMap = new HashMap<>();

        dotenv.entries().forEach(dotenvEntry -> dotenvMap.put(dotenvEntry.getKey(), dotenvEntry.getValue()));

        // Add these properties to the Spring Environment
        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
    }

}
