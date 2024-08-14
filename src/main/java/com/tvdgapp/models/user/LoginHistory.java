//package com.tvdgapp.models.user;
//
//import com.tvdgapp.constants.PrcRsltCode;
//import com.tvdgapp.models.common.audit.AuditListener;
//import com.tvdgapp.models.common.audit.AuditSection;
//import com.tvdgapp.models.common.audit.Auditable;
//import com.tvdgapp.models.generic.TvdgAppEntity;
//import jakarta.annotation.Nullable;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//
//@MappedSuperclass
//@EntityListeners(value = AuditListener.class)
////@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Getter
//@Setter
//@SuppressWarnings("NullAway.Init")
//public class LoginHistory extends TvdgAppEntity<Long, LoginHistory> implements Auditable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    protected Long id;
//
//    @Column(name = "IP_ADDR")
//    @Nullable
//    private String ipAddr;
//
//    @Column(name = "PRC_RSLT", columnDefinition = "varchar(1) not null")
//    @Nullable
//    private String prcRslt= PrcRsltCode.FAILURE; //failure by default
//
//    @Embedded
//    private AuditSection auditSection = new AuditSection();
//
//}
