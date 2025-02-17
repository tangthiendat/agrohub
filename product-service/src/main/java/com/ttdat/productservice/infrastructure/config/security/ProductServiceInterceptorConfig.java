package com.ttdat.productservice.infrastructure.config.security;

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
         registry.addInterceptor(permissionInterceptor);
    }
}
