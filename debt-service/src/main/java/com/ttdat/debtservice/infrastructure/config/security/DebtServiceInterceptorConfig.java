package com.ttdat.debtservice.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class DebtServiceInterceptorConfig implements WebMvcConfigurer {
    private final PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whitelist = new String[]{
                "/api/v1/payment-methods",
        };
        registry.addInterceptor(permissionInterceptor)
                .excludePathPatterns(whitelist);
    }
}
