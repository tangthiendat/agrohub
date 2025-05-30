package com.ttdat.purchaseservice.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@RequiredArgsConstructor
public class ProductServiceInterceptorConfig implements WebMvcConfigurer {
    private final PermissionInterceptor permissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/api/v1/suppliers/search",
        };
         registry.addInterceptor(permissionInterceptor)
         .excludePathPatterns(whiteList);
    }
}
