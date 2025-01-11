package com.ttdat.authservice.application.queries.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ttdat.authservice.api.dto.request.PaginationParams;
import com.ttdat.authservice.api.dto.request.SortParams;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GetRolePageQuery {
    PaginationParams paginationParams;
    SortParams sortParams;
    Map<String, String> filterParams;

    @JsonCreator
    public GetRolePageQuery(@JsonProperty("paginationParams") PaginationParams paginationParams,
                            @JsonProperty("sortParams") SortParams sortParams,
                            @JsonProperty("filterParams") Map<String, String> filterParams) {
        this.paginationParams = paginationParams;
        this.sortParams = sortParams;
        this.filterParams = filterParams;
    }
}
