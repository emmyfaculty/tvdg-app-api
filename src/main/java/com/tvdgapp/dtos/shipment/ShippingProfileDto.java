package com.tvdgapp.dtos.shipment;

import com.tvdgapp.models.shipment.ServiceInterest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ShippingProfileDto {

    private Long id;

    @NotNull
    private String shipmentName;

    @NotNull
    private String shippingMode;

    @NotNull
    private String pickupLocation;

    @NotNull
    private String pickupDays;

    @NotNull
    private int averageDailyPackages;

    @NotNull
    private double averageDailyWeight;

    @NotNull
    private int numberOfPackagesPerMonth;

    @NotNull
    private String subscription;

    @NotNull
    private ServiceInterest serviceInterests;


    @NotNull
    private double monthlyRevenue;
//    private ShipmentMode shipmentMode;
//    private String pickupAddress;
//    private int numberOfMonthlyPackages;

}

