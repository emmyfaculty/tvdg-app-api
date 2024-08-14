package com.tvdgapp.models.shipment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tvdgapp.constants.SchemaConstant.TABLE_CUSTOMER_SHIPMENT_MAP;
import static com.tvdgapp.constants.SchemaConstant.TABLE_PRODUCT_ITEM;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name=TABLE_CUSTOMER_SHIPMENT_MAP)
public class CustomerShipmentMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "shipment_ref", nullable = false)
    private String shipmentRef;

}
