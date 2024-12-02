package com.tvdgapp.models.user.customer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.user.User;
import com.tvdgapp.utils.PricingCategoryDeserializer;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.TABLE_CUSTOMER_USERS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@SQLDelete(sql =
        "UPDATE customer_user " +
            "SET deleted = '1'" +
            "WHERE id = ?")
@Where(clause = "deleted = '0'")
@Table(name = TABLE_CUSTOMER_USERS)
@SuppressWarnings("NullAway.Init")
public class CustomerUser extends User {

    @Column(length = 100)
    private String customerRef;
    @Column(length = 100)
    private String companyName;

    @Column(length = 100)
    private String companyContactName;

    @Column(length = 100)
    private String companyEmail;

    @Column(length = 100)
    private String companyPhoneNo;

    @Column(length = 100)
    private String industry;

    @Column(length = 100)
    private String designation;

    @Column(length = 100)
    private String address;

    @Column(length = 100)
    private String city;

    @Column()
    @Nullable
    private String postalCode;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Nullable
    private String state;

    @Column()
    @Nullable
    private String title;

    @Temporal(TemporalType.DATE)
    @Nullable
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column()
    @Nullable
    private String natureOfBusiness;
    @Column()
    @Nullable
    private String companyRegNumber;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    private Integer pricingLevelId;

//    @OneToMany(mappedBy = "customerUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference
//    @Nullable
//    private Set<Shipment> shipment = new HashSet<>();

    private Long shipmentCount;


}
