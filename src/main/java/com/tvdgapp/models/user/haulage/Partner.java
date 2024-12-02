//package com.tvdgapp.models.user.haulage;
//
//import jakarta.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//@Table(name = "partners")
//public class Partner {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long partnerId;
//
//    @Column(nullable = false)
//    private String fullName;
//
//    @Column(nullable = false)
//    private String contactNumber;
//
//    @Column(nullable = false, unique = true)
//    private String emailAddress;
//
//    @Column(nullable = false)
//    private String address;
//
//    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
//    private Date dateOfBirth;
//
//    @Column(nullable = false)
//    private String businessName;
//
//    @Column(nullable = false, unique = true)
//    private String businessRegistrationNumber;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private BusinessStructure businessStructure;
//
//    @Column(nullable = false)
//    private int yearsInOperation;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Vehicle> vehicles;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<InsuranceDetail> insuranceDetails;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<DriverQualification> driverQualifications;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SafetyRecord> safetyRecords;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OperatingArea> operatingAreas;
//
//    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Reference> references;
//
//}
//
