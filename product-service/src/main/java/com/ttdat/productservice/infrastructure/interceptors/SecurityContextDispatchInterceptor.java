package com.ttdat.productservice.infrastructure.interceptors;

import jakarta.annotation.Nonnull;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SecurityContextDispatchInterceptor implements MessageDispatchInterceptor<Message<?>> {

    @Nonnull
    @Override
    public BiFunction<Integer, Message<?>, Message<?>> handle(@Nonnull List<? extends Message<?>> messages) {
        return (index, message) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                // Add Authentication to query metadata
                return message.andMetaData(Map.of("authentication", authentication));
            }
            return message;
        };
    }
}
