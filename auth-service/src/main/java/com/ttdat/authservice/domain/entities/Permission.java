package com.ttdat.authservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    Long permissionId;

    String permissionName;

    String description;

    String apiPath;

    String httpMethod;

    String module;
}
