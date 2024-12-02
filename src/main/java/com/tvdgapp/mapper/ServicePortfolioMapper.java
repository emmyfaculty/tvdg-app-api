package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceDTO;
import com.tvdgapp.models.shipment.pricingcaculation.ExpectedDeliveryDay;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.ShippingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PriceModelLevelMapper.class, ExpectedDeliveryDayMapper.class})
public interface ServicePortfolioMapper {
    ServicePortfolioMapper INSTANCE = Mappers.getMapper(ServicePortfolioMapper.class);

    @Mapping(target = "expectedDeliveryDays", source = "entity.expectedDeliveryDays", qualifiedByName = "expectedDeliveryDayIds")
    @Mapping(target = "priceModelLevels", source = "entity.priceModelLevels", qualifiedByName = "priceModelLevelIds")
    ShippingServiceDTO toDto(ShippingService entity);

    @Mapping(target = "expectedDeliveryDays", ignore = true) // Ignore mapping for now
    @Mapping(target = "priceModelLevels", ignore = true) // Ignore mapping for now
    ShippingService toEntity(ShippingServiceDTO dto);

    @Named("expectedDeliveryDayIds")
    default List<Long> mapExpectedDeliveryDaysToIds(List<ExpectedDeliveryDay> expectedDeliveryDays) {
        return expectedDeliveryDays.stream()
                .map(ExpectedDeliveryDay::getId)
                .collect(Collectors.toList());
    }

    @Named("priceModelLevelIds")
    default List<Integer> mapPriceModelLevelsToIds(List<PriceModelLevel> priceModelLevels) {
        return priceModelLevels.stream()
                .map(PriceModelLevel::getPricingId)
                .collect(Collectors.toList());
    }

}