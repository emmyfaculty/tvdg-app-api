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

import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_INTERNATIONAL_REGIONS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SuppressWarnings("NullAway.Init")
@Table(name = TABLE_INTERNATIONAL_REGIONS)
public class Region /*extends TvdgAppEntity<Integer, Region> implements Auditable*/ {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regionId;
    @NotNull
    private String regionName;
    private String regionDescription;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Country> countries;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}

