package com.ttdat.authservice.infrastructure.config.security;

import com.ttdat.authservice.application.exception.AuthException;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler)  {
        String contextPath = request.getContextPath();
        String path = contextPath + request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String httpMethod = request.getMethod();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email != null){
            Optional<User> optionalUser = userRepository.findByEmail(email);
            optionalUser.ifPresent(user -> checkPermission(user, path, httpMethod));
        }
        return true;
    }

    private void checkPermission(User user, String path, String httpMethod) {
        Role role = user.getRole();
        if (role != null) {
            boolean hasPermission = role.getPermissions().stream()
                    .anyMatch(permission -> permission.getApiPath().equals(path)
                            && permission.getHttpMethod().equals(httpMethod));
            boolean isAllowed = hasPermission && role.isActive();
            if (!isAllowed) {
                throw new AuthException(ErrorCode.ACCESS_DENIED);
            }
        } else {
            throw new AuthException(ErrorCode.ACCESS_DENIED);
        }
    }
}
