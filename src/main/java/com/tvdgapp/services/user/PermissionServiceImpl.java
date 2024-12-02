package com.tvdgapp.services.user;


import com.tvdgapp.dtos.user.permission.ListPermissionDto;
import com.tvdgapp.dtos.user.permission.PermissionDto;
import com.tvdgapp.dtos.user.permission.PermissionRequestDto;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.PermissionMapper;
import com.tvdgapp.models.user.Permission;
import com.tvdgapp.models.user.PermissionType;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.repositories.User.PermissionRepository;
import com.tvdgapp.repositories.User.RoleRepository;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends TvdgEntityServiceImpl<Integer, Permission> implements PermissionService {

    private final PermissionRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PermissionMapper permissionMapper;


    @Autowired
    public PermissionServiceImpl(PermissionRepository repository, UserRepository userRepository, RoleRepository roleRepository, PermissionMapper permissionMapper) {
        super(repository);
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionMapper = permissionMapper;
    }

    @Cacheable
    @Override
    public Optional<Permission> findPermByKey(String permKey) {
        return this.repository.findByPermission(permKey);
    }

    @Override
    public Collection<Permission> fetchPermissions() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Permission> fetchByPermission(String permission) {
        return this.repository.findByPermission(permission);
    }

    @Override
    public Collection<Permission> findByPermissionIn(Collection<String> names) {
        return CollectionUtils.isEmpty(names) ? Collections.emptyList() : repository.findByPermissionIn(names);
    }

//    @Override
//    public Collection<Permission> fetchPermissions(PermissionType permissionType) {
//        return this.repository.findAllByPermissionType(permissionType);
//    }

    @Transactional
    public Permission createPermission(@Valid PermissionRequestDto permissionDTO) {
        validatePermission(permissionDTO);

        if (repository.findByPermission(permissionDTO.getPermission()).isPresent()) {
            throw new IllegalArgumentException("Permission with the same key already exists");
        }

        Permission newPermission = new Permission();
        newPermission.setPermission(permissionDTO.getPermission());
        newPermission.setName(permissionDTO.getName());
        newPermission.setDescription(permissionDTO.getDescription());
//        newPermission.setPermissionType(permissionDTO.getPermissionType());
//        newPermission.setPermissionGroup(permissionDTO.getPermissionGroup());

        return repository.save(newPermission);
    }

    @Transactional
    public PermissionRequestDto updatePermission(Integer permissionId, @Valid PermissionRequestDto permissionDTO) {
        validatePermission(permissionDTO);

        // Retrieve the existing Permission entity
        Permission permission = repository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", String.valueOf(permissionId)));

        // Update the Permission entity with new data
        permission.setPermission(permissionDTO.getPermission());
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());
//        permission.setPermissionType(permissionDTO.getPermissionType());
//        permission.setPermissionGroup(permissionDTO.getPermissionGroup());

        // Save the updated Permission entity
        Permission updatedPermission = repository.save(permission);

        // Convert the updated Permission entity to DTO
        PermissionRequestDto updatedPermissionDTO = permissionMapper.convertToDTO(updatedPermission);

        return updatedPermissionDTO;
    }
    @Transactional
    public void deletePermission(Integer permissionId) {
        Permission permission = repository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        repository.delete(permission);
    }

    @Transactional
    public void assignPermissionToRole(Integer permissionId, Integer roleId) {
        Permission permission = repository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.addPermission(permission);
        roleRepository.save(role);
    }

    private void validatePermission(PermissionRequestDto permissionDTO) {
        if (permissionDTO.getPermission() == null || permissionDTO.getPermission().isEmpty()) {
            throw new ConstraintViolationException("Permission key must not be null or empty", null);
        }
        if (permissionDTO.getName() == null || permissionDTO.getName().isEmpty()) {
            throw new ConstraintViolationException("Permission name must not be null or empty", null);
        }
        if (permissionDTO.getPermissionType() == null) {
            throw new ConstraintViolationException("Permission type must not be null", null);
        }
        if (permissionDTO.getPermissionGroup() != null && permissionDTO.getPermissionGroup().length() > 50) {
            throw new ConstraintViolationException("Permission group length must be less than or equal to 50", null);
        }
    }


   /* @Override
    public Collection<ListPermissionDto> listPermissions() {

        Collection<Permission> permissions=this.fetchPermissions();
        if(!CollectionUtils.isEmpty(permissions)){
            return this.convertEntityToDtos(permissions);
        }
        return Collections.emptyList();
    }

    private Collection<PermissionDto> convertEntityToDtos(Collection<Permission> permissions) {
        return permissions.stream().map((p)->new PermissionDto(p.getPermission(),p.getName(),p.getDescription())).collect(Collectors.toList());
    }*/


    @Override
    public Collection<ListPermissionDto> listPermissions() {
        Collection<Permission> permissions=this.fetchPermissions();
        if(!CollectionUtils.isEmpty(permissions)){
            return this.convertEntityToDtos(permissions);
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<String> listPermissions(String email) {
        var adminUser=this.userRepository.findAuthUserByEmail(email);
        if(adminUser.isEmpty()){
            return Collections.emptyList();
        }
        Set<String> authorities=new HashSet<>();
       var roles=adminUser.get().getRoles();
       for(var role:roles){
           authorities.addAll(role.getPermissions().stream().map(Permission::getPermission).collect(Collectors.toList()));
       }
       return authorities;
    }

    private Collection<ListPermissionDto> convertEntityToDtos(Collection<Permission> permissions) {
        Map<String,ListPermissionDto> holdMap=new HashMap<>();
        permissions.forEach(perm -> {
            var listPermDto=holdMap.computeIfAbsent(perm.getPermission(),permGrp-> new ListPermissionDto(permGrp,new ArrayList<>()));
            listPermDto.getPermissions().add(new PermissionDto(perm.getPermission(),perm.getName(),perm.getDescription()));
        });
        return holdMap.values();
    }


}
