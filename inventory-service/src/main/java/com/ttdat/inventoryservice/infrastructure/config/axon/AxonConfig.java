package com.ttdat.inventoryservice.infrastructure.config.axon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.axonframework.axonserver.connector.AxonServerConfiguration;
import org.axonframework.axonserver.connector.AxonServerConnectionManager;
import org.axonframework.axonserver.connector.query.AxonServerQueryBus;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

//    @Bean
//    public ConfigurerModule errorHandlingConfigurerModule() {
//        return configurer -> configurer.eventProcessing(processingConfigurer ->
//                        processingConfigurer
//                                .registerDefaultListenerInvocationErrorHandler(
//                                        conf -> new PurchaseServiceEventErrorHandler()
//                                )
//        );
//    }

    @Bean
    public Serializer messageSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return JacksonSerializer.builder()
                .objectMapper(objectMapper)
                .build();
    }

    @Bean
    public QueryBus queryBus(AxonServerConnectionManager axonServerConnectionManager,
                             AxonServerConfiguration axonServerConfiguration,
                             QueryUpdateEmitter queryUpdateEmitter) {
        return AxonServerQueryBus.builder()
                .configuration(axonServerConfiguration)
                .axonServerConnectionManager(axonServerConnectionManager)
                .messageSerializer(messageSerializer())
                .genericSerializer(messageSerializer())
                .updateEmitter(queryUpdateEmitter)
                .localSegment(SimpleQueryBus.builder()
                        .queryUpdateEmitter(queryUpdateEmitter)
                        .build())
                .build();
    }

    @Bean
    public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 100);
    }

}
