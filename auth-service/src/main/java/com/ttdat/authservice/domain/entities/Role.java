package com.ttdat.authservice.domain.entities;

import com.ttdat.authservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends Auditable implements GrantedAuthority {

    @Id
    Long roleId;

    String roleName;

    boolean active;

    String description;

    @ManyToMany
    @JoinTable(name = "permission_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    List<Permission> permissions;

    @OneToMany(mappedBy = "role")
    List<User> users;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
