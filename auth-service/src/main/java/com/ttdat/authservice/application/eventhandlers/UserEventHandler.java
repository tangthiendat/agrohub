package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.UserCreatedEvent;
import com.ttdat.authservice.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-group")
public class UserEventHandler {
    private final UserService userService;

    @EventHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        User user = User.builder()
                .userId(userCreatedEvent.getUserId())
                .fullName(userCreatedEvent.getFullName())
                        .gender(userCreatedEvent.getGender())
                        .email(userCreatedEvent.getEmail())
                        .password(userCreatedEvent.getPassword())
                        .phoneNumber(userCreatedEvent.getPhoneNumber())
                        .role(Role.builder().roleId(userCreatedEvent.getRoleId()).build())
                        .build();
        userService.createUser(user);
    }
}
