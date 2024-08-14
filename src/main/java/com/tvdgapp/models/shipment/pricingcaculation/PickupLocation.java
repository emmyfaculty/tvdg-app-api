package com.tvdgapp.models.shipment.pricingcaculation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.tvdgapp.constants.SchemaConstant.TABLE_LOCAL_PICKUP_LOCATION;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name=TABLE_LOCAL_PICKUP_LOCATION)
public class PickupLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double weightBandStart;

    @Column
    private Double weightBandEnd;

    @Column(nullable = false)
    private String pickupRegion;
    @Column(nullable = false)
    private BigDecimal pickupFee;

}
