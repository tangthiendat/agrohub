package com.ttdat.userservice.application.handlers.query;

import com.ttdat.core.api.dto.response.PaginationMeta;
import com.ttdat.core.application.exceptions.ErrorCode;
import com.ttdat.core.application.exceptions.ResourceNotFoundException;
import com.ttdat.userservice.api.dto.common.UserDTO;
import com.ttdat.userservice.api.dto.response.UserPageResult;
import com.ttdat.userservice.application.mappers.UserMapper;
import com.ttdat.userservice.application.queries.user.GetUserByEmailQuery;
import com.ttdat.userservice.application.queries.user.GetUserByIdQuery;
import com.ttdat.userservice.application.queries.user.GetUserPageQuery;
import com.ttdat.userservice.domain.entities.User;
import com.ttdat.userservice.domain.repositories.UserRepository;
import com.ttdat.userservice.infrastructure.utils.PaginationUtils;
import com.ttdat.userservice.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserQueryHandler {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @QueryHandler
    public UserDTO handle(GetUserByEmailQuery getUserByEmailQuery){
        User user = userRepository.findByEmail(getUserByEmailQuery.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMAIL_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @QueryHandler
    public UserDTO handle(GetUserByIdQuery getUserByIdQuery){
        User user = userRepository.findById(UUID.fromString(getUserByIdQuery.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    @QueryHandler
    public UserPageResult handle(GetUserPageQuery getUserPageQuery){
        Pageable pageable = PaginationUtils.getPageable(getUserPageQuery.getPaginationParams(), getUserPageQuery.getSortParams());
        Specification<User> userPageSpec = getUserPageSpec(getUserPageQuery.getFilterParams());
        Page<User> userPage = userRepository.findAll(userPageSpec, pageable);
        PaginationMeta paginationMeta = PaginationUtils.getPaginationMeta(userPage);
        return UserPageResult.builder()
                .meta(paginationMeta)
                .content(userMapper.toDTOs(userPage.getContent()))
                .build();
    }

    private Specification<User> getUserPageSpec(Map<String, String> filterParams){
        Specification<User> userPageSpec = Specification.where(null);
        userPageSpec = userPageSpec.and(SpecificationUtils.buildSpecification(filterParams, "fullName", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "gender", String.class))
                .and(SpecificationUtils.buildSpecification(filterParams, "active", Boolean.class))
                .and(SpecificationUtils.buildJoinSpecification(filterParams, "role", "roleName", String.class));
        if (filterParams.containsKey("query")){
            String searchValue = filterParams.get("query").toLowerCase();
            Specification<User> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern),
                        criteriaBuilder.like(root.get("email"), likePattern),
                        criteriaBuilder.like(root.get("phoneNumber"), likePattern)
                );
            };
            userPageSpec = userPageSpec.and(querySpec);
        }
        return userPageSpec;
    }
}
