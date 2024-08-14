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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.tvdgapp.constants.SchemaConstant.TABLE_RECEIVER_DETAILS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql = "UPDATE receiver_details SET deleted = '1' WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name = TABLE_RECEIVER_DETAILS)
public class ReceiverDetails extends TvdgAppEntity<Long, ReceiverDetails> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "shipment_ref", nullable = false)
    private String shipmentRef;
    private String firstName;
    private String lastName;
    private String companyName;
    private String tertiaryCountry;
    private String country;
    private String address;
    private String state;
    private String city;
    private String zipcode;
    private String email;
    private String phoneNo;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
