//package com.tvdgapp.models.user.haulage;
//
//@Entity
//@Table(name = "haulage_pt_personal_information")
//public class PersonalInformation {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "business_id", nullable = false)
//    private Integer businessId;
//
//    @Column(name = "first_name", nullable = false, length = 55)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false, length = 55)
//    private String lastName;
//
//    @Column(name = "contact_number", nullable = false, length = 20)
//    private String contactNumber;
//
//    @Column(name = "email_address", nullable = false, length = 255)
//    private String emailAddress;
//
//    @Column(name = "address", nullable = false, length = 255)
//    private String address;
//
//    @Column(name = "city", nullable = false, length = 100)
//    private String city;
//
//    @Column(name = "state", nullable = false, length = 100)
//    private String state;
//
//    @Column(name = "zip_code", nullable = false, length = 20)
//    private String zipCode;
//
//    @Column(name = "date_of_birth")
//    private LocalDate dateOfBirth;
//
//    @Column(name = "date_created", nullable = false, updatable = false)
//    @CreationTimestamp
//    private LocalDateTime dateCreated;
//
//    @Column(name = "date_modified", nullable = false)
//    @UpdateTimestamp
//    private LocalDateTime dateModified;
//
//    @Column(name = "ts_created", nullable = false)
//    private Integer tsCreated;
//
//    @Column(name = "ts_modified", nullable = false)
//    private Integer tsModified;
//
//    @Column(name = "deleted", nullable = false)
//    private Boolean deleted;
//}