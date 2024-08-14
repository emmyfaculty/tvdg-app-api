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
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties({"created_at", "updated_on", "date_created", "date_modified", "ts_created", "ts_modified", "created_by", "deleted", "modified_by"})
public class LocaleCityDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_code", nullable = false)
    private String country_code;

    @Column(name = "flag")
    private String flag;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private String  date_created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_modified")
    private String date_modified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_created")
    private String ts_created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_modified")
    private String ts_modified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private String created_at;

    @Column(name = "state_code", nullable = false)
    private String state_code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on", nullable = false)
    private String updatedOn;

    @Column(name = "wiki_data_id", nullable = false)
    private String wikiDataId;

    @Column(name = "city_name", nullable = false)
    private String city_name;

    @Column(name = "created_by")
    private String created_by;

    @Column(name = "deleted")
    private String deleted;

    @Column(name = "modified_by")
    private String modified_by;
}
