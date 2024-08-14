package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.ExpectedDeliveryDayDTO;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.mapper.ExpectedDeliveryDayMapper;
import com.tvdgapp.models.reference.Region;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.repositories.shipment.pricecaculation.ExpectedDeliveryDayRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import com.tvdgapp.services.reference.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpectedDeliveryDayService {
    private static final Logger logger = LoggerFactory.getLogger(ExpectedDeliveryDayService.class);

    private final ExpectedDeliveryDayRepository expectedDeliveryDayRepository;
    private final ShippingServiceRepository shippingServiceRepository;

    private final ExpectedDeliveryDayMapper expectedDeliveryDayMapper;

    @Autowired
    private RegionService regionService;

    public ExpectedDeliveryDayDTO createExpectedDeliveryDay(ExpectedDeliveryDayDTO dto) {
        Region region = regionService.findRegionById(Math.toIntExact(dto.getRegionId()));
        ExpectedDeliveryDay entity = new ExpectedDeliveryDay();
        entity.setRegion(region);
        entity.setDayRange(dto.getDayRange());
        //        // Fetch and set the ServicePortfolio if provided
        if (dto.getServiceId() != null) {
            ShippingService shippingService = shippingServiceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("ServicePortfolio not found"));
            entity.setShippingService(shippingService);
        }

        entity = expectedDeliveryDayRepository.save(entity);
        return convertToDTO(entity);
    }

    public ExpectedDeliveryDayDTO updateExpectedDeliveryDay(Long id, ExpectedDeliveryDayDTO dto) {
        ExpectedDeliveryDay entity = expectedDeliveryDayRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ExpectedDeliveryDay not found"));

        Region region = regionService.findRegionById(Math.toIntExact(dto.getRegionId()));
        entity.setRegion(region);
        entity.setDayRange(dto.getDayRange());
        //        // Fetch and set the ServicePortfolio if provided
        if (entity.getShippingService() != null) {
            ShippingService shippingService = shippingServiceRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("ServicePortfolio not found"));
            entity.setShippingService(shippingService);
        }

        entity = expectedDeliveryDayRepository.save(entity);
        return convertToDTO(entity);
    }

    // Other methods...

    private ExpectedDeliveryDayDTO convertToDTO(ExpectedDeliveryDay entity) {
        ExpectedDeliveryDayDTO dto = new ExpectedDeliveryDayDTO();
        dto.setId(entity.getId());
        dto.setRegionId(Long.valueOf(entity.getRegion().getRegionId()));
        dto.setDayRange(entity.getDayRange());
        if (entity.getShippingService() != null) {
            dto.setServiceId(entity.getShippingService().getServiceId());
        }
        return dto;
    }

//    public ExpectedDeliveryDayDTO createExpectedDeliveryDay(ExpectedDeliveryDayDTO expectedDeliveryDayDTO) {
////        logger.info("Creating ExpectedDeliveryDay for country: {}", expectedDeliveryDayDTO.getCountry());
//
//        ExpectedDeliveryDay expectedDeliveryDay = new ExpectedDeliveryDay();
////        expectedDeliveryDay.setCountry(expectedDeliveryDayDTO.getCountry());
////        expectedDeliveryDay.setRegion(expectedDeliveryDayDTO.getRegion());
//        expectedDeliveryDay.setDayRange(expectedDeliveryDayDTO.getDayRange());
//
//        // Fetch and set the ServicePortfolio if provided
//        if (expectedDeliveryDayDTO.getServicePortfolioId() != null) {
//            ServicePortfolio servicePortfolio = servicePortfolioRepository.findById(expectedDeliveryDayDTO.getServicePortfolioId())
//                    .orElseThrow(() -> new IllegalArgumentException("ServicePortfolio not found"));
//            expectedDeliveryDay.setServicePortfolio(servicePortfolio);
//        }
//
//        ExpectedDeliveryDay savedExpectedDeliveryDay = expectedDeliveryDayRepository.save(expectedDeliveryDay);
//        return toDTO(savedExpectedDeliveryDay);
//    }
//
//    private ExpectedDeliveryDayDTO toDTO(ExpectedDeliveryDay expectedDeliveryDay) {
//        ExpectedDeliveryDayDTO dto = new ExpectedDeliveryDayDTO();
//        dto.setId(expectedDeliveryDay.getId());
////        dto.setCountry(expectedDeliveryDay.getCountry());
////        dto.setRegion(expectedDeliveryDay.getRegion());
//        dto.setDayRange(expectedDeliveryDay.getDayRange());
//        if (expectedDeliveryDay.getServicePortfolio() != null) {
//            dto.setServicePortfolioId(expectedDeliveryDay.getServicePortfolio().getId());
//        }
//        return dto;
//    }
//
//
//    public ExpectedDeliveryDayDTO updateExpectedDeliveryDay(Long id, ExpectedDeliveryDayDTO expectedDeliveryDayDTO) {
//        ExpectedDeliveryDay expectedDeliveryDay = expectedDeliveryDayRepository.findById(id)
//                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ExpectedDeliveryDay not found with id " + id));
//
////        expectedDeliveryDay.setCountry(expectedDeliveryDayDTO.getCountry());
////        expectedDeliveryDay.setRegion(expectedDeliveryDayDTO.getRegion());
//        expectedDeliveryDay.setDayRange(expectedDeliveryDayDTO.getDayRange());
//
//        return expectedDeliveryDayMapper.toDTO(expectedDeliveryDayRepository.save(expectedDeliveryDay));
//    }

    public void deleteExpectedDeliveryDay(Long id) {
        ExpectedDeliveryDay expectedDeliveryDay = expectedDeliveryDayRepository.findById(id)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ExpectedDeliveryDay not found with id " + id));

        expectedDeliveryDayRepository.delete(expectedDeliveryDay);
        logger.info("Deleted ExpectedDeliveryDay with id: {}", id);
    }

    public ExpectedDeliveryDayDTO getExpectedDeliveryDayById(Long id) {
        ExpectedDeliveryDay expectedDeliveryDay = expectedDeliveryDayRepository.findById(id)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ExpectedDeliveryDay not found with id " + id));
        return expectedDeliveryDayMapper.toDTO(expectedDeliveryDay);
    }

    public List<ExpectedDeliveryDayDTO> getAllExpectedDeliveryDays() {
        return expectedDeliveryDayRepository.findAll().stream()
                .map(expectedDeliveryDayMapper::toDTO)
                .collect(Collectors.toList());
    }
}
