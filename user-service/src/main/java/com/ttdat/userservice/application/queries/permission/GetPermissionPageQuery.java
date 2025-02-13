package com.ttdat.userservice.application.queries.permission;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ttdat.core.api.dto.request.PaginationParams;
import com.ttdat.core.api.dto.request.SortParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class GetPermissionPageQuery {
    PaginationParams paginationParams;
    SortParams sortParams;
    Map<String, String> filterParams;
}
