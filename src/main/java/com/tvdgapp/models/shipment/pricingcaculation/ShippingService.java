package com.tvdgapp.models.shipment.pricingcaculation;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.shipment.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_SHIPPING_SERVICES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name= TABLE_SHIPPING_SERVICES)
public class ShippingService /*extends TvdgAppEntity<Long, ShippingService> implements Auditable */ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Integer serviceId;

    @Column(name = "service_name", nullable = false, length = 255)
    private String serviceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ServiceType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "carrier", nullable = false)
    private Carrier carrier;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;


    @OneToMany(mappedBy = "shippingService", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExpectedDeliveryDay> expectedDeliveryDays;

    @OneToMany(mappedBy = "shippingService", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceModelLevel> priceModelLevels;


}


