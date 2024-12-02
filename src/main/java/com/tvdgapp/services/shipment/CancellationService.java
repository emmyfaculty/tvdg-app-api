package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.CancellationRequestDto;
import com.tvdgapp.dtos.shipment.ReviewCancellationRequestDto;

public interface CancellationService {

    // Method for requesting a cancellation (user endpoint)
    void requestCancellation(CancellationRequestDto dto, Long id);

    // Method for reviewing cancellation requests (admin endpoint)
    void reviewCancellationRequest(Long id, ReviewCancellationRequestDto dto, Long adminId);

}
