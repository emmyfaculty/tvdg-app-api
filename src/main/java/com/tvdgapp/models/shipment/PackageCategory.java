package com.tvdgapp.models.shipment;

import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.tvdgapp.constants.SchemaConstant.TABLE_PACKAGE_CATEGORY;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@SQLDelete(sql =
//        "UPDATE packages " +
//                "SET deleted = '1' " +
//                "WHERE id = ?")
//@Where(clause = "deleted='0'")
@Table(name=TABLE_PACKAGE_CATEGORY)
public class PackageCategory extends TvdgAppEntity<Long, PackageCategory> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoryName;
    private BigDecimal categoryAmount;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
