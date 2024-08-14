package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.ExpectedDeliveryDayDTO;
import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceDTO;
import com.tvdgapp.exceptions.TvdgException;
import com.tvdgapp.mapper.ExpectedDeliveryDayMapper;
import com.tvdgapp.mapper.ServicePortfolioMapper;
import com.tvdgapp.models.shipment.pricingcaculation.Carrier;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import com.tvdgapp.repositories.shipment.pricecaculation.ExpectedDeliveryDayRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.ShippingServiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicePortfolioServiceImp {
    private static final Logger logger = LoggerFactory.getLogger(ServicePortfolioService.class);

    private final ShippingServiceRepository shippingServiceRepository;

    private final ServicePortfolioMapper servicePortfolioMapper;
    private final ExpectedDeliveryDayMapper expectedDeliveryDayMapper;

    private final PriceModelLevelRepository priceModelLevelRepository;

    private final ExpectedDeliveryDayRepository expectedDeliveryDayRepository;



    public ShippingServiceDTO createServicePortfolio(ShippingServiceDTO shippingServiceDTO) {
        ShippingService shippingService = servicePortfolioMapper.toEntity(shippingServiceDTO);

        // Add PriceModelLevels and ExpectedDeliveryDays
        addPriceModelLevels(shippingService, shippingServiceDTO.getPriceModelLevels());
        addExpectedDeliveryDays(shippingService, shippingServiceDTO.getExpectedDeliveryDays());

        shippingService = shippingServiceRepository.save(shippingService);

        return servicePortfolioMapper.toDto(shippingService);
    }

    public ShippingServiceDTO updateServicePortfolio(ShippingServiceDTO shippingServiceDTO) {
        ShippingService existingShippingService = shippingServiceRepository.findById(shippingServiceDTO.getId())
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found"));

        existingShippingService.setServiceName(shippingServiceDTO.getServiceName());
        existingShippingService.setCarrier(Carrier.valueOf(shippingServiceDTO.getCarrier()));
        existingShippingService.setType(shippingServiceDTO.getServiceType());

        try {
            existingShippingService.setType(shippingServiceDTO.getServiceType());
        } catch (IllegalArgumentException e) {
            // Handle the case where the enum constant is not found
            throw new IllegalArgumentException("Invalid ServiceType: " + shippingServiceDTO.getServiceType(), e);
        }
        // Clear existing PriceModelLevels and set new ones
        existingShippingService.getPriceModelLevels().clear();
        addPriceModelLevels(existingShippingService, shippingServiceDTO.getPriceModelLevels());

        // Clear existing ExpectedDeliveryDays and set new ones
        existingShippingService.getExpectedDeliveryDays().clear();
        addExpectedDeliveryDays(existingShippingService, shippingServiceDTO.getExpectedDeliveryDays());

        existingShippingService = shippingServiceRepository.save(existingShippingService);

        return servicePortfolioMapper.toDto(existingShippingService);
    }

    public void addPriceModelLevels(ShippingService shippingService, List<Long> priceModelLevelIds) {
        List<PriceModelLevel> priceModelLevels = priceModelLevelIds.stream()
                .map(id -> priceModelLevelRepository.findById(id)
                        .orElseThrow(() -> new TvdgException.EntityNotFoundException("PriceModelLevel not found with id " + id)))
                .collect(Collectors.toList());
        shippingService.setPriceModelLevels(priceModelLevels);
    }

    public void addExpectedDeliveryDays(ShippingService shippingService, List<Long> expectedDeliveryDayIds) {
        List<ExpectedDeliveryDay> expectedDeliveryDays = expectedDeliveryDayIds.stream()
                .map(id -> expectedDeliveryDayRepository.findById(id)
                        .orElseThrow(() -> new TvdgException.EntityNotFoundException("ExpectedDeliveryDay not found with id " + id)))
                .collect(Collectors.toList());
        shippingService.setExpectedDeliveryDays(expectedDeliveryDays);
    }
    public void deleteServicePortfolio(Integer id) {
        ShippingService shippingService = shippingServiceRepository.findById(id)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found with id " + id));

        shippingServiceRepository.delete(shippingService);
        logger.info("Deleted ServicePortfolio with id: {}", id);
    }

    public ShippingServiceDTO getServicePortfolioById(Integer id) {
        ShippingService shippingService = shippingServiceRepository.findById(id)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found with id " + id));
        return servicePortfolioMapper.toDto(shippingService);
    }

    public List<ShippingServiceDTO> getAllServicePortfolios() {
        return shippingServiceRepository.findAll().stream()
                .map(servicePortfolioMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<ShippingServiceDTO> getServicePortfoliosStartingWithImport() {
        return shippingServiceRepository.findAll().stream()
                .filter(sp -> sp.getServiceName().split(" ")[0].equalsIgnoreCase("IMPORT"))
                .map(servicePortfolioMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ShippingServiceDTO> getServicePortfoliosStartingWithExport() {
        return shippingServiceRepository.findAll().stream()
                .filter(sp -> sp.getServiceName().split(" ")[0].equalsIgnoreCase("EXPORT"))
                .map(servicePortfolioMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ExpectedDeliveryDayDTO> getExpectedDeliveryDays(Integer servicePortfolioId) {
        return expectedDeliveryDayRepository.findByShippingServiceServiceId(servicePortfolioId)
                .stream()
                .map(expectedDeliveryDayMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void assignPriceModelLevelToServicePortfolio(Integer servicePortfolioId, Long priceModelLevelId) {
        ShippingService shippingService = shippingServiceRepository.findById(servicePortfolioId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found with id " + servicePortfolioId));
        PriceModelLevel priceModelLevel = priceModelLevelRepository.findById(priceModelLevelId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("PriceModelLevel not found with id " + priceModelLevelId));

        priceModelLevel.setShippingService(shippingService);
        priceModelLevelRepository.save(priceModelLevel);
    }

    public void assignExpectedDeliveryDayToServicePortfolio(Integer servicePortfolioId, Long expectedDeliveryDayId) {
        ShippingService shippingService = shippingServiceRepository.findById(servicePortfolioId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ServicePortfolio not found with id " + servicePortfolioId));
        ExpectedDeliveryDay expectedDeliveryDay = expectedDeliveryDayRepository.findById(expectedDeliveryDayId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("ExpectedDeliveryDay not found with id " + expectedDeliveryDayId));

        expectedDeliveryDay.setShippingService(shippingService);
        expectedDeliveryDayRepository.save(expectedDeliveryDay);
    }
}