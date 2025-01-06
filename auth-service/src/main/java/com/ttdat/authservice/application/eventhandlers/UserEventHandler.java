package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.UserCreatedEvent;
import com.ttdat.authservice.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-group")
public class UserEventHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @EventHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        User user = User.builder()
                .userId(userCreatedEvent.getUserId())
                .fullName(userCreatedEvent.getFullName())
                        .gender(userCreatedEvent.getGender())
                        .email(userCreatedEvent.getEmail())
                        .password(passwordEncoder.encode(userCreatedEvent.getPassword()))
                        .phoneNumber(userCreatedEvent.getPhoneNumber())
                        .role(Role.builder().roleId(userCreatedEvent.getRoleId()).build())
                        .build();
        userRepository.save(user);
    }
}
