package com.tvdgapp.repositories.User;


import com.tvdgapp.models.user.Permission;
import com.tvdgapp.models.user.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface PermissionRepository  extends JpaRepository<Permission,Integer> {
    Optional<Permission> findByPermission(String permKey);

    Collection<Permission> findByPermissionIn(Collection<String> names);

    Collection<Permission> findAllByPermissionType(PermissionType permissionType);

    @Query("SELECT p FROM Permission p JOIN FETCH p.roles WHERE p.id = :id")
    Optional<Permission> findByIdWithRoles(@Param("id") Integer id);
}
