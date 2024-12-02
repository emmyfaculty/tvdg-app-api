//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.*;
//
//@Entity
//@Table(name = "vehicles")
//public class Vehicle {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long vehicleId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "partner_id")
//    private Partner partner;
//
//    @NotBlank
//    @Size(max = 50)
//    private String registrationNumber;
//
//    @NotNull
//    @Min(0)
//    private Integer age;
//
//    @NotNull
//    @DecimalMin("0.00")
//    private Double loadCapacity;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private VehicleType type;
//
//    @Size(max = 500)
//    private String documentUrl;
//
//    // Getters and Setters
//}
