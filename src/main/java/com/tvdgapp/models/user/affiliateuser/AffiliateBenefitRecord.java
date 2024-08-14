package com.tvdgapp.models.user.affiliateuser;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.shipment.ReceiverDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import static com.tvdgapp.constants.SchemaConstant.TABLE_AFFILIATE_BENEFITED_RECORDS;
import static com.tvdgapp.constants.SchemaConstant.TABLE_RECEIVER_DETAILS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Where(clause = "deleted='0'")
@Table(name = TABLE_AFFILIATE_BENEFITED_RECORDS)
public class AffiliateBenefitRecord extends TvdgAppEntity<Long, AffiliateBenefitRecord> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long affiliateUserId;
    private String phoneNumber;
    private String email;
    private String company;
    private LocalDateTime timestamp;

    @Embedded
    private AuditSection auditSection = new AuditSection();

}