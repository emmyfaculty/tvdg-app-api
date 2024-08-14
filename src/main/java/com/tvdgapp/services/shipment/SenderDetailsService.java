package com.tvdgapp.services.shipment;

import com.tvdgapp.models.shipment.SenderDetails;
import com.tvdgapp.repositories.shipment.SenderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SenderDetailsService {

    private final SenderDetailsRepository senderDetailsRepository;

    @Autowired
    public SenderDetailsService(SenderDetailsRepository senderDetailsRepository) {
        this.senderDetailsRepository = senderDetailsRepository;
    }

    public Optional<SenderDetails> findByShipmentId(Long shipmentId) {
        return senderDetailsRepository.findByShipmentId(shipmentId);
    }
    public Optional<SenderDetails> findByShipmentRef(String shipmentRef) {
        return senderDetailsRepository.findByShipmentRef(shipmentRef);
    }

    public void saveOrUpdate(SenderDetails senderDetails) {
        Optional<SenderDetails> existingDetails = senderDetailsRepository.findByShipmentRef(senderDetails.getShipmentRef());

        if (existingDetails.isPresent()) {
            // Update the existing sender details
            SenderDetails existing = existingDetails.get();
            existing.setFirstName(senderDetails.getFirstName());
            existing.setLastName(senderDetails.getLastName());
            existing.setCompanyName(senderDetails.getCompanyName());
            existing.setCountry(senderDetails.getCountry());
            existing.setAddress(senderDetails.getAddress());
            existing.setState(senderDetails.getState());
            existing.setCity(senderDetails.getCity());
            existing.setZipcode(senderDetails.getZipcode());
            existing.setEmail(senderDetails.getEmail());
            existing.setPhoneNo(senderDetails.getPhoneNo());
            senderDetailsRepository.save(existing);
        } else {
            // Save new sender details
            senderDetailsRepository.save(senderDetails);
        }
    }
}
