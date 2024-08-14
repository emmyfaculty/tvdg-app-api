package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.PickupLocationDTO;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.PickupLocationMapper;
import com.tvdgapp.models.shipment.pricingcaculation.PickupLocation;
import com.tvdgapp.repositories.shipment.pricecaculation.PickupLocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PickupLocationService {

    private static final Logger logger = LoggerFactory.getLogger(PickupLocationService.class);

    @Autowired
    private PickupLocationRepository pickupLocationRepository;

    @Autowired
    private PickupLocationMapper pickupLocationMapper;

    public List<PickupLocationDTO> findAll() {
        logger.info("Fetching all pickup locations");
        return pickupLocationRepository.findAll().stream()
                .map(pickupLocationMapper::toDto)
                .collect(Collectors.toList());
    }

    public PickupLocationDTO findById(Long id) {
        logger.info("Fetching pickup location with id {}", id);
        return pickupLocationRepository.findById(id)
                .map(pickupLocationMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("Pickup location not found with id {}", id);
                    return new ResourceNotFoundException("Pickup location not found with id " + id);
                });
    }

    public PickupLocationDTO save(PickupLocationDTO pickupLocationDTO) {
        logger.info("Saving new pickup location");
        PickupLocation pickupLocation = pickupLocationMapper.toEntity(pickupLocationDTO);
        pickupLocation = pickupLocationRepository.save(pickupLocation);
        return pickupLocationMapper.toDto(pickupLocation);
    }

    public PickupLocationDTO update(Long id, PickupLocationDTO pickupLocationDTO) {
        logger.info("Updating pickup location with id {}", id);
        PickupLocation pickupLocation = pickupLocationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pickup location not found with id {}", id);
                    return new ResourceNotFoundException("Pickup location not found with id " + id);
                });

        pickupLocation.setWeightBandStart(pickupLocationDTO.getWeightBandStart());
        pickupLocation.setWeightBandEnd(pickupLocationDTO.getWeightBandEnd());
        pickupLocation.setPickupRegion(pickupLocationDTO.getPickupRegion());
        pickupLocation.setPickupFee(pickupLocationDTO.getPickupFee());
        pickupLocation = pickupLocationRepository.save(pickupLocation);
        return pickupLocationMapper.toDto(pickupLocation);
    }

    public void delete(Long id) {
        logger.info("Deleting pickup location with id {}", id);
        PickupLocation pickupLocation = pickupLocationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Pickup location not found with id {}", id);
                    return new ResourceNotFoundException("Pickup location not found with id " + id);
                });
        pickupLocationRepository.delete(pickupLocation);
    }
}
