package com.tvdgapp.controllers.shipment;

import com.tvdgapp.dtos.shipment.CalculateShippingOptionDto;
import com.tvdgapp.dtos.shipment.ServicePortfolioResponseDto;
import com.tvdgapp.services.shipment.international.InternationalShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shipments")
public class InternationalShipmentController {

    private final InternationalShipmentService shipmentService;

    @PostMapping("/calculate-shipping-options")
    public ResponseEntity<List<ServicePortfolioResponseDto>> calculateShippingOptions(@RequestBody CalculateShippingOptionDto shipmentRequestDto) {
        List<ServicePortfolioResponseDto> response = shipmentService.calculateServiceOptions(shipmentRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
