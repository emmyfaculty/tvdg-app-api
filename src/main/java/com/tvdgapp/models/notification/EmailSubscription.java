package com.tvdgapp.models.notification;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tvdgapp.constants.SchemaConstant.TABLE_EMAILSUBSCRIPTION;

@Entity
@Table(name = TABLE_EMAILSUBSCRIPTION, uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@EntityListeners(AuditListener.class)
@NoArgsConstructor
public class EmailSubscription extends TvdgAppEntity<Long, EmailSubscription> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean subscribed;

    @Embedded
    private AuditSection auditSection = new AuditSection();

}
