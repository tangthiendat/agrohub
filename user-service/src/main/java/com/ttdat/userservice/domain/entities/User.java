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
    @Column(length = 50)
    String userId;

    @Column(length = 100, nullable = false)
    String fullName;

    @Column(nullable = false)
    LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    Gender gender;

    boolean active;

    @Column(nullable = false)
    Long warehouseId;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(length = 20, nullable = false)
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
