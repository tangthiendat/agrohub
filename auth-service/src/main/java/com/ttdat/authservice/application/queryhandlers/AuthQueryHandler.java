package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.application.queries.auth.CheckPermissionQuery;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class AuthQueryHandler {
    private final UserRepository userRepository;

    @QueryHandler
    public boolean handle(CheckPermissionQuery query){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email != null) {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Role role = user.getRole();
                if (role != null) {
                    return role.getPermissions().stream()
                            .anyMatch(permission -> permission.getApiPath().equals(query.getPath())
                                    && permission.getHttpMethod().equals(query.getHttpMethod())) && role.isActive();
                }
            }
        }
        return false;
    }
}
