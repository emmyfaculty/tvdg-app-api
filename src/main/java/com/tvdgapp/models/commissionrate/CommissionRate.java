package com.tvdgapp.models.commissionrate;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;

import static com.tvdgapp.constants.SchemaConstant.TABLE_COMMISSION_RATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE packages " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name = TABLE_COMMISSION_RATE)
public class CommissionRate extends TvdgAppEntity<Long, CommissionRate> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal minSalesAmount;

    @Column
    private BigDecimal maxSalesAmount;

    @Column(nullable = false)
    private BigDecimal commissionPercentage;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
