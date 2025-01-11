package com.ttdat.authservice.application.eventhandlers;

import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.events.user.UserCreatedEvent;
import com.ttdat.authservice.domain.events.user.UserUpdatedEvent;
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
    private final UserMapper userMapper;

    @EventHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        User user = userMapper.toUser(userCreatedEvent);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @EventHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {
        User user = userRepository.findById(userUpdatedEvent.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUserFromEvent(user, userUpdatedEvent);
        userRepository.save(user);
    }
}
