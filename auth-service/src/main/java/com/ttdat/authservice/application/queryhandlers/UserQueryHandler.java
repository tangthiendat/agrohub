package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.UserDTO;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.application.queries.FindUserByEmailQuery;
import com.ttdat.authservice.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryHandler {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @QueryHandler
    public UserDTO handle(FindUserByEmailQuery query){
        return userRepository.findByEmail(query.getEmail())
                .map(userMapper::toUserDTO)
                .orElse(null);
    }
}
