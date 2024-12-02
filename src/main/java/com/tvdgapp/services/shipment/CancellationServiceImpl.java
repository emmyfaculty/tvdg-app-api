package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.CancellationRequestDto;
import com.tvdgapp.dtos.shipment.ReviewCancellationRequestDto;
import com.tvdgapp.models.shipment.CancellationRequest;
import com.tvdgapp.models.shipment.CancellationStatus;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.user.User;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.repositories.shipment.CancellationRequestRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.services.user.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CancellationServiceImpl implements CancellationService {

    @Autowired
    private CancellationRequestRepository cancellationRequestRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private UserRepository repository;

//    @Autowired
//    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(CancellationServiceImpl.class);

    @Override
    @Transactional
    public void requestCancellation(CancellationRequestDto dto, Long id) {
        Optional<User> user = repository.findById(id);

        // Find the shipment
        Shipment shipment = shipmentRepository.findByShipmentRef(dto.getShipmentRef())
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        // Ensure shipment is still in warehouse
        if (!shipment.getStatus().equalsIgnoreCase("IN_WAREHOUSE")) {
            throw new IllegalStateException("Only shipments in the warehouse can be cancelled.");
        }

        // Check if a cancellation request for this shipment already exists
        Optional<CancellationRequest> existingRequest = cancellationRequestRepository.findByShipmentRef(shipment.getShipmentRef());
        if (existingRequest.isPresent() && existingRequest.get().getStatus() == CancellationStatus.PENDING) {
            throw new IllegalStateException("Cancellation request for this shipment is already pending.");
        }

        // Create and save the cancellation request
        CancellationRequest request = new CancellationRequest();
        request.setShipment(shipment);
        request.setRequestedBy(user.get());
        request.setStatus(CancellationStatus.PENDING);
        request.setReason(dto.getReason());
        request.setRequestDate(new Date());

        cancellationRequestRepository.save(request);

        // Log the request
        logger.info("Cancellation request created for shipment: {} by user: {}", dto.getShipmentRef(), id);

        // Notify the admin
//        notificationService.notifyAdminOfCancellationRequest(request);
    }

    @Override
    @Transactional
    public void reviewCancellationRequest(Long id, ReviewCancellationRequestDto dto, Long adminId) {
        Optional<User> admin = repository.findById(adminId);

        // Fetch the cancellation request
        CancellationRequest request = cancellationRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cancellation request not found"));

        if (request.getStatus() != CancellationStatus.PENDING) {
            throw new IllegalStateException("This cancellation request has already been reviewed.");
        }

        // Approve or reject the request
        if (dto.isApproved()) {
            request.setStatus(CancellationStatus.APPROVED);
            
            // Update shipment status to "CANCELLED"
            Shipment shipment = request.getShipment();
            shipment.setStatus("CANCELLED");
            shipmentRepository.save(shipment);

            logger.info("Shipment {} was cancelled by admin: {}", shipment.getShipmentRef(), admin.get());
        } else {
            request.setStatus(CancellationStatus.REJECTED);
            request.setAdminRemarks(dto.getRemarks());

            logger.info("Cancellation request {} was rejected by admin: {}", request.getId(), admin.get());
        }

        // Update the request with review details
        request.setReviewedDate(new Date());
        cancellationRequestRepository.save(request);

        // Notify the user
//        notificationService.notifyUserOfCancellationReview(request);
    }
}
