package com.ttdat.core.api.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRole implements GrantedAuthority {
    String roleName;
    List<AuthPermission> permissions;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
