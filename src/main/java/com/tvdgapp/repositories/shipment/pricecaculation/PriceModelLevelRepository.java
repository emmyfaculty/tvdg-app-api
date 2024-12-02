package com.tvdgapp.repositories.shipment.pricecaculation;

//import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.user.customer.PricingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceModelLevelRepository extends JpaRepository<PriceModelLevel, Long>, JpaSpecificationExecutor<PriceModelLevel> {
    boolean existsByWeightToAndPricingLevelLevelIdAndRegionRegionId(BigDecimal weightBandEnd, Long pricingModelId, Long regionId);

    @Query("SELECT CASE WHEN COUNT(pml) > 0 THEN true ELSE false END " +
            "FROM PriceModelLevel pml " +
            "WHERE pml.pricingLevel.levelId = :pricingLevelId " +
            "AND ((:weightBandStart BETWEEN pml.weightFrom AND pml.weightTo" +
            ") " +
            "OR (:weightBandEnd BETWEEN pml.weightFrom AND pml.weightTo) " +
            "OR (pml.weightFrom BETWEEN :weightBandStart AND :weightBandEnd) " +
            "OR (pml.weightTo BETWEEN :weightBandStart AND :weightBandEnd))" +
            "AND (pml.region.regionId = :regionId)")
    boolean existsOverlappingRange(@Param("weightBandStart") BigDecimal weightBandStart,
                                   @Param("weightBandEnd") BigDecimal weightBandEnd,
                                   @Param("pricingLevelId") Long pricingLevelId,
                                   @Param("regionId") Long regionId);

    List<PriceModelLevel> findByShippingServiceServiceId(Long servicePortfolioId);

    List<PriceModelLevel> findByShippingService_ServiceId(Long serviceId);

    @Query("SELECT pml FROM PriceModelLevel pml JOIN pml.pricingLevel pm WHERE pml.shippingService.serviceId = :servicePortfolioId AND (:weightToUse BETWEEN pml.weightFrom AND pml.weightTo OR pml.weightFrom = :weightToUse OR pml.weightTo = :weightToUse) AND pml.region = :region AND pm.levelId = :pricingCategory")
    Optional<PriceModelLevel> findPriceModelLevel(@Param("servicePortfolioId") Long servicePortfolioId, @Param("weightToUse") double weightToUse, @Param("region") String region, @Param("pricingCategory") PricingCategory pricingCategory);

//    List<PriceModelLevel> findByRegion(NationWideRegion region);
//
//    List<PriceModelLevel> findByNationWideRegionAndShippingService_serviceIdAndPricingLevel_levelId(
//            NationWideRegion nationWideRegion, Long serviceId, Long pricingLevelId);
}