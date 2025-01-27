package com.ttdat.authservice.application.queryhandlers;

import com.ttdat.authservice.api.dto.common.UserDTO;
import com.ttdat.authservice.api.dto.request.FilterCriteria;
import com.ttdat.authservice.api.dto.response.PaginationMeta;
import com.ttdat.authservice.api.dto.response.UserPageResult;
import com.ttdat.authservice.application.exception.ErrorCode;
import com.ttdat.authservice.application.exception.ResourceNotFoundException;
import com.ttdat.authservice.application.mappers.UserMapper;
import com.ttdat.authservice.application.queries.user.GetUserByEmailQuery;
import com.ttdat.authservice.application.queries.user.GetUserByIdQuery;
import com.ttdat.authservice.application.queries.user.GetUserPageQuery;
import com.ttdat.authservice.domain.entities.Role;
import com.ttdat.authservice.domain.entities.User;
import com.ttdat.authservice.domain.repositories.UserRepository;
import com.ttdat.authservice.infrastructure.utils.FilterUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserQueryHandler {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FilterUtils filterUtils;

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
        List<Sort.Order> sortOrders = filterUtils.toSortOrders(getUserPageQuery.getSortParams());
        int page = getUserPageQuery.getPaginationParams().getPage();
        int pageSize = getUserPageQuery.getPaginationParams().getPageSize();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        Specification<User> userPageSpec = getUserPageSpec(getUserPageQuery.getFilterParams());
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(userPageSpec, pageable);
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

    private Specification<User> getUserPageSpec(Map<String, String> filterParams){
        Specification<User> userPageSpec = Specification.where(null);
        if(filterParams.containsKey("gender")){
            List<FilterCriteria> genderCriteria = filterUtils.getFilterCriteriaList(filterParams, "gender");
            Specification<User> genderSpec = genderCriteria.stream()
                    .map(criteria -> (Specification<User>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("gender"), criteria.getValue()))
                    .reduce(Specification::or)
                    .orElse(Specification.where(null));
            userPageSpec = userPageSpec.and(genderSpec);
        }
        if(filterParams.containsKey("active")){
            List<FilterCriteria> activeCriteria = filterUtils.getFilterCriteriaList(filterParams, "active");
            Specification<User> activeSpec = activeCriteria.stream()
                    .map(criteria -> (Specification<User>) (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("active"), Boolean.parseBoolean((String) criteria.getValue())))
                    .reduce(Specification::or)
                    .orElse(Specification.where(null));
            userPageSpec = userPageSpec.and(activeSpec);
        }
        if(filterParams.containsKey("role")){
            List<FilterCriteria> roleCriteria = filterUtils.getFilterCriteriaList(filterParams, "role");
            Specification<User> roleSpec = roleCriteria.stream()
                    .map(criteria -> (Specification<User>) (root, query, criteriaBuilder) -> {
                        Join<User, Role> roleJoin = root.join("role", JoinType.INNER);
                        return criteriaBuilder.equal(roleJoin.get("roleName"), criteria.getValue());
                    })
                    .reduce(Specification::or)
                    .orElse(Specification.where(null));
            userPageSpec = userPageSpec.and(roleSpec);
        }
        if (filterParams.containsKey("query")){
            String searchValue = filterParams.get("query").toLowerCase();
            Specification<User> querySpec = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + searchValue + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("fullName"))),
                                likePattern),
                        criteriaBuilder.like(root.get("email"), likePattern),
                        criteriaBuilder.like(root.get("phoneNumber"), likePattern)
                );
            };
            userPageSpec = userPageSpec.and(querySpec);
        }
        return userPageSpec;
    }
}
