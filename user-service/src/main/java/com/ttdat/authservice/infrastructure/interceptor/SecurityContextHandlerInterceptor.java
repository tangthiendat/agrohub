package com.ttdat.authservice.infrastructure.interceptor;

import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nonnull;

public class SecurityContextHandlerInterceptor implements MessageHandlerInterceptor<Message<?>> {
    @Override
    public Object handle(@Nonnull UnitOfWork<? extends Message<?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) throws Exception {
        Message<?> message = unitOfWork.getMessage();
        if (message.getMetaData().containsKey("authentication")) {
            Authentication authentication = (Authentication) message.getMetaData().get("authentication");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        try {
            return interceptorChain.proceed();
        }
        finally {
            SecurityContextHolder.clearContext();
        }
    }
}
