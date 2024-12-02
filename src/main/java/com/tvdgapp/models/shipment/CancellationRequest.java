package com.tvdgapp.models.shipment;

import com.tvdgapp.models.user.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.tvdgapp.constants.SchemaConstant.TABLE_CANCELLATION;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name=TABLE_CANCELLATION)
public class CancellationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_ref", referencedColumnName = "shipment_ref", nullable = false)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User requestedBy; // User who requested the cancellation

    @Enumerated(EnumType.STRING)
    private CancellationStatus status; // PENDING, APPROVED, REJECTED

    @Nullable
    private String reason; // Reason for cancellation
    @Nullable
    private String adminRemarks; // Admin comments during review

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Nullable
    private Date reviewedDate;

}
