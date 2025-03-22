package com.ttdat.userservice.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttdat.userservice.infrastructure.audit.Auditable;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long permissionId;

    @Column(length = 100, nullable = false)
    String permissionName;

    String description;

    @Column(length = 100, nullable = false)
    String apiPath;

    @Column(length = 10, nullable = false)
    String httpMethod;

    @Column(length = 50, nullable = false)
    String module;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    List<Role> roles;
}
