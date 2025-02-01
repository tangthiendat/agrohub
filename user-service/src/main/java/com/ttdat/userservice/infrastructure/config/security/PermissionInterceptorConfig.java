package com.ttdat.userservice.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class PermissionInterceptorConfig implements WebMvcConfigurer {
    private final PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       String[] whiteList = {
        "/api/v1/auth/**",
        "/api/v1/users/me"
       };
         registry.addInterceptor(permissionInterceptor)
                .excludePathPatterns(whiteList);
    }
}
