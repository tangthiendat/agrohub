package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.exception.DuplicateResourceException;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.user.UserCreatedEvent;
import com.ttdat.authservice.domain.events.user.UserStatusUpdatedEvent;
import com.ttdat.authservice.domain.events.user.UserUpdatedEvent;
import com.ttdat.authservice.domain.repositories.RoleRepository;
import com.ttdat.authservice.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-group")
public class UserEventHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    @EventHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        if (userRepository.existsByEmail(userCreatedEvent.getEmail())) {
            throw new DuplicateResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = userMapper.toEntity(userCreatedEvent);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    @EventHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {
        User user = userRepository.findById(userUpdatedEvent.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateEntityFromEvent(user, userUpdatedEvent);
        userRepository.save(user);
    }

    @Transactional
    @EventHandler
    public void on(UserStatusUpdatedEvent userStatusUpdatedEvent){
        User user = userRepository.findById(userStatusUpdatedEvent.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        user.setActive(userStatusUpdatedEvent.isActive());
        userRepository.save(user);
    }
}
