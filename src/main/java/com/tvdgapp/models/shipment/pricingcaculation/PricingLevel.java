package com.tvdgapp.models.shipment.pricingcaculation;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_PRICING_LEVELS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name=TABLE_PRICING_LEVELS)
public class PricingLevel /*extends TvdgAppEntity<Long, PricingLevel> implements Auditable */{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "level_name", nullable = false, length = 50)
    private String levelName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "pricingLevel")
    private List<PriceModelLevel> priceModelLevels;

//    @Embedded
//    private AuditSection auditSection = new AuditSection();

}


