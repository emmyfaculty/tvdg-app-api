package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServicePortfolioService {


    private final PriceModelLevelRepository priceModelLevelRepository;

    @Autowired
    public ServicePortfolioService(PriceModelLevelRepository priceModelLevelRepository ) {
        this.priceModelLevelRepository = priceModelLevelRepository;
    }

    public Optional<PriceModelLevel> getPriceModelLevelForShipment(Long serviceId, BigDecimal shipmentWeight, String shipmentCountry, Integer pricingLevelId, String units) {
        String region = getRegionByCountry(serviceId, shipmentCountry);
        Specification<PriceModelLevel> spec = Specification
                .where(PriceModelLevelSpecification.belongsToServicePortfolio(serviceId))
                .and(PriceModelLevelSpecification.weightBandMatches(shipmentWeight))
                .and(PriceModelLevelSpecification.regionMatches(region))
                .and(PriceModelLevelSpecification.customerTypeMatches(pricingLevelId))
                .and(PriceModelLevelSpecification.unitMatches(units));

        return priceModelLevelRepository.findOne(spec);
    }

    private String getRegionByCountry(Long serviceId, String country) {
        List<PriceModelLevel> priceModelLevels = priceModelLevelRepository.findByShippingService_ServiceId(serviceId);

        Optional<PriceModelLevel> priceModelLevel = priceModelLevels.stream()
                .filter(pml -> pml.getRegion().getCountries().stream()
                        .anyMatch(c -> c.getName().equalsIgnoreCase(country.trim())))
                .findFirst();

        return priceModelLevel
                .map(pml -> pml.getRegion().getRegionName())
                .orElse("Unknown Region");
    }

}
