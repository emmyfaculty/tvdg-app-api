package com.tvdgapp.models.user.rider;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.shipment.Shipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import static com.tvdgapp.constants.SchemaConstant.TABLE_RIDER_SHIPMENT_MAP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TABLE_RIDER_SHIPMENT_MAP)
public class RiderShipmentMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rider_id", nullable = false)
    private RiderUser rider;

    @ManyToOne
    @JoinColumn(name = "shipment_ref", referencedColumnName = "shipment_ref", nullable = false)
    private Shipment shipment;

    @Column(name = "assigned_by", nullable = false)
    private Long assignedById;
    @Enumerated(EnumType.STRING)
    private AssignedFor assignedFor;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

}
