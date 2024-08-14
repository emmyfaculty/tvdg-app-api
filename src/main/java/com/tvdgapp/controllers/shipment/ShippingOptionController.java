//package com.tvdgapp.controllers.shipment;
//
//import com.tvdgapp.dtos.shipment.ShippingOptionDto;
//import com.tvdgapp.models.shipment.ShippingOption;
//import com.tvdgapp.services.shipment.ShippingOptionService;
//import io.swagger.annotations.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/shipping-option")
//@Api(tags = { "Shipping Options Controller" })
//public class ShippingOptionController {
//
//    @Autowired
//    private ShippingOptionService shippingOptionService;
//
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Resource created successfully"),
//            @ApiResponse(code = 400, message = "Invalid request!!!"),
//            @ApiResponse(code = 409, message = "non unique entity!!!"),
//            @ApiResponse(code = 401, message = "not authorized!")})
//    @ApiOperation(value = "Create user", notes = "authorities[manageAdminUser]", authorizations = {@Authorization(value = "JWT") })
//    @PostMapping("/create")
//    public ResponseEntity<ShippingOption> createServiceOption(@RequestBody ShippingOptionDto request) {
//        ShippingOption shippingOption = shippingOptionService.createShippingOption(request);
//        return ResponseEntity.ok(shippingOption);
//    }
//
//    @PutMapping("/edit/{id}")
//    public ResponseEntity<ShippingOption> editServiceOption(@PathVariable Long id, @RequestBody ShippingOptionDto request) {
//        ShippingOption shippingOption = shippingOptionService.editShippingOption(id, request);
//        return ResponseEntity.ok(shippingOption);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ShippingOption>> listServiceOptions() {
//        List<ShippingOption> shippingOptions = shippingOptionService.listShippingOptions();
//        return ResponseEntity.ok(shippingOptions);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteServiceOption(@PathVariable Long id) {
//        shippingOptionService.deleteShippingOption(id);
//        return ResponseEntity.noContent().build();
//    }
//}
