//package com.tvdgapp.models.notification;
//
//import com.tvdgapp.models.common.audit.AuditListener;
//import com.tvdgapp.models.common.audit.AuditSection;
//import com.tvdgapp.models.common.audit.Auditable;
//import com.tvdgapp.models.generic.TvdgAppEntity;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//
//import static com.tvdgapp.constants.SchemaConstant.TABLE_MAILINGLIST;
//import static com.tvdgapp.constants.SchemaConstant.TABLE_NOTIFICATION;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@EntityListeners(AuditListener.class)
//@Table(name=TABLE_MAILINGLIST)
//public class MailingList extends TvdgAppEntity<Long, MailingList> implements Auditable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
//    @GenericGenerator(name = "native")
//    @Column(name = "id", updatable = false, nullable = false)
//    private Long id;
//    private String email;
//
//    @Embedded
//    private AuditSection auditSection = new AuditSection();
//}
