//package com.tvdgapp.controllers.shipment.nationwide;
//
//import com.tvdgapp.dtos.shipment.nationwide.NationWideCityDTO;
//import com.tvdgapp.services.shipment.nationwide.NationWideCityService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/nationwide-cities")
//public class NationWideCityController {
//
//    private final NationWideCityService cityService;
//
//    @GetMapping
//    public ResponseEntity<List<NationWideCityDTO>> getAllCities() {
//        return ResponseEntity.ok(cityService.getAllCities());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<NationWideCityDTO> getCityById(@PathVariable Long id) {
//        return ResponseEntity.ok(cityService.getCityById(id));
//    }
//
//    @PostMapping
//    public ResponseEntity<NationWideCityDTO> createCity(@Validated @RequestBody NationWideCityDTO dto) {
//        return ResponseEntity.ok(cityService.createCity(dto));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<NationWideCityDTO> updateCity(@PathVariable Long id, @Validated @RequestBody NationWideCityDTO dto) {
//        return ResponseEntity.ok(cityService.updateCity(id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
//        cityService.deleteCity(id);
//        return ResponseEntity.noContent().build();
//    }
//}
