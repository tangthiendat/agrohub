package com.ttdat.inventoryservice.infrastructure.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Slf4j
public class MetadataDispatchInterceptor implements MessageDispatchInterceptor<Message<?>> {

    @Nonnull
    @Override
    public BiFunction<Integer, Message<?>, Message<?>> handle(@Nonnull List<? extends Message<?>> messages) {
        return (index, message) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Jwt jwt = (Jwt) authentication.getCredentials();
                Long warehouseId = Long.parseLong(jwt.getClaimAsString("warehouse_id"));
                String userId = jwt.getSubject();
                return message.andMetaData(Map.of("warehouse_id", warehouseId,
                        "user_id", userId));
            }
            return message;
        };
    }
}
