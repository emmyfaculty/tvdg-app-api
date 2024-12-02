//package com.tvdgapp.controllers;
//
//import com.tvdgapp.services.DhlApiService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("${api.basepath-api}/shipment")
//public class ShipmentController {
//
//    private final DhlApiService dhlApiService;
//
//    public ShipmentController(DhlApiService dhlApiService) {
//        this.dhlApiService = dhlApiService;
//    }
//
//    @PostMapping("/send")
//    public String sendShipment(@RequestBody Map<String, Object> shipmentRequest) {
//        try {
//            return dhlApiService.sendShipmentRequest(shipmentRequest);
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//}
