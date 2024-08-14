package com.tvdgapp.services.user;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.dtos.user.permission.ListPermissionDto;
import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.dtos.user.role.RoleDetailDto;
import com.tvdgapp.dtos.user.role.RoleRequestDto;
import com.tvdgapp.exceptions.InvalidOperationException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.user.*;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.repositories.User.RoleRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.utils.UserInfoUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tvdgapp.exceptions.EntityType.ROLE;


@Service
public class RoleServiceImpl extends TvdgEntityServiceImpl<Integer, Role> implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;
    private final UserInfoUtil userInfoUtil;

    @Autowired
    public RoleServiceImpl(RoleRepository repository, PermissionService permissionService,
                           UserInfoUtil userInfoUtil) {
        super(repository);
        this.repository = repository;
        this.permissionService = permissionService;
        this.userInfoUtil = userInfoUtil;
    }

    @Override
    public Optional<Role> fetchByRoleKey(String roleKey) {
        return this.repository.findByRoleKey(roleKey);
    }

    @Override
    @Transactional
    public Role createRole(RoleRequestDto roleDto) {
        Role role = new Role();
        this.mapDtoToEntityModel(roleDto, role);
        role.setRoleType(RoleType.ADMIN);
        Collection<Permission> permissions = this.permissionService.findByPermissionIn(roleDto.getPermissions());
        permissions.forEach((role::addPermission));
        return this.saveRole(role);
    }

    @Override
    public Collection<ListRoleDto> manageRoles() {
        return this.repository.listRoles();
    }


    @Override
    public Collection<ListRoleDto> listRoles() {
        User user = (User) userInfoUtil.authenticatedUserInfo();

        if (user instanceof AdminUser) {
            return this.listRolesForAdmin();
        } else if (user instanceof AffiliateUser) {
            return this.listRolesForAffiliate();
        } else if (user instanceof CustomerUser) {
            return this.listRolesForCustomer();
        }

        // Handle additional user types if necessary
        // else if (user instanceof RiderUser) {
        //     return this.listRolesForRider();
        // }

        return Collections.emptyList();
    }

    @Override
    public Collection<ListRoleDto> listRolesForAffiliate() {
        return this.repository.listRolesForAffiliate();
    }
    @Override
    public Collection<ListRoleDto> listRolesForCustomer() {
        return this.repository.listRolesForCustomer();
    }

    @Override
    public Collection<ListRoleDto> listRolesForAdmin() {
        return this.repository.listRolesForAdmin();
    }


    @Override
    public RoleDetailDto fetchRoleDetail(Integer roleId) {
        var role = this.repository.findRoleDetailById(roleId).orElseThrow(() ->
                new ResourceNotFoundException(ROLE, String.valueOf(roleId))

        );
        //only admin type role allowed to be fetched in this method
        /*if (!isAdminRole(role.get())) {
            throw new UnAuthorizeEntityAccessException(ROLE);
        }*/
        return this.convertToDetailDto(role, PermissionType.ADMIN);
    }

    @Override
    @Transactional
    @CacheEvict(value = "adminUserAuthInfo", allEntries = true)
    public Role updateRole(Integer roleId, RoleRequestDto roleRequestDto) {

        var role = this.repository.findRoleDetailById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE, String.valueOf(roleId)));
        //only admin type role allowed to be updated in this method
       /* if (!isAdminRole(role.get())) {
            throw new UnAuthorizeEntityAccessException(ROLE, String.valueOf(roleId));
        }*/

        this.mapDtoToEntityModel(roleRequestDto, role);
        Collection<Permission> permissions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleRequestDto.getPermissions())) {
            permissions = this.permissionService.findByPermissionIn(roleRequestDto.getPermissions());
        }
        this.updateRolePermission(role, permissions);
        return this.updateRole(role);
    }

    @Override
    public void deleteRole(Integer roleId) {
        Optional<Role> optional = this.repository.findById(roleId);
        optional.ifPresent(role -> {
            if (role.isSystemCreated()) {
                throw new InvalidOperationException("Not authorized to delete system-created roles.");
            }
            this.repository.delete(role);
        });
    }


    @Override
    public Collection<Role> getRoleByIds(Collection<Integer> ids) {
        try {
            return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : repository.findAllById(ids);
        } catch (Exception e) {
            //throw new ServiceException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<ListRoleDto> listRoles(String roleType) {
        if (Constants.ROLE_TYPE_AFFILIATE.equals(roleType)) {
            return this.listRolesForAffiliate();
        } else if (Constants.ROLE_TYPE_ADMIN.equals(roleType)) {
            return this.listRolesForAdmin();
        }else if (Constants.ROLE_TYPE_CUSTOMER.equals(roleType)) {
            return this.listRolesForCustomer();
        }
        return Collections.emptyList();
    }

    private void updateRolePermission(Role role, Collection<Permission> permissions) {

        Collection<Permission> definedPermissions = role.getPermissions();
        Collection<String> userRequestPermissions = permissions.stream().map(Permission::getPermission).collect(Collectors.toList());
        Collection<Permission> uncheckedList = definedPermissions.stream().filter(perm -> !userRequestPermissions.contains(perm.getPermission())).collect(Collectors.toList());

        permissions.forEach(role::addPermission);
        uncheckedList.forEach(role::removePermission);

    }

    private Role updateRole(Role role) {
        return role;
    }

    private boolean isAdminRole(Role role) {
        return role.getRoleType() != null && role.getRoleType().equals(RoleType.ADMIN);
    }

    private RoleDetailDto convertToDetailDto(Role role, PermissionType permissionType) {

        RoleDetailDto roleDetailDto = new RoleDetailDto();
        roleDetailDto.setId(role.getId());
        roleDetailDto.setName(role.getName());
        roleDetailDto.setDescription(role.getDescription());

        Collection<Permission> rolePermissions = role.getPermissions();
        Collection<Permission> allPermissions = permissionService.fetchPermissions();
        Collection<String> rolePerms = new HashSet<>();
        if (CollectionUtils.isNotEmpty(rolePermissions)) {
            rolePerms = rolePermissions.stream().map(Permission::getPermission).collect(Collectors.toSet());
        }
        boolean checked;
        String permission;
       /* for (Permission perm : allPermissions) {
            permission = perm.getPermission();
            checked = !rolePerms.isEmpty() && rolePerms.contains(permission);
            roleDetailDto.addPermission(roleDetailDto.new PermissionDTO(permission, perm.getName(), checked));
        }*/
        Map<String, ListPermissionDto> holdMap=new HashMap<>();
        for (Permission perm : allPermissions) {
            var listPermDto=holdMap.computeIfAbsent(perm.getPermissionGroup(),permGrp-> new ListPermissionDto(permGrp,new ArrayList<>()));
            listPermDto.getPermissions().add(roleDetailDto.new PermissionDTO(perm.getPermission(),perm.getName(), rolePerms.contains(perm.getPermission()),perm.getDescription()));
        }
        roleDetailDto.setPerms(holdMap.values());
        return roleDetailDto;
    }

    private Role saveRole(Role role) {
        return this.repository.save(role);
    }

    private void mapDtoToEntityModel(RoleRequestDto roleDto, Role role) {
        role.setName(roleDto.getName());
        role.setRoleKey(roleDto.getRoleKey());
        role.setDescription(roleDto.getDescription());
//        role.setRoleType(RoleType.valueOf(roleDto.getRoleType()));
    }
}
