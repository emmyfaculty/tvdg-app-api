package com.tvdgapp.models.shipment;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.tvdgapp.constants.SchemaConstant.TABLE_PRODUCT_ITEM;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
//@SQLDelete(sql =
//        "UPDATE packages " +
//                "SET deleted = '1' " +
//                "WHERE id = ?")
//@Where(clause = "deleted='0'")
@Table(name=TABLE_PRODUCT_ITEM)
public class ProductItem extends TvdgAppEntity<Long, ProductItem> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_ref", referencedColumnName = "shipment_ref", nullable = false)
    private Shipment shipment;
    @ManyToOne
    @JoinColumn(name = "package_category_id")
    private PackageCategory packageCategory;
//    private String productName;
    private String description;
    private int quantity;
    private String units;
    private BigDecimal value;
    private double weight;
    private String manufacturingCountry;

    @Embedded
    private AuditSection auditSection = new AuditSection();

}
