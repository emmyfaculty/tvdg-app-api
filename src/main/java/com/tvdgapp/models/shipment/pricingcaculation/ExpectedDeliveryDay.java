package com.tvdgapp.models.shipment.pricingcaculation;

import com.tvdgapp.models.reference.Region;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tvdgapp.constants.SchemaConstant.TABLE_EXPECTED_DELIVERY_DAY_INTERNATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_EXPECTED_DELIVERY_DAY_INTERNATION)
public class ExpectedDeliveryDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @NotNull
    private String dayRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ShippingService shippingService;
}
