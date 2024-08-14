package com.tvdgapp.dtos.rider;

import com.tvdgapp.models.user.rider.AssignedFor;
import com.tvdgapp.models.user.rider.ShipmentAssignmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiderShipmentMapDto {

    private Long id;
    private Long riderId;
    private String shipmentRef;
    private Long assignedById;
    private AssignedFor assignedFor;
    private LocalDateTime assignedAt;

}
