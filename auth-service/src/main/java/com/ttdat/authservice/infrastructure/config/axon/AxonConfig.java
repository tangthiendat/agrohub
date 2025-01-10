package com.ttdat.authservice.infrastructure.config.axon;

import com.ttdat.authservice.application.errorhandler.PermissionGroupErrorHandler;
import org.axonframework.config.ConfigurerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public ConfigurerModule processingGroupErrorHandlingConfigurerModule() {
        return configurer -> configurer.eventProcessing(processingConfigurer ->
                processingConfigurer
                        .registerListenerInvocationErrorHandler(
                                "permission-group",
                                conf -> new PermissionGroupErrorHandler()
                        )
        );
    }

}
