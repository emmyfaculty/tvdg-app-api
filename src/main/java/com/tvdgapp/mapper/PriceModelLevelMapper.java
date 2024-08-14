package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.CreatePriceModelLevelDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.PriceModelLevelDTO;
import com.tvdgapp.dtos.shipment.pricingcaculation.UpdatePriceModelLevelDto;
import com.tvdgapp.models.reference.Region;
import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PriceModelLevelMapper {
    @Mapping(source = "region.regionId", target = "regionId")
    @Mapping(source = "nationWideRegion.id", target = "nationWideRegionId")
    @Mapping(source = "shippingService.serviceId", target = "serviceId")
    @Mapping(source = "pricingLevel.levelId", target = "pricingLevelId")
    PriceModelLevelDTO toDto(PriceModelLevel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auditSection", ignore = true)
    @Mapping(target = "region", source = "regionId")
    @Mapping(target = "nationWideRegion", source = "nationWideRegionId")
    @Mapping(target = "shippingService", source = "serviceId")
    @Mapping(target = "pricingLevel", source = "pricingLevelId")
    PriceModelLevel toEntity(CreatePriceModelLevelDto createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auditSection", ignore = true)
    @Mapping(target = "region", source = "regionId")
    @Mapping(target = "nationWideRegion", source = "nationWideRegionId")
    @Mapping(target = "shippingService", source = "serviceId")
    @Mapping(target = "pricingLevel", source = "pricingLevelId")
    PriceModelLevel toEntity(UpdatePriceModelLevelDto updateDto);

    default Region mapRegion(Integer regionId) {
        if (regionId == null) {
            return null;
        }
        Region region = new Region();
        region.setRegionId(regionId);
        return region;
    }

    default NationWideRegion mapNationWideRegion(Long nationWideRegionId) {
        if (nationWideRegionId == null) {
            return null;
        }
        NationWideRegion nationWideRegion = new NationWideRegion();
        nationWideRegion.setId(nationWideRegionId);
        return nationWideRegion;
    }

    default ShippingService mapShippingService(Integer   serviceId) {
        if (serviceId == null) {
            return null;
        }
        ShippingService shippingService = new ShippingService();
        shippingService.setServiceId(serviceId);
        return shippingService;
    }

    default PricingLevel mapPricingLevel(Integer pricingLevelId) {
        if (pricingLevelId == null) {
            return null;
        }
        PricingLevel pricingLevel = new PricingLevel();
        pricingLevel.setLevelId(pricingLevelId);
        return pricingLevel;
    }
}
