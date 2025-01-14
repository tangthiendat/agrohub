package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.api.dto.response.PaginationMeta;
import com.ttdat.authservice.api.dto.response.UserPageResult;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.application.queries.user.GetUserByEmailQuery;
import com.ttdat.authservice.application.queries.user.GetUserPageQuery;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.repositories.UserRepository;
import com.ttdat.authservice.infrastructure.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserQueryHandler {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FilterUtils filterUtils;

    @QueryHandler
    public UserDTO handle(GetUserByEmailQuery query){
        User user = userRepository.findByEmail(query.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @QueryHandler
    public UserPageResult handle(GetUserPageQuery query){
        List<Sort.Order> sortOrders = filterUtils.toSortOrders(query.getSortParams());
        int page = query.getPaginationParams().getPage();
        int pageSize = query.getPaginationParams().getPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(pageable);
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .page(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .build();
        return UserPageResult.builder()
                .meta(paginationMeta)
                .content(userMapper.toDTOs(userPage.getContent()))
                .build();
    }
}
