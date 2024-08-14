package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelRequestDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelResponseDto;
import com.tvdgapp.exceptions.ServiceException;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PricingLevelMapper {

    public static PricingLevel toEntity(PricingLevelRequestDto dto, PriceModelLevelRepository priceModelLevelRepository) {
        PricingLevel pricingLevel = new PricingLevel();
        pricingLevel.setLevelName(dto.getLevelName());
        pricingLevel.setDescription(dto.getDescription());

        if (dto.getPriceModelLevelIds() != null) {
            List<PriceModelLevel> priceModelLevels = dto.getPriceModelLevelIds().stream()
                    .map(id -> priceModelLevelRepository.findById(Long.valueOf(id))
                            .orElseThrow(() -> new ServiceException("PriceModelLevel not found with id: " + id)))
                    .collect(Collectors.toList());
            pricingLevel.setPriceModelLevels(priceModelLevels);
        }

        return pricingLevel;
    }

    public static PricingLevelResponseDto toDto(PricingLevel entity) {
//        List<Long> priceModelLevelIds = entity.getPriceModelLevels().stream()
//                .map(PriceModelLevel::getId)
//                .collect(Collectors.toList());

        return new PricingLevelResponseDto(
                entity.getLevelId(),
                entity.getLevelName(),
                entity.getDescription()
//                priceModelLevelIds
        );
    }

    public static void updateEntityFromDto(PricingLevelRequestDto dto, PricingLevel entity, PriceModelLevelRepository priceModelLevelRepository) {
        entity.setLevelName(dto.getLevelName());
        entity.setDescription(dto.getDescription());

        if (dto.getPriceModelLevelIds() != null) {
            List<PriceModelLevel> priceModelLevels = dto.getPriceModelLevelIds().stream()
                    .map(id -> priceModelLevelRepository.findById(Long.valueOf(id))
                            .orElseThrow(() -> new ServiceException("PriceModelLevel not found with id: " + id)))
                    .collect(Collectors.toList());
            entity.setPriceModelLevels(priceModelLevels);
        }
    }
}
