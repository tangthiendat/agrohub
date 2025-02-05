package com.ttdat.productservice;

import com.ttdat.productservice.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ProductServiceApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer(".env.dev"));
        springApplication.run(args);
    }

}
