package com.tvdgapp.models.reference;

import com.tvdgapp.models.common.audit.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.tvdgapp.constants.SchemaConstant.TABLE_INTERNATIONAL_REGIONS_COUNTRIES;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SuppressWarnings("NullAway.Init")
@Table(name = TABLE_INTERNATIONAL_REGIONS_COUNTRIES)
public class Country /*extends TvdgAppEntity<Integer, Country> implements Auditable */ {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @NotNull
//    private String name;
//    @Column(name = "iso3")
//    private String iso3;
//
//    @Column(name = "iso2")
//    private String iso2;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "region_id")
//    private Region region;
//
//    @Embedded
//    private AuditSection auditSection = new AuditSection();

    @Id
    @Column(name = "country_id", nullable = false)
    private Integer countryId;

    @Column(name = "country_name", nullable = false, length = 255)
    private String countryName;

    @Column(name = "iso2", length = 2, nullable = true)
    private String iso2;

    @Column(name = "iso3", length = 3, nullable = true)
    private String iso3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "create_ts", nullable = true)
    private Integer createTs;

    @Column(name = "update_ts", nullable = true)
    private Integer updateTs;

    @PrePersist
    protected void onCreate() {
        createTs = Math.toIntExact(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updateTs = Math.toIntExact(System.currentTimeMillis());
    }
}
