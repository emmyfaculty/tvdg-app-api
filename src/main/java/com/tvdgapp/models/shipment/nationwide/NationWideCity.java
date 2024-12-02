//package com.tvdgapp.models.shipment.nationwide;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import static com.tvdgapp.constants.SchemaConstant.TABLE_NATIONAL_WIDE_CITY;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name= TABLE_NATIONAL_WIDE_CITY)
//@Entity
//public class NationWideCity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    private String name;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "nation_wide_state_id")
//    private NationWideState state;
//    private String description;
//
//}
