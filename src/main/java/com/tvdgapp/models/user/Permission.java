package com.tvdgapp.models.user;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.util.HashSet;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.TABLE_PERMISSIONS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE permissions " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name=TABLE_PERMISSIONS)
@SuppressWarnings("NullAway.Init")
public class Permission extends TvdgAppEntity<Integer, Permission> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String permission;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private PermissionType permissionType;
    @Column(length = 50)
    private String permissionGroup;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
    }
    public void removeRole(Role role) {
        roles.remove(role);
    }

    @Embedded
    private AuditSection auditSection = new AuditSection();

    public Permission(String permission) {
        this.permission=permission;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", permissionType=" + permissionType +
                '}';
    }
}
