package com.tvdgapp.models.user.admin;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.tvdgapp.constants.SchemaConstant.TABLE_ADMIN_USER;


@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE admin_user " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name = TABLE_ADMIN_USER)
public class AdminUser extends User {

//    @Enumerated(EnumType.STRING)
//    @Column(name = "generate_code", nullable = false)
//    private GenerateCode generateCode;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "generate_code", nullable = false)
//    private GenerateCode generateCode; // YES or NO
}
