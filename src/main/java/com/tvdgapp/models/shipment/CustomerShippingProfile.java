package com.tvdgapp.models.shipment;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.common.audit.AuditSection;
import com.tvdgapp.models.common.audit.Auditable;
import com.tvdgapp.models.generic.TvdgAppEntity;
import com.tvdgapp.models.user.customer.CustomerUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static com.tvdgapp.constants.SchemaConstant.CUSTOMER_SHIPMENT_PROFILE;

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
@Table(name= CUSTOMER_SHIPMENT_PROFILE)
@SuppressWarnings("NullAway.Init")
public class CustomerShippingProfile extends TvdgAppEntity<Long, CustomerShippingProfile> implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String shipmentName;

    private String shippingMode;

    private String pickupLocation;

    private String pickupDays;

    private int averageDailyPackages;
    private String subscription;

    private double averageDailyWeight;

    private int numberOfMonthlyPackages;

    @Enumerated(EnumType.STRING)
    private ServiceInterest serviceInterests;

    private double monthlyRevenue;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private CustomerUser customerUser;

    @Embedded
    private AuditSection auditSection = new AuditSection();

}
