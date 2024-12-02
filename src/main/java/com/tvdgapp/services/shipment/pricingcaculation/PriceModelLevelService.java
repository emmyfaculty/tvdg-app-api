package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.CreatePriceModelLevelDto;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.PriceModelLevelMapper;
import com.tvdgapp.models.reference.Region;

import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingType;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PricingLevelRepository;
import com.tvdgapp.services.reference.RegionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceModelLevelService {

    private final PriceModelLevelRepository priceModelLevelRepository;
    private final PriceModelLevelMapper priceModelLevelMapper;
    private final RegionService regionService;
    private final PricingLevelRepository pricingLevelRepository;

    public CreatePriceModelLevelDto createPriceModelLevel(CreatePriceModelLevelDto createDto) {
                // Validate weight band start and end
        if (!isValidWeightBand(createDto)) {
            throw new IllegalArgumentException("Invalid weight band range.");
        }
        PriceModelLevel priceModelLevel = priceModelLevelMapper.toEntity(createDto);
        PriceModelLevel savedPriceModelLevel = priceModelLevelRepository.save(priceModelLevel);
        return priceModelLevelMapper.toDTO(savedPriceModelLevel);
    }

    public CreatePriceModelLevelDto getPriceModelLevelById(Long id) {
        PriceModelLevel priceModelLevel = priceModelLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PriceModelLevel not found"));
        return priceModelLevelMapper.toDTO(priceModelLevel);
    }

    public CreatePriceModelLevelDto updatePriceModelLevel(Long id, CreatePriceModelLevelDto updateDto) {
        PriceModelLevel priceModelLevel = priceModelLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PriceModelLevel not found"));
        Region region = regionService.findRegionById(Math.toIntExact(updateDto.getRegionId()));

        priceModelLevel.setWeightFrom(updateDto.getWeightFrom());
        priceModelLevel.setWeightTo(updateDto.getWeightTo());
        priceModelLevel.setBaseUnit(updateDto.getBaseUnit());
        priceModelLevel.setRegion(region);
//        if (updateDto.getNationWideRegionId() != null) {
//            NationWideRegion nationWideRegion = nationWideRegionRepository.findById(Long.valueOf(updateDto.getNationWideRegionId()))
//                    .orElseThrow(() -> new IllegalArgumentException("NationWideRegion not found"));
//            priceModelLevel.setNationWideRegion(nationWideRegion);
//        }
        priceModelLevel.setDescription(updateDto.getDescription());
        priceModelLevel.setRate(updateDto.getRate());
        priceModelLevel.setCurrencyCode(updateDto.getCurrencyCode() );
        if (updateDto.getLevelId() != null) {
            PricingLevel pricingLevel = pricingLevelRepository.findById(updateDto.getLevelId())
                    .orElseThrow(() -> new IllegalArgumentException("pricingModel not found"));
            priceModelLevel.setPricingLevel(pricingLevel);
        }
        if (updateDto.getServiceId() != null) {
            ShippingService shippingService = new ShippingService();
            shippingService.setServiceId(updateDto.getServiceId());
            priceModelLevel.setShippingService(shippingService);
        }
        priceModelLevel.setPricingType(PricingType.valueOf(updateDto.getPricingType()));

        PriceModelLevel updatedPriceModelLevel = priceModelLevelRepository.save(priceModelLevel);
        return priceModelLevelMapper.toDTO(updatedPriceModelLevel);
    }

    public void deletePriceModelLevel(Long id) {
        if (!priceModelLevelRepository.existsById(id)) {
            throw new ResourceNotFoundException("PriceModelLevel not found");
        }
        priceModelLevelRepository.deleteById(id);
    }

    public List<CreatePriceModelLevelDto> getAllPriceModelLevels() {
        return priceModelLevelRepository.findAll().stream()
                .map(priceModelLevelMapper::toDTO)
                .collect(Collectors.toList());
    }

        public List<CreatePriceModelLevelDto> getPriceModelLevelsByServicePortfolioId(Long servicePortfolioId) {
        return priceModelLevelRepository.findByShippingServiceServiceId(servicePortfolioId)
                .stream()
                .map(priceModelLevelMapper::toDTO)
                .collect(Collectors.toList());
    }

        private boolean isValidWeightBand(CreatePriceModelLevelDto priceModelLevelDTO) {
        BigDecimal weightBandStart = priceModelLevelDTO.getWeightFrom();
        BigDecimal weightBandEnd = priceModelLevelDTO.getWeightTo();
        Integer pricingModelId = priceModelLevelDTO.getLevelId();
        Integer regionId = priceModelLevelDTO.getRegionId();

        // Check if weightBandStart is equal to weightBandEnd of any existing range
        boolean isStartEqualToEnd = priceModelLevelRepository.existsByWeightToAndPricingLevelLevelIdAndRegionRegionId(weightBandStart, Long.valueOf(pricingModelId), Long.valueOf(regionId));
        if (isStartEqualToEnd) {
            return false;
        }

        // Check for overlapping ranges
        boolean isOverlapping = priceModelLevelRepository.existsOverlappingRange(weightBandStart, weightBandEnd, Long.valueOf(pricingModelId), Long.valueOf(regionId));
            return !isOverlapping;
        }
}

//public class PriceModelLevelService {
//    private static final Logger logger = LoggerFactory.getLogger(PriceModelLevelService.class);
//
//    @Autowired
//    private PriceModelLevelRepository priceModelLevelRepository;
//
//    @Autowired
//    private PriceModelLevelMapper priceModelLevelMapper;
//
//    @Autowired
//    PricingLevelRepository pricingLevelRepository;
//    @Autowired
//    private RegionService regionService;
//
////    public PriceModelLevelDTO createPriceModelLevel(PriceModelLevelDTO priceModelLevelDTO) {
////        // Validate weight band start and end
////        if (!isValidWeightBand(priceModelLevelDTO)) {
////            throw new IllegalArgumentException("Invalid weight band range.");
////        }
////
////        logger.info("Creating PriceModelLevel");
////        PriceModelLevel priceModelLevel = toEntity(priceModelLevelDTO);
////        priceModelLevel = priceModelLevelRepository.save(priceModelLevel);
////        return toDTO(priceModelLevel);
////    }
//
//    public PriceModelLevelDTO createPriceModelLevel(PriceModelLevelDTO dto) {
//        Region region = regionService.findRegionById(Math.toIntExact(dto.getRegionId()));
//        // Validate weight band start and end
//        if (!isValidWeightBand(dto)) {
//            throw new IllegalArgumentException("Invalid weight band range.");
//        }
//        PriceModelLevel entity = new PriceModelLevel();
//        entity.setWeightBandStart(dto.getWeightBandStart());
//        entity.setWeightBandEnd(dto.getWeightBandEnd());
//        entity.setUnits(Units.valueOf(dto.getUnits()));
//        entity.setRegion(region);
//        entity.setPrice(dto.getPrice());
//        entity.setDescription(dto.getDescription());
//        entity.setCurrency(dto.getCurrency());
//        entity.setTotalCostForWeightRange(dto.getTotalCostForWeightRange());
//        if (dto.getPricingLevelId() != null) {
//            PricingLevel priceModelLevel = pricingLevelRepository.findById(dto.getPricingLevelId())
//                    .orElseThrow(() -> new IllegalArgumentException("pricingModel not found"));
//            entity.setPricingLevel(priceModelLevel);
//        }
//        if (dto.getServiceId() != null) {
//            ShippingService shippingService = new ShippingService();
//            shippingService.setServiceId(dto.getServiceId());
//            entity.setShippingService(shippingService);
//        }
//
//        entity = priceModelLevelRepository.save(entity);
//        return convertToDTO(entity);
//    }
//
//    private boolean isValidWeightBand(PriceModelLevelDTO priceModelLevelDTO) {
//        BigDecimal weightBandStart = priceModelLevelDTO.getWeightBandStart();
//        BigDecimal weightBandEnd = priceModelLevelDTO.getWeightBandEnd();
//        Integer pricingModelId = priceModelLevelDTO.getPricingLevelId();
//        Long regionId = priceModelLevelDTO.getRegionId();
//
//        // Check if weightBandStart is equal to weightBandEnd of any existing range
//        boolean isStartEqualToEnd = priceModelLevelRepository.existsByWeightBandEndAndPricingLevelLevelIdAndRegionRegionId(weightBandStart, Long.valueOf(pricingModelId), regionId);
//        if (isStartEqualToEnd) {
//            return false;
//        }
//
//        // Check for overlapping ranges
//        boolean isOverlapping = priceModelLevelRepository.existsOverlappingRange(weightBandStart, weightBandEnd, Long.valueOf(pricingModelId), regionId);
//        if (isOverlapping) {
//            return false;
//        }
//
//        return true;
//    }
//
//
//    public PriceModelLevelDTO updatePriceModelLevel(Long id, PriceModelLevelDTO dto) {
//        PriceModelLevel entity = priceModelLevelRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("PriceModelLevel not found"));
//
//        Region region = regionService.findRegionById(Math.toIntExact(dto.getRegionId()));
//        entity.setWeightBandStart(dto.getWeightBandStart());
//        entity.setWeightBandEnd(dto.getWeightBandEnd());
//        entity.setUnits(Units.valueOf(dto.getUnits()));
//        entity.setRegion(region);
//        entity.setPrice(dto.getPrice());
//        entity.setDescription(dto.getDescription());
//        entity.setCurrency(dto.getCurrency());
//        entity.setTotalCostForWeightRange(dto.getTotalCostForWeightRange());
//        if (dto.getPricingLevelId() != null) {
//            PricingLevel priceModelLevel = pricingLevelRepository.findById(dto.getPricingLevelId())
//                    .orElseThrow(() -> new IllegalArgumentException("pricingModel not found"));
//            entity.setPricingLevel(priceModelLevel);
//        }
//        if (dto.getServiceId() != null) {
//            ShippingService shippingService = new ShippingService();
//            shippingService.setServiceId(dto.getServiceId());
//            entity.setShippingService(shippingService);
//        }
//
//        entity = priceModelLevelRepository.save(entity);
//        return convertToDTO(entity);
//    }
//
//    // Other methods...
//
//    private PriceModelLevelDTO convertToDTO(PriceModelLevel entity) {
//        PriceModelLevelDTO dto = new PriceModelLevelDTO();
//        dto.setId(entity.getId());
//        dto.setWeightBandStart(entity.getWeightBandStart());
//        dto.setWeightBandEnd(entity.getWeightBandEnd());
//        dto.setUnits(String.valueOf(entity.getUnits()));
//        dto.setRegionId(Long.valueOf(entity.getRegion().getRegionId()));
//        dto.setPrice(entity.getPrice());
//        dto.setCurrency(entity.getCurrency());
//        dto.setDescription(entity.getDescription());
//        dto.setTotalCostForWeightRange(entity.getTotalCostForWeightRange());
//        dto.setPricingLevelId(entity.getPricingLevel().getLevelId());
//        if (entity.getShippingService() != null) {
//            dto.setServiceId(entity.getShippingService().getServiceId());
//        }
//
//        return dto;
//    }
//
//    private PriceModelLevelDTO toDTO(PriceModelLevel priceModelLevel) {
//        if (priceModelLevel == null) {
//            return null;
//        }
//
//        PriceModelLevelDTO dto = new PriceModelLevelDTO();
//        dto.setId(priceModelLevel.getId());
//        dto.setWeightBandStart(priceModelLevel.getWeightBandStart());
//        dto.setWeightBandEnd(priceModelLevel.getWeightBandEnd());
//        dto.setUnits(priceModelLevel.getUnits().name()); // Assuming Units is an enum
//        dto.setRegionId(Long.valueOf(priceModelLevel.getRegion().getRegionId()));
//        dto.setPrice(priceModelLevel.getPrice());
//        dto.setPricingLevelId(priceModelLevel.getPricingLevel().getLevelId());
//        dto.setTotalCostForWeightRange(priceModelLevel.getTotalCostForWeightRange());
//        if (priceModelLevel.getShippingService() != null) {
//            dto.setServiceId(priceModelLevel.getShippingService().getServiceId());
//        }
//        return dto;
//    }
//
//    private PriceModelLevel toEntity(PriceModelLevelDTO priceModelLevelDTO) {
//        if (priceModelLevelDTO == null) {
//            return null;
//        }
//
//        PriceModelLevel entity = new PriceModelLevel();
//        entity.setId(priceModelLevelDTO.getId());
//        entity.setWeightBandStart(priceModelLevelDTO.getWeightBandStart());
//        entity.setWeightBandEnd(priceModelLevelDTO.getWeightBandEnd());
//        entity.setUnits(Units.valueOf(priceModelLevelDTO.getUnits())); // Assuming Units is an enum
//        entity.setPrice(priceModelLevelDTO.getPrice());
//        // Fetch and set the ServicePortfolio if provided
//        if (priceModelLevelDTO.getPricingLevelId() != null) {
//            PricingLevel priceModelLevel = pricingLevelRepository.findById(priceModelLevelDTO.getPricingLevelId())
//                    .orElseThrow(() -> new IllegalArgumentException("pricingModel not found"));
//            entity.setPricingLevel(priceModelLevel);
//        }
//        entity.setTotalCostForWeightRange(priceModelLevelDTO.getTotalCostForWeightRange());
//        if (priceModelLevelDTO.getServiceId() != null) {
//            ShippingService shippingService = new ShippingService();
//            shippingService.setServiceId(priceModelLevelDTO.getServiceId());
//            entity.setShippingService(shippingService);
//        }
//        return entity;
//    }
//    public void deletePriceModelLevel(Long id) {
//        PriceModelLevel priceModelLevel = priceModelLevelRepository.findById(id)
//                .orElseThrow(() -> new TvdgException.EntityNotFoundException("PriceModelLevel not found with id " + id));
//
//        priceModelLevelRepository.delete(priceModelLevel);
//        logger.info("Deleted PriceModelLevel with id: {}", id);
//    }
//
//    public PriceModelLevelDTO getPriceModelLevelById(Long id) {
//        PriceModelLevel priceModelLevel = priceModelLevelRepository.findById(id)
//                .orElseThrow(() -> new TvdgException.EntityNotFoundException("PriceModelLevel not found with id " + id));
//        return priceModelLevelMapper.toDTO(priceModelLevel);
//    }
//
//    public List<PriceModelLevelDTO> getAllPriceModelLevels() {
//        return priceModelLevelRepository.findAll().stream()
//                .map(priceModelLevelMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public List<PriceModelLevelDTO> getPriceModelLevelsByServicePortfolioId(Long servicePortfolioId) {
//        return priceModelLevelRepository.findByShippingServiceServiceId(servicePortfolioId)
//                .stream()
//                .map(priceModelLevelMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//}