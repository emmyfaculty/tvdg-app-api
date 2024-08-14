package com.tvdgapp.models.reference.countrycode;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.shipment.Shipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_LOCALE_CITIES;
import static com.tvdgapp.constants.SchemaConstant.TABLE_LOCALE_COUNTRIES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_LOCALE_CITIES)
@SuppressWarnings("NullAway.Init")
public class LocaleCity extends TvdgAppEntity<Integer, LocaleCity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "flag")
    private String flag;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private String dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_modified")
    private String dateModified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_created")
    private String tsCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_modified")
    private String tsModified;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "state_code")
    private String stateCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_on")
    private String updatedOn;

    @Column(name = "wiki_data_id")
    private String wikiDataId;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "deleted")
    private String deleted;

    @Column(name = "modified_by")
    private String modifiedBy;
}
