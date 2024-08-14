package com.tvdgapp.services.user;

import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.dtos.user.role.RoleDetailDto;
import com.tvdgapp.dtos.user.role.RoleRequestDto;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.Collection;
import java.util.Optional;


public interface RoleService  extends TvdgEntityService<Integer, Role> {
    Optional<Role> fetchByRoleKey(String roleKey);

    Role createRole(RoleRequestDto roleRequestDto);

    Collection<ListRoleDto> manageRoles();

    Collection<ListRoleDto> listRoles();

    Collection<ListRoleDto> listRolesForAffiliate();
    Collection<ListRoleDto> listRolesForCustomer();

    Collection<ListRoleDto> listRolesForAdmin();

    RoleDetailDto fetchRoleDetail(Integer roleId);

    Role updateRole(Integer roleId, RoleRequestDto roleRequestDto);

    void deleteRole(Integer roleId);

    Collection<Role> getRoleByIds(Collection<Integer> ids);

    Collection<ListRoleDto> listRoles(String roleType);
}
