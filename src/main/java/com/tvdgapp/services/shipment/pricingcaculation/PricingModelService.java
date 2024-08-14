package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.PricingModelDTO;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.repositories.shipment.pricecaculation.PricingLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingModelService {

    private final PricingLevelRepository pricingLevelRepository;

    // Create
    public PricingModelDTO createPricingModel(PricingModelDTO pricingModelDTO) {
        PricingLevel pricingLevel = new PricingLevel();
        pricingLevel.setLevelName(pricingModelDTO.getLevelName());
        pricingLevel.setDescription(pricingModelDTO.getDescription());  // New field
        
        PricingLevel savedModel = pricingLevelRepository.save(pricingLevel);
        return convertToDTO(savedModel);
    }

    // Read all
    public List<PricingModelDTO> getAllPricingModels() {
        List<PricingLevel> models = pricingLevelRepository.findAll();
        return models.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Read by ID
    public PricingModelDTO getPricingModelById(Integer id) {
        PricingLevel model = pricingLevelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PricingModel id " + id));
        return convertToDTO(model);
    }

    // Update
    public PricingModelDTO updatePricingModel(Integer id, PricingModelDTO pricingModelDTO) {
        PricingLevel pricingLevel = pricingLevelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PricingModel id " + id));
        pricingLevel.setLevelName(pricingModelDTO.getLevelName());
        pricingLevel.setDescription(pricingModelDTO.getDescription());  // New field

        PricingLevel updatedModel = pricingLevelRepository.save(pricingLevel);
        return convertToDTO(updatedModel);
    }

    // Delete
    public void deletePricingModel(Integer id) {
        PricingLevel pricingLevel = pricingLevelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("PricingModel id " + id));
        pricingLevelRepository.delete(pricingLevel);
    }

    private PricingModelDTO convertToDTO(PricingLevel pricingLevel) {
        PricingModelDTO dto = new PricingModelDTO();
        dto.setId(Long.valueOf(pricingLevel.getLevelId()));
        dto.setLevelName(pricingLevel.getLevelName());
        dto.setDescription(pricingLevel.getDescription());  // New field
        return dto;
    }
}
