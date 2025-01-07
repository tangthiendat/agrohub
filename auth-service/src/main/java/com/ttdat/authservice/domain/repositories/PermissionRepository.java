package com.ttdat.authservice.domain.repositories;

import com.ttdat.authservice.domain.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
