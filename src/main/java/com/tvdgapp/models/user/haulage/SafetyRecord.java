//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Min;
//
//@Entity
//@Table(name = "safety_records")
//public class SafetyRecord {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long safetyRecordId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "partner_id")
//    private Partner partner;
//
//    @Lob
//    private String description;
//
//    @Min(0)
//    private Integer incidentCount;
//
//}