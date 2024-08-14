package com.tvdgapp.models.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.utils.CommonUtils;
import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.*;

//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TABLE_USERS, indexes = {
        @jakarta.persistence.Index(name = "idx_firstnme", columnList = "first_name"),
        @jakarta.persistence.Index(name = "idx_lastnme", columnList = "last_name"),
        @Index(name = "idx_status", columnList = "status, user_type")
})
@SuppressWarnings("NullAway.Init")
public class User extends TvdgAppEntity<Long, User> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(length = SchemaConstant.FIRST_NAME_COL_SIZE)
//    @Index(name = "idx_firstnme")
    @Nullable
    private String firstName;

    @Column(length = SchemaConstant.LAST_NAME_COL_SIZE)
//    @Index(name = "idx_lastnme")
    @Nullable
    private String lastName;

    @Column(unique = true, length = SchemaConstant.EMAIL_COL_SIZE)
    private String email;

    @Column(length = 64)
    @Nullable
    private String password;

    @Column(length = 100)
    @Nullable
    private String passwordResetToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date passwordResetVldtyTerm;

    @Column(name = "last_login")
    private Long lastLogin;

    @Enumerated(EnumType.STRING)
//    @Index(name = "idx_status")//todo: make non-nullable
    private UserStatus status;

    @Column(length = SchemaConstant.PHONE_NO_COL_SIZE)
    @Nullable
    private String telephoneNumber;

    @Enumerated(EnumType.STRING)
//    @Index(name = "idx_status")//todo: make non-nullable
    private UserType userType;

    @Column(length = 64)
    @Nullable
    private String profilePic;
    private String otp;

    private String deviceToken;
    private boolean receivePushNotification;
    private boolean receiveEmail;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(
            name = TABLE_USER_ROLE,
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.addUser(this);
    }

    public void removeRole(Role role) {
        role.removeUser(this);
        roles.remove(role);
    }

    @Embedded
    private AuditSection auditSection = new AuditSection();
    protected User(Long id) { this.id = id; }

    public boolean tokenExpired() {
        if (this.passwordResetVldtyTerm != null && this.passwordResetVldtyTerm.before(TvdgAppDateUtils.now())) {
            return true;
        }
        return false;
    }

    public void calculateTokenExpiryDate(String valdtytrm) {
        if (!"0" .equals(valdtytrm)) { //0 means no validity term used
            if (!CommonUtils.isInteger(valdtytrm)) {
                this.setDefaultPasswordValidityTerm();
            } else {
                if (Integer.parseInt(valdtytrm) < 0) {
                    this.setDefaultPasswordValidityTerm();
                } else {
                    setPasswordResetVldtyTerm(DateUtils.addHours(TvdgAppDateUtils.now(), Integer.parseInt(valdtytrm)));
                }
            }
        }
    }

    private void setDefaultPasswordValidityTerm() {
        setPasswordResetVldtyTerm(DateUtils.addHours(TvdgAppDateUtils.now(), SchemaConstant.DEFAULT_PWRD_SETTING_VLDTY_TRM));
    }

}
