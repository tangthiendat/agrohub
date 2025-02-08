package com.ttdat.userservice.application.handlers.event;

import com.ttdat.core.application.exceptions.DuplicateResourceException;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.application.constants.RedisKeys;
import com.ttdat.userservice.application.mappers.UserMapper;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.events.user.UserCreatedEvent;
import com.ttdat.userservice.domain.events.user.UserStatusUpdatedEvent;
import com.ttdat.userservice.domain.events.user.UserUpdatedEvent;
import com.ttdat.userservice.domain.repositories.UserRepository;
import com.ttdat.userservice.infrastructure.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-group")
public class UserEventHandler {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RedisService redisService;

    private void deleteUsersRoleCache() {
        redisService.deleteWithPattern(RedisKeys.USER_PREFIX + ":*:role");
    }

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

    private User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    @EventHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {
        User user = getUserById(userUpdatedEvent.getUserId());
        userMapper.updateEntityFromEvent(user, userUpdatedEvent);
        userRepository.save(user);
        deleteUsersRoleCache();
    }

    @Transactional
    @EventHandler
    public void on(UserStatusUpdatedEvent userStatusUpdatedEvent){
        User user = getUserById(userStatusUpdatedEvent.getUserId());
        user.setActive(userStatusUpdatedEvent.isActive());
        userRepository.save(user);
        deleteUsersRoleCache();
    }
}
