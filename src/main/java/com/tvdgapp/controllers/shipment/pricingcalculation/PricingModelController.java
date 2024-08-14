//package com.tvdgapp.controllers.shipment.pricingcalculation;
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.shipment.pricingcaculation.PricingModelDTO;
//import com.tvdgapp.services.shipment.pricingcaculation.PricingModelService;
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
//@RequestMapping("/api/v1/pricing-models")
//public class PricingModelController {
//    private final PricingModelService pricingModelService;
//
//    @PostMapping
//    public ResponseEntity<ApiDataResponse<PricingModelDTO>> createPricingModel(@RequestBody PricingModelDTO pricingModelDTO) {
//        PricingModelDTO createdModel = pricingModelService.createPricingModel(pricingModelDTO);
//        return ApiResponseUtils.response(HttpStatus.CREATED, createdModel, "Resource created successfully");
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiDataResponse<List<PricingModelDTO>>> getAllPricingModels() {
//        List<PricingModelDTO>  list = pricingModelService.getAllPricingModels();
//        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiDataResponse<PricingModelDTO>> getPricingModelById(@PathVariable Integer id) {
//        PricingModelDTO model = pricingModelService.getPricingModelById(id);
//        return ApiResponseUtils.response(HttpStatus.OK, model, "Resources retrieved successfully");
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiDataResponse<PricingModelDTO>> updatePricingModel(@PathVariable Integer id, @RequestBody PricingModelDTO pricingModelDTO) {
//        PricingModelDTO updatedModel = pricingModelService.updatePricingModel(id, pricingModelDTO);
//        return ApiResponseUtils.response(HttpStatus.OK, updatedModel, "Resources updated successfully");
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiDataResponse<Object>> deletePricingModel(@PathVariable Integer id) {
//        pricingModelService.deletePricingModel(id);
//        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
//    }
//}
