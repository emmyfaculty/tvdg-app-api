package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.CreateShippingServiceDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceResponseDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.UpdateShippingServiceDto;
import com.tvdgapp.exceptions.ServiceException;
import com.tvdgapp.mapper.ShippingServiceMapper;
import com.tvdgapp.models.shipment.ServiceType;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingServiceService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingServiceService.class);

    private final ShippingServiceRepository shippingServiceRepository;

    public ShippingServiceResponseDto createShippingService(CreateShippingServiceDto dto) {
        ShippingService shippingService = ShippingServiceMapper.toEntity(dto);
        shippingService.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        shippingService.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ShippingService savedShippingService = shippingServiceRepository.save(shippingService);
        logger.info("Created ShippingService with ID: {}", savedShippingService.getServiceId());
        return ShippingServiceMapper.toDto(savedShippingService);
    }

    public ShippingServiceResponseDto updateShippingService(UpdateShippingServiceDto dto) {
        ShippingService existingService = shippingServiceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new ServiceException("ShippingService not found for ID: " + dto.getServiceId()));

        ShippingService updatedService = ShippingServiceMapper.toEntity(dto);
        updatedService.setCreatedAt(existingService.getCreatedAt());
        updatedService.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ShippingService savedShippingService = shippingServiceRepository.save(updatedService);
        logger.info("Updated ShippingService with ID: {}", savedShippingService.getServiceId());
        return ShippingServiceMapper.toDto(savedShippingService);
    }

    public void deleteShippingService(Integer serviceId) {
        ShippingService shippingService = shippingServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceException("ShippingService not found for ID: " + serviceId));
        shippingServiceRepository.delete(shippingService);
        logger.info("Deleted ShippingService with ID: {}", serviceId);
    }

    public ShippingServiceResponseDto getShippingServiceById(Integer serviceId) {
        ShippingService shippingService = shippingServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ServiceException("ShippingService not found for ID: " + serviceId));
        return ShippingServiceMapper.toDto(shippingService);
    }

    public List<ShippingServiceResponseDto> getAllShippingServices() {
        List<ShippingService> shippingServices = shippingServiceRepository.findAll();
        return shippingServices.stream()
                .map(ShippingServiceMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ShippingServiceResponseDto> getShippingServicesByType(ServiceType type) {
        List<ShippingService> shippingServices = shippingServiceRepository.findByType(type);
        if (shippingServices.isEmpty()) {
            throw new ServiceException("No ShippingServices found for type: " + type);
        }
        return shippingServices.stream()
                .map(ShippingServiceMapper::toDto)
                .collect(Collectors.toList());
    }
}
