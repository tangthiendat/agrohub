package com.ttdat.authservice.infrastructure.config.security;

import com.ttdat.authservice.application.exception.AuthException;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.queries.auth.CheckPermissionQuery;
import com.ttdat.authservice.domain.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final QueryGateway queryGateway;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)  {
        String contextPath = request.getContextPath();
        String path = contextPath + request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        CheckPermissionQuery query = CheckPermissionQuery.builder()
                .path(path)
                .httpMethod(httpMethod)
                .build();
        boolean isAllowed = queryGateway.query(query, ResponseTypes.instanceOf(Boolean.class)).join();
        if (!isAllowed) {
            throw new AuthException(ErrorCode.ACCESS_DENIED);
        }
        return true;
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        if(email != null) {
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                checkPermission(user, path, httpMethod);
//            }
//        } else {
//            throw new AuthException(ErrorCode.ACCESS_DENIED);
//        }
    }

//    private void checkPermission(User user, String path, String httpMethod) {
//        Role role = user.getRole();
//        if (role != null) {
//            boolean hasPermission = role.getPermissions().stream()
//                    .anyMatch(permission -> permission.getApiPath().equals(path)
//                            && permission.getHttpMethod().equals(httpMethod));
//            boolean isAllowed = hasPermission && role.isActive();
//            if (!isAllowed) {
//                throw new AuthException(ErrorCode.ACCESS_DENIED);
//            }
//        } else {
//            throw new AuthException(ErrorCode.ACCESS_DENIED);
//        }
//    }
}
