package com.tvdgapp.repositories.User;


import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleKey(String key);

    //@Query("select new com.decagon.fellowship.dtos.user.role.ListRoleDto(r.id,r.name,r.description) from Role r WHERE r.roleType=com.decagon.fellowship.models.user.RoleType.ADMIN and r.auditSection.delF <> '1' order by r.name asc")
    @Query("select new com.tvdgapp.dtos.user.role.ListRoleDto(r.id,r.name,r.description,r.roleType,r.systemCreated) from Role r WHERE r.auditSection.delF <> '1' order by r.name asc")
    Collection<ListRoleDto> listRoles();

    @Query("select r from Role r " +
            "left join fetch r.permissions p  " +
            "where r.id =:roleId ")
    Optional<Role> findRoleDetailById(Integer roleId);

    @Query("select new com.tvdgapp.dtos.user.role.ListRoleDto(r.id,r.name,r.description,r.roleType,r.systemCreated) " +
            "from Role r " +
            "WHERE r.roleType=com.tvdgapp.models.user.RoleType.AFFILIATE " +
//            "or r.roleType=com.tvdgapp.models.user.RoleType.GLOBAL " +
            "and r.auditSection.delF <> '1' " +
            "order by r.name asc")
    Collection<ListRoleDto> listRolesForAffiliate();

    @Query("select new com.tvdgapp.dtos.user.role.ListRoleDto(r.id,r.name,r.description,r.roleType,r.systemCreated) " +
            "from Role r " +
            "WHERE r.roleType=com.tvdgapp.models.user.RoleType.ADMIN " +
            "and r.auditSection.delF <> '1' " +
            "order by r.name asc")
    Collection<ListRoleDto> listRolesForAdmin();
    @Query("select new com.tvdgapp.dtos.user.role.ListRoleDto(r.id,r.name,r.description,r.roleType,r.systemCreated) " +
            "from Role r " +
            "WHERE r.roleType=com.tvdgapp.models.user.RoleType.CUSTOMER " +
            "and r.auditSection.delF <> '1' " +
            "order by r.name asc")
    Collection<ListRoleDto> listRolesForCustomer();

}
