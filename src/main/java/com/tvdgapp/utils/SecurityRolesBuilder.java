package com.tvdgapp.utils;

import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.RoleType;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for building security roles and permissions
 */
public class SecurityRolesBuilder {
	
	private final List<Role> roles = new ArrayList<>();
	@Nullable
	private Role lastRole = null;
	
	
	public SecurityRolesBuilder addRole(String name,String roleKey, RoleType type) {
		return this.add(name, roleKey, type);
	}

	public SecurityRolesBuilder addRole(String name, RoleType type) {
		return this.add(name,"", type);
	}

	private SecurityRolesBuilder add(String name,String roleKey, RoleType type) {

		Role role = new Role();
		role.setName(name);
		role.setRoleKey(roleKey);
		role.setRoleType(type);
		role.setSystemCreated(true);
		roles.add(role);
		this.lastRole = role;

		return this;
	}

	public SecurityRolesBuilder addPermission(String name) {
		if(this.lastRole == null) {
			Role role = this.roles.get(0);
			if(role == null) {
				role = new Role();
			    role.setName("UNDEFINED");
				role.setRoleType(RoleType.ADMIN);
				role.setSystemCreated(true);
				roles.add(role);
				this.lastRole = role;
			}
		}

		if(lastRole!=null) {
			com.tvdgapp.models.user.Permission permission = new com.tvdgapp.models.user.Permission();
			permission.setName(name);
			lastRole.addPermission(permission);
		}
		
		return this;
	}
	
	public SecurityRolesBuilder addPermission(com.tvdgapp.models.user.Permission permission) {
		
		if(this.lastRole == null) {
			Role role = this.roles.get(0);
			if(role == null) {
				role = new Role();
				role.setName("UNDEFINED");
				role.setRoleType(RoleType.ADMIN);
				role.setSystemCreated(true);
				roles.add(role);
				this.lastRole = role;
			}
		}
		if(lastRole!=null) {
			lastRole.addPermission(permission);
		}
		return this;
	}
	
	public List<Role> build() {
		return roles;
	}

	public @Nullable Role getLastRole(){
		return lastRole;
	}

}
