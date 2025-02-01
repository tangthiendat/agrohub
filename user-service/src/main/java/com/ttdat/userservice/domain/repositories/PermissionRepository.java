package com.ttdat.userservice.domain.repositories;

import com.ttdat.userservice.domain.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    @Query("SELECT CASE WHEN COUNT(r.roleId) > 0 THEN true ELSE false END FROM Role r JOIN r.permissions p WHERE p.permissionId = :permissionId")
    boolean isPermissionInUse(@Param("permissionId") Long permissionId);
}
