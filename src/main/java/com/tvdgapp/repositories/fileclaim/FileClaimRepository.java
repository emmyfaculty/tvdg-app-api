package com.tvdgapp.repositories.fileclaim;

import com.tvdgapp.models.fileclaims.FileClaims;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileClaimRepository extends JpaRepository<FileClaims, Long> {
    Optional<Object> findByShipmentTrackingNo(String shipmentTrackingNo);
}
