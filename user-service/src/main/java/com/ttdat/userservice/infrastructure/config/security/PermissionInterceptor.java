package com.ttdat.userservice.infrastructure.config.security;

import com.ttdat.userservice.application.exception.AuthException;
import com.ttdat.userservice.application.exception.ErrorCode;
import com.ttdat.userservice.application.queries.auth.CheckPermissionQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.lang.NonNull;
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
        CheckPermissionQuery query = CheckPermissionQuery.builder()
                .path(path)
                .httpMethod(httpMethod)
                .build();
        boolean isAllowed = queryGateway.query(query, ResponseTypes.instanceOf(Boolean.class)).join();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Role authRole = (Role) authentication.getAuthorities().stream().findFirst().orElse(null);
//        if(authRole == null){
//            throw new AuthException(ErrorCode.ACCESS_DENIED);
//        }
//        boolean isAllowed = authRole.isActive() && authRole.getPermissions().stream()
//                .anyMatch(permission -> permission.getApiPath().equals(path)
//                        && permission.getHttpMethod().equals(httpMethod));
        if (!isAllowed) {
            throw new AuthException(ErrorCode.ACCESS_DENIED);
        }
        return true;
    }

}
