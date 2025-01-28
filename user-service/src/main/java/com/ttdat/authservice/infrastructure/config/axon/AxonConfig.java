package com.ttdat.authservice.infrastructure.config.axon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ttdat.authservice.application.errorhandler.AuthServiceEventErrorHandler;
import com.ttdat.authservice.infrastructure.interceptor.SecurityContextDispatchInterceptor;
import com.ttdat.authservice.infrastructure.interceptor.SecurityContextHandlerInterceptor;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {


    @Bean
    public ConfigurerModule errorHandlingConfigurerModule() {
        return configurer -> configurer.eventProcessing(processingConfigurer ->
                processingConfigurer
                        .registerDefaultListenerInvocationErrorHandler(
                                conf -> new AuthServiceEventErrorHandler()
                        )
//                        .registerListenerInvocationErrorHandler(
//                                "permission-group",
//                                conf -> new PermissionGroupErrorHandler()
//                        )
        );
    }

    @Bean
    public Serializer messageSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Handle Java 8 Date/Time types
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return JacksonSerializer.builder()
                .objectMapper(objectMapper)
                .build();
    }

    @Bean
    public QueryBus queryBus() {
        SimpleQueryBus queryBus = SimpleQueryBus.builder().build();
        queryBus.registerDispatchInterceptor(new SecurityContextDispatchInterceptor());
        queryBus.registerHandlerInterceptor(new SecurityContextHandlerInterceptor());
        return queryBus;
    }

}
