package com.ttdat.core.api.dto.response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class AuthPermission {
    String apiPath;
    String httpMethod;
}
