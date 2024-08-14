package com.tvdgapp.models.reference.countrycode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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
public class LocaleCountryDto {

    private Integer id;

    @Column(name = "country_name", nullable = false)
    private String country_name;

    @Column(name = "iso3", nullable = false)
    private String iso3;

    @Column(name = "iso2", nullable = false)
    private String iso2;

    @Column(name = "phonecode", nullable = false)
    private String phone_code;

    @Column(name = "capital", nullable = false)
    private String capital;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private String created_at;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private String updated_at;

    @Column(name = "flag")
    private String flag;

    @Column(name = "wiki_data_id")
    private String wikiDataId;

}
