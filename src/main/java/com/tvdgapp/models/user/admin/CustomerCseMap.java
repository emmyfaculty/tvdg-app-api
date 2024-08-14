package com.tvdgapp.models.user.admin;

import com.tvdgapp.models.common.audit.AuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.tvdgapp.constants.SchemaConstant.TABLE_CUSTOMER_CSE_MAP;
import static com.tvdgapp.constants.SchemaConstant.TABLE_USER_ROLE;


@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = TABLE_CUSTOMER_CSE_MAP)
public class CustomerCseMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "cse_id", nullable = false)
    private Long cseId;

    @Column(name = "assigned_by", nullable = false)
    private Long assignedBy;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Column(name = "ts_created", nullable = false)
    private Integer tsCreated;

    @Column(name = "ts_modified")
    private Integer tsModified;

    @Column(name = "deleted", length = 1)
    private String deleted;

}
