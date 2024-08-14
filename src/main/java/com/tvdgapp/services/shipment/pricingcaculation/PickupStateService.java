package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.PickupStateDTO;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.PickupStateMapper;
import com.tvdgapp.models.shipment.pricingcaculation.PickupState;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PickupStateService {

    private static final Logger logger = LoggerFactory.getLogger(PickupStateService.class);

    @Autowired
    private PickupStateRepository pickupStateRepository;

    public PickupStateDTO createPickupState(PickupStateDTO pickupStateDTO) {
        PickupState pickupState = PickupStateMapper.INSTANCE.toPickupState(pickupStateDTO);
        pickupState = pickupStateRepository.save(pickupState);
        logger.info("Created new PickupState with ID: {}", pickupState.getId());
        return PickupStateMapper.INSTANCE.toPickupStateDTO(pickupState);
    }

    public PickupStateDTO getPickupStateById(Long id) {
        PickupState pickupState = pickupStateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PickupState not found with id " + id));
        return PickupStateMapper.INSTANCE.toPickupStateDTO(pickupState);
    }

    public List<PickupStateDTO> getAllPickupStates() {
        return pickupStateRepository.findAll().stream()
                .map(PickupStateMapper.INSTANCE::toPickupStateDTO)
                .collect(Collectors.toList());
    }

    public PickupStateDTO updatePickupState(Long id, PickupStateDTO pickupStateDTO) {
        Optional<PickupState> optionalPickupState = pickupStateRepository.findById(id);
        if (!optionalPickupState.isPresent()) {
            throw new ResourceNotFoundException("PickupState not found with id " + id);
        }

        PickupState pickupState = optionalPickupState.get();
        pickupState.setStateName(pickupStateDTO.getStateName());
        pickupState.setPickupRegion(pickupStateDTO.getPickupRegion());
        pickupState = pickupStateRepository.save(pickupState);
        logger.info("Updated PickupState with ID: {}", pickupState.getId());
        return PickupStateMapper.INSTANCE.toPickupStateDTO(pickupState);
    }

    public void deletePickupState(Long id) {
        if (!pickupStateRepository.existsById(id)) {
            throw new ResourceNotFoundException("PickupState not found with id " + id);
        }
        pickupStateRepository.deleteById(id);
        logger.info("Deleted PickupState with ID: {}", id);
    }
}
