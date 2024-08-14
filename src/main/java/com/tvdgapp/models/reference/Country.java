package com.tvdgapp.models.reference;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.tvdgapp.constants.SchemaConstant.TABLE_INTERNATIONAL_COUNTRY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SuppressWarnings("NullAway.Init")
@Table(name = TABLE_INTERNATIONAL_COUNTRY)
public class Country extends TvdgAppEntity<Integer, Country> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private String name;
    @Column(name = "iso3")
    private String iso3;

    @Column(name = "iso2")
    private String iso2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
