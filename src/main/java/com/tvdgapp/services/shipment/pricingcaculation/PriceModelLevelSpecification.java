package com.tvdgapp.services.shipment.pricingcaculation;

import com.tvdgapp.dtos.shipment.Units;
import com.tvdgapp.models.shipment.pricingcaculation.PriceModelLevel;
import com.tvdgapp.models.user.customer.PricingCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PriceModelLevelSpecification {

//    public static Specification<PriceModelLevel> belongsToServicePortfolio(Long servicePortfolioId) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if servicePortfolio.id equals " + servicePortfolioId);
//            return criteriaBuilder.equal(root.get("servicePortfolio").get("id"), servicePortfolioId);
//        };
//    }
//
//    public static Specification<PriceModelLevel> weightBandMatches(BigDecimal shipmentWeight) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if weightBandStart <= " + shipmentWeight + " and weightBandEnd >= " + shipmentWeight);
//            return criteriaBuilder.and(
//                    criteriaBuilder.lessThanOrEqualTo(root.get("weightBandStart"), shipmentWeight),
//                    criteriaBuilder.greaterThanOrEqualTo(root.get("weightBandEnd"), shipmentWeight)
//
//            );
//
//        };
//    }
//
//    public static Specification<PriceModelLevel> regionMatches(String region) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if region equals " + region);
//            return criteriaBuilder.equal(root.get("region"), region);
//        };
//    }
//
//    public static Specification<PriceModelLevel> countryMatches(String country) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if country equals " + country);
//            return criteriaBuilder.equal(root.get("country"), country);
//        };
//    }
//
//    public static Specification<PriceModelLevel> unitMatches(Units units) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if units equals " + units);
//            return criteriaBuilder.equal(root.get("units"), units);
//        };
//    }    public static Specification<PriceModelLevel> customerTypeMatches(PricingCategory pricingCategory) {
//        return (root, query, criteriaBuilder) -> {
//            System.out.println("Checking if pricingModel.priceCategory equals " + pricingCategory);
//            return criteriaBuilder.equal(root.get("pricingModel").get("priceCategory"), pricingCategory);
//        };
//    }

    public static Specification<PriceModelLevel> belongsToServicePortfolio(Long serviceId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shippingService").get("serviceId"), serviceId);
    }

    public static Specification<PriceModelLevel> weightBandMatches(BigDecimal shipmentWeight) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.lessThanOrEqualTo(root.get("weightFrom"), shipmentWeight),
                criteriaBuilder.or(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("weightTo"), shipmentWeight),
                        criteriaBuilder.isNull(root.get("weightTo"))
                )
        );
    }

    public static Specification<PriceModelLevel> regionMatches(String region) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("region").get("regionName"), region);
    }

    public static Specification<PriceModelLevel> customerTypeMatches(Integer pricingLevelId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pricingLevel").get("levelId"), pricingLevelId);
    }

    public static Specification<PriceModelLevel> unitMatches(String units) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("units"), units);
    }
}
