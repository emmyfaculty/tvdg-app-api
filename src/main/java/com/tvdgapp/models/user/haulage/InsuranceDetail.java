//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.FutureOrPresent;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "insurance_details")
//public class InsuranceDetail {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long insuranceId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "partner_id")
//    private Partner partner;
//
//    @NotBlank
//    @Size(max = 255)
//    private String insuranceProvider;
//
//    @NotBlank
//    @Size(max = 100)
//    private String policyNumber;
//
//    @NotNull
//    @FutureOrPresent
//    private Date expirationDate;
//
//    @Size(max = 500)
//    private String documentUrl;
//
//    // Getters and Setters
//}