package com.tvdgapp.models;

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

import static com.tvdgapp.constants.SchemaConstant.TABLE_SYSTEM_CONFIGURATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = TABLE_SYSTEM_CONFIGURATION)
@SuppressWarnings("NullAway.Init")
public class SystemConfiguration extends TvdgAppEntity<Long, SystemConfiguration> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=100)
    @Nullable
    private String configurationName;
    @Nullable
    private String value;

    @Column(length=50,unique = true)
    @Nullable
    private String configurationKey;

    @Nullable
    private String description;

    @Column(length=30)
    @Nullable
    private String configurationGroup;

    private int sortOrder;

    @Enumerated(EnumType.STRING)//TODO: make-non-nullable
    private SystemConfigrationType systemConfigrationType=SystemConfigrationType.TEXT;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
