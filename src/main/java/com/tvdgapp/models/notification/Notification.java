package com.tvdgapp.models.notification;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_NOTIFICATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
//@SQLDelete(sql =
//        "UPDATE shipment " +
//                "SET deleted = '1' " +
//                "WHERE id = ?")
//@Where(clause = "deleted='0'")
@Table(name=TABLE_NOTIFICATION)
public class Notification extends TvdgAppEntity<Long, Notification> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long userId;

    private String target;
    private String title;
    private String message;
    private String notificationType; // push, email, reminder
    private String role; // e.g., rider, customer, affiliate, admin

    @Embedded
    private AuditSection auditSection = new AuditSection();

}
