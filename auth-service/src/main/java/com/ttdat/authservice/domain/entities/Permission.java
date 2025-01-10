package com.ttdat.authservice.domain.entities;

import com.ttdat.authservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends Auditable {
    @Id
    Long permissionId;

    String permissionName;

    String description;

    String apiPath;

    String httpMethod;

    String module;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    List<Role> roles;
}
