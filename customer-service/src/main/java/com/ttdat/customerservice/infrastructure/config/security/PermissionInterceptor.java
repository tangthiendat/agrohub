package com.ttdat.customerservice.infrastructure.config.security;

import com.ttdat.core.application.exceptions.AuthException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.queries.auth.CheckPermissionQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    private final QueryGateway queryGateway;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)  {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        CheckPermissionQuery query = CheckPermissionQuery.builder()
                .userId(userId)
                .apiPath(path)
                .httpMethod(httpMethod)
                .build();
        boolean isAllowed = queryGateway.query(query, ResponseTypes.instanceOf(Boolean.class)).join();
        if (!isAllowed) {
            throw new AuthException(ErrorCode.ACCESS_DENIED);
        }
        return true;
    }

}
