package com.tvdgapp.models.quote;

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

import static com.tvdgapp.constants.SchemaConstant.TABLE_QUOTE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@SQLDelete(sql =
        "UPDATE shipment " +
                "SET deleted = '1' " +
                "WHERE id = ?")
@Where(clause = "deleted='0'")
@Table(name=TABLE_QUOTE)
@SuppressWarnings("NullAway.Init")
public class Quote extends TvdgAppEntity<Long, Quote> implements Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String subject;
    private String description;
    private String companyName;
    private String industry;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String country;
    private String state;
    private String city;
    private String zipcode;
    private String streetAddress;

    @Embedded
    private AuditSection auditSection = new AuditSection();
}
