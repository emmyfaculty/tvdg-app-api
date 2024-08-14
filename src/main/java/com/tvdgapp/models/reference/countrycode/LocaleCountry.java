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


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_COUNTRY_CODE;
import static com.tvdgapp.constants.SchemaConstant.TABLE_LOCALE_COUNTRIES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TABLE_LOCALE_COUNTRIES)
@SuppressWarnings("NullAway.Init")
public class LocaleCountry extends TvdgAppEntity<Integer, LocaleCountry>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "iso3")
    private String iso3;

    @Column(name = "iso2")
    private String iso2;

    @Column(name = "phonecode")
    private String phonecode;

    @Column(name = "capital")
    private String capital;

    @Column(name = "currency")
    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private String createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "flag")
    private String flag;

    @Column(name = "wiki_data_id")
    private String wikiDataId;

}
