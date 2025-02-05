package com.ttdat.userservice;

import com.ttdat.userservice.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserServiceApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer(".env.dev"));
        springApplication.run(args);
    }


}
