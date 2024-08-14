package com.tvdgapp.services.shipment;

import com.tvdgapp.models.shipment.ReceiverDetails;
import com.tvdgapp.repositories.shipment.ReceiverDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceiverDetailsService {

    private final ReceiverDetailsRepository receiverDetailsRepository;

    @Autowired
    public ReceiverDetailsService(ReceiverDetailsRepository receiverDetailsRepository) {
        this.receiverDetailsRepository = receiverDetailsRepository;
    }

    public Optional<ReceiverDetails> findByShipmentId(Long shipmentId) {
        return receiverDetailsRepository.findByShipmentId(shipmentId);
    }
    public Optional<ReceiverDetails> findByShipmentRef(String shipmentRef) {
        return receiverDetailsRepository.findByShipmentRef(shipmentRef);
    }

    public void saveOrUpdate(ReceiverDetails receiverDetails) {
        Optional<ReceiverDetails> existingDetails = receiverDetailsRepository.findByShipmentRef(receiverDetails.getShipmentRef());

        if (existingDetails.isPresent()) {
            // Update the existing receiver details
            ReceiverDetails existing = existingDetails.get();
            existing.setFirstName(receiverDetails.getFirstName());
            existing.setLastName(receiverDetails.getLastName());
            existing.setCompanyName(receiverDetails.getCompanyName());
            existing.setCountry(receiverDetails.getCountry());
            existing.setAddress(receiverDetails.getAddress());
            existing.setState(receiverDetails.getState());
            existing.setCity(receiverDetails.getCity());
            existing.setZipcode(receiverDetails.getZipcode());
            existing.setEmail(receiverDetails.getEmail());
            existing.setPhoneNo(receiverDetails.getPhoneNo());
            receiverDetailsRepository.save(existing);
        } else {
            // Save new receiver details
            receiverDetailsRepository.save(receiverDetails);
        }
    }
}
