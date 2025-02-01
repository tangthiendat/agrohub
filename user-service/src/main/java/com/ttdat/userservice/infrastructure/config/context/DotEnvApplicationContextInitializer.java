package com.ttdat.userservice.infrastructure.config.context;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DotEnvApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private final String dotenvFile;

    public DotEnvApplicationContextInitializer(String dotenvFile) {
        this.dotenvFile = dotenvFile;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Dotenv dotenv = Dotenv.configure().filename(dotenvFile).load();
            Map<String, Object> dotenvMap = new HashMap<>();

            dotenv.entries().forEach(dotenvEntry -> dotenvMap.put(dotenvEntry.getKey(), dotenvEntry.getValue()));

            // Add these properties to the Spring Environment
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));
        } catch (DotenvException e) {
            log.warn("Failed to load .env.dev file for \"dev\" profile. If you use another profile, ignore this warning.");
        }
    }

}
