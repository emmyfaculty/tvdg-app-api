//package com.tvdgapp.controllers.shipment.pricingcalculation;
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.shipment.pricingcaculation.ExpectedDeliveryDayDTO;
//import com.tvdgapp.dtos.shipment.pricingcaculation.ShippingServiceDTO;
//import com.tvdgapp.services.shipment.pricingcaculation.ServicePortfolioServiceImp;
//import com.tvdgapp.utils.ApiResponseUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/service-portfolios")
//public class ServicePortfolioController {
//    private final ServicePortfolioServiceImp servicePortfolioServiceImp;
//
//    @PostMapping
//    public ResponseEntity<ApiDataResponse<ShippingServiceDTO>> createServicePortfolio(@RequestBody ShippingServiceDTO shippingServiceDTO) {
//        ShippingServiceDTO createdPortfolio = servicePortfolioServiceImp.createServicePortfolio(shippingServiceDTO);
//        return ApiResponseUtils.response(HttpStatus.CREATED, createdPortfolio, "Resource created successfully");
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiDataResponse<List<ShippingServiceDTO>>> getAllServicePortfolios() {
//        List<ShippingServiceDTO> list = servicePortfolioServiceImp.getAllServicePortfolios();
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiDataResponse<ShippingServiceDTO>> getServicePortfolioById(@PathVariable Integer id) {
//        ShippingServiceDTO portfolio = servicePortfolioServiceImp.getServicePortfolioById(id);
//        return ApiResponseUtils.response(HttpStatus.OK, portfolio, "Resources retrieved successfully");
//    }
//
//    @GetMapping("/import")
//    public ResponseEntity<ApiDataResponse<List<ShippingServiceDTO>>> getServicePortfoliosStartingWithImport() {
//        List<ShippingServiceDTO> list = servicePortfolioServiceImp.getServicePortfoliosStartingWithImport();
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//    }
//
//    @GetMapping("/export")
//    public ResponseEntity<ApiDataResponse<List<ShippingServiceDTO>>> getServicePortfoliosStartingWithExport() {
//        List<ShippingServiceDTO> list = servicePortfolioServiceImp.getServicePortfoliosStartingWithExport();
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//    }
//
//    @GetMapping("/{id}/expected-delivery-days")
//    public ResponseEntity<ApiDataResponse<List<ExpectedDeliveryDayDTO>>> getExpectedDeliveryDays(@PathVariable Long id) {
//        List<ExpectedDeliveryDayDTO> list = servicePortfolioServiceImp.getExpectedDeliveryDays(id);
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//
//    }
//
//    @PutMapping
//    public ResponseEntity<ApiDataResponse<ShippingServiceDTO>> updateServicePortfolio(/*@PathVariable Long id,*/ @RequestBody ShippingServiceDTO shippingServiceDTO) {
//        ShippingServiceDTO updatedPortfolio = servicePortfolioServiceImp.updateServicePortfolio(shippingServiceDTO);
//        return ApiResponseUtils.response(HttpStatus.OK, updatedPortfolio, "Resources updated successfully");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiDataResponse<Object>> deleteServicePortfolio(@PathVariable Integer id) {
//        servicePortfolioServiceImp.deleteServicePortfolio(id);
//        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
//    }
//
//    @PostMapping("/{servicePortfolioId}/price-model-level/{priceModelLevelId}")
//    public ResponseEntity<ApiDataResponse<Object>> assignPriceModelLevelToServicePortfolio(@PathVariable Integer servicePortfolioId, @PathVariable Long priceModelLevelId) {
//        servicePortfolioServiceImp.assignPriceModelLevelToServicePortfolio(servicePortfolioId, priceModelLevelId);
//        return ApiResponseUtils.response(HttpStatus.OK, "Resource assigned successfully");
//    }
//
//    @PostMapping("/{servicePortfolioId}/expected-delivery-days/{expectedDeliveryDayId}")
//    public ResponseEntity<ApiDataResponse<Object>> assignExpectedDeliveryDayToServicePortfolio(
//            @PathVariable Integer servicePortfolioId,
//            @PathVariable Long expectedDeliveryDayId) {
//        servicePortfolioServiceImp.assignExpectedDeliveryDayToServicePortfolio(servicePortfolioId, expectedDeliveryDayId);
//        return ApiResponseUtils.response(HttpStatus.OK, "Resource assigned successfully");
//    }
//}
