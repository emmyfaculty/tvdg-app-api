package com.tvdgapp.dtos.shipment;

import com.google.firebase.database.DatabaseError;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class AuditSectionDto {
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date modifiedAt;
    private String tsCreated;
    private String tsModified;
}
