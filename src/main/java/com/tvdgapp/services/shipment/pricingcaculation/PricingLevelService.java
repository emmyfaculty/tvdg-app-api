package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelRequestDto;
import com.tvdgapp.dtos.shipment.pricingcaculation.PricingLevelResponseDto;
import com.tvdgapp.exceptions.ServiceException;
import com.tvdgapp.mapper.PricingLevelMapper;
import com.tvdgapp.models.shipment.pricingcaculation.PricingLevel;
import com.tvdgapp.repositories.shipment.pricecaculation.PriceModelLevelRepository;
import com.tvdgapp.repositories.shipment.pricecaculation.PricingLevelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PricingLevelService {

    private static final Logger logger = LoggerFactory.getLogger(PricingLevelService.class);

    private final PricingLevelRepository pricingLevelRepository;

    private final PriceModelLevelRepository priceModelLevelRepository;

    public PricingLevelResponseDto createPricingLevel(PricingLevelRequestDto requestDto) {
        PricingLevel pricingLevel = PricingLevelMapper.toEntity(requestDto, priceModelLevelRepository);
        pricingLevel = pricingLevelRepository.save(pricingLevel);
        logger.info("Created PricingLevel: {}", pricingLevel);
        return PricingLevelMapper.toDto(pricingLevel);
    }

    public List<PricingLevelResponseDto> getAllPricingLevels() {
        List<PricingLevel> pricingLevels = pricingLevelRepository.findAll();
        return pricingLevels.stream()
                .map(PricingLevelMapper::toDto)
                .collect(Collectors.toList());
    }

    public PricingLevelResponseDto getPricingLevelById(Integer levelId) {
        PricingLevel pricingLevel = pricingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ServiceException("PricingLevel not found with id: " + levelId));
        return PricingLevelMapper.toDto(pricingLevel);
    }

    public PricingLevelResponseDto updatePricingLevel(Integer levelId, PricingLevelRequestDto requestDto) {
        PricingLevel pricingLevel = pricingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ServiceException("PricingLevel not found with id: " + levelId));
        PricingLevelMapper.updateEntityFromDto(requestDto, pricingLevel, priceModelLevelRepository);
        pricingLevel = pricingLevelRepository.save(pricingLevel);
        logger.info("Updated PricingLevel: {}", pricingLevel);
        return PricingLevelMapper.toDto(pricingLevel);
    }

    public void deletePricingLevel(Integer levelId) {
        PricingLevel pricingLevel = pricingLevelRepository.findById(levelId)
                .orElseThrow(() -> new ServiceException("PricingLevel not found with id: " + levelId));
        pricingLevelRepository.delete(pricingLevel);
        logger.info("Deleted PricingLevel: {}", pricingLevel);
    }
}
