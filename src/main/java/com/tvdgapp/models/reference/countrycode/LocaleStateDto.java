package com.tvdgapp.models.reference.countrycode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties({"created_at", "updated_at"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocaleStateDto {

//    private Integer id;
//
//    @Column(name = "country_code", nullable = false)
//    private String countryCode;
//
//    @Column(name = "country_id", nullable = false)
//    private Integer countryId;
//
//    @Column(name = "flag", nullable = false)
//    private Integer flag;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_at", nullable = false)
//    private Date created_at;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "updated_at", nullable = false)
//    private Date updated_at;
////
//    @Column(name = "capital_name")
//    private String capitalName;
//
//    @Column(name = "created_by")
//    private String createdBy;
//
//    @Column(name = "deleted")
//    private Boolean deleted;
//
//    @Column(name = "fips_code", nullable = false)
//    private String fipsCode;
//
//    @Column(name = "iso2", nullable = false)
//    private String iso2;
//
//    @Column(name = "modified_by")
//    private String modifiedBy;
//
//    @Column(name = "state_name", nullable = false)
//    private String stateName;
//
//    @Column(name = "wiki_data_id")
//    private String wikiDataId;

    private Integer id;
    private String state_name;
    private String capital_name;
    private String country_id;
    private String country_code;
    private String fips_code;
    private String iso2;
    private String created_at;
    private String updated_at;
    private String flag;
    private String wikiDataId;

}
