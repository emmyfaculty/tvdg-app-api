//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//
//@Entity
//@Table(name = "operating_areas")
//public class OperatingArea {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long areaId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "partner_id")
//    private Partner partner;
//
//    @NotBlank
//    @Size(max = 255)
//    private String regions;
//
//    @Size(max = 255)
//    private String preferredRoutes;
//
//    @DecimalMin("0.00")
//    private Double maxLoadCapacity;
//
//    // Getters and Setters
//}
