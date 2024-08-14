package com.tvdgapp.models.reference.countrycode;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_LOCALE_STATES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = TABLE_LOCALE_STATES)
@SuppressWarnings("NullAway.Init")
public class LocaleState extends TvdgAppEntity<Integer, LocaleState> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "state_name", nullable = false)
    private String stateName;

    @Column(name = "capital_name")
    private String capitalName;

    @Column(name = "country_id", nullable = false)
    private String countryId;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(name = "fips_code", nullable = false)
    private String fipsCode;

    @Column(name = "iso2", nullable = false)
    private String iso2;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "flag")
    private String flag;

    @Column(name = "wikiDataId")
    private String wikiDataId;

}
