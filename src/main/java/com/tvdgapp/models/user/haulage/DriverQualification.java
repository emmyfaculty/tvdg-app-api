//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Min;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//
//@Entity
//@Table(name = "driver_qualifications")
//public class DriverQualification {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long driverId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "partner_id")
//    private Partner partner;
//
//    @NotBlank
//    @Size(max = 255)
//    private String driverName;
//
//    @NotBlank
//    @Size(max = 100)
//    private String licenseNumber;
//
//    @NotNull
//    @Min(0)
//    private Integer experienceYears;
//
//    @Size(max = 255)
//    private String specialCertifications;
//
//    // Getters and Setters
//}
