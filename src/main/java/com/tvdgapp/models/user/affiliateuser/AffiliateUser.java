package com.tvdgapp.models.user.affiliateuser;

import com.tvdgapp.models.bankdetails.BankDetails;
import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.tvdgapp.constants.SchemaConstant.TABLE_AFFILIATES;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name = TABLE_AFFILIATES)
@SuppressWarnings("NullAway.Init")
public class AffiliateUser extends User {

    private String gender;
    private String title;

    @Column(nullable = false)
    private Date dateOfBirth;
    @Column(name = "identification_type")
    private String identificationType;

    @Column(unique = true, nullable = false)
    @Size(message = "Identification number must be unique")
    private String identificationNumber;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(unique = true, nullable = false)
    @Size(message = "userName must be to a user")
    private String username;

    private String referralCode;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BankDetails> bankDetails;

}
