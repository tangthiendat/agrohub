package com.ttdat.authservice;

import com.ttdat.authservice.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AuthServiceApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer());
        springApplication.run(args);
    }

}
