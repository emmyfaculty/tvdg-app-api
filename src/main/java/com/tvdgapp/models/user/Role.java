package com.tvdgapp.models.user;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.TABLE_ROLES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE roles " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name=TABLE_ROLES)
@SuppressWarnings("NullAway.Init")
public class Role extends TvdgAppEntity<Integer, Role> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length=100)//todo: make non-nullable
    private String name;
    @Nullable
    private String description;

    @Column(length = 30)//TODO: should be unique, but setting it here makes tests fail for now when calling AdminUserDetail.getLoggedinToken()
    @Nullable
    private String roleKey;

    @Enumerated(EnumType.STRING)
    @Nullable
    private RoleType roleType;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE })
    @JoinTable(
            name = "roles_permissions", // Custom join table name
            joinColumns = @JoinColumn(name = "role_id"), // Custom column name for Role
            inverseJoinColumns = @JoinColumn(name = "permission_id") // Custom column name for Permission
    )
    private Set<Permission> permissions = new HashSet<>();

//    @ManyToMany(cascade = {CascadeType.MERGE })
//    private Set<Permission> permissions = new HashSet<>();

    @Embedded
    private AuditSection auditSection = new AuditSection();

    private boolean systemCreated;

    public void addPermission(Permission permission) {
        permission.addRole(this);
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permission.removeRole(this);
        permissions.remove(permission);
    }

   
    public void addUser(User user){
        this.users.add(user);
    }

    public void removeUser(User user){
        //adminUser.removeRole(this);
        this.users.remove(user);
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", roleKey='" + roleKey + '\'' +
                ", roleType=" + roleType +
                ", auditSection=" + auditSection +
                '}';
    }
}
