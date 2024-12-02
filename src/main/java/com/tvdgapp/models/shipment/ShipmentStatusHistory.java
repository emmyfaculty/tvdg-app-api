package com.tvdgapp.models.shipment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipment_status_history")
public class ShipmentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_ref", referencedColumnName = "shipment_ref", nullable = false)
    private Shipment shipment;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "status_description", columnDefinition = "TEXT")
    private String statusDescription;
//
//    @Column(name = "timestamp", nullable = false)
//    private LocalDateTime timestamp;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_by_role", length = 50)
    private String updatedByRole;

    @Column(name = "update_source", length = 50)
    private String updateSource;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "is_final_status", nullable = false)
    private Boolean isFinalStatus = false;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "date_modified")
    private LocalDateTime updatedAt;
    @Column(name = "ts_created", nullable = false)
    private LocalDateTime tscreated;

    @Column(name = "ts_modified")
    private LocalDateTime tsupdated;
}
