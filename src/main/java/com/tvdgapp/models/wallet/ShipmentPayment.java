package com.tvdgapp.models.wallet;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "shipment_payment")
public class ShipmentPayment extends TvdgAppEntity<Long, ShipmentPayment> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_ref", nullable = false)
    private String shipmentRef;

    @Column(name = "payment_ref", nullable = false, unique = true)
    private String paymentRef;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

//    @Column(name = "create_ts", nullable = false)
//    private Integer createTs;
//
//    @Column(name = "update_ts", nullable = false)
//    private Integer updateTs;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_processor", nullable = false)
    private PaymentProcessor paymentProcessor;

    @Embedded
    private AuditSection auditSection = new AuditSection();

}

