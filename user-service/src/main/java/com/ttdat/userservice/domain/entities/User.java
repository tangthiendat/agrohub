package com.ttdat.userservice.domain.entities;

import com.ttdat.core.domain.entities.Gender;
import com.ttdat.userservice.infrastructure.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends Auditable implements UserDetails {
    @Id
    UUID userId;

    @Column(length = 100)
    String fullName;

    LocalDate dob;

    @Enumerated(EnumType.STRING)
            @Column(length = 10)
    Gender gender;

    boolean active;

    Long warehouseId;

    String email;

    String password;

    @Column(length = 20)
    String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
