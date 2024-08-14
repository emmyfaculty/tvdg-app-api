package com.tvdgapp.models.user.rider;

import com.tvdgapp.models.common.audit.AuditListener;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tvdgapp.constants.SchemaConstant.TABLE_AFFILIATES;
import static com.tvdgapp.constants.SchemaConstant.TABLE_RIDERS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
@Entity
@Table(name = TABLE_RIDERS)
@SuppressWarnings("NullAway.Init")
public class RiderUser extends User {

    private String title;

    @Column(nullable = false)
    private Date dateOfBirth;

}
