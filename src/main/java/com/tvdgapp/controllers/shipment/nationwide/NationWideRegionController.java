//package com.tvdgapp.controllers.shipment.nationwide;
//
//import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideRegionDto;
//import com.tvdgapp.dtos.shipment.nationwide.NationWideRegionDto;
//import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideRegionDto;
//import com.tvdgapp.services.shipment.nationwide.NationWideRegionService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/nation-wide-regions")
//public class NationWideRegionController {
//
//    private final NationWideRegionService nationWideRegionService;
//
//    @PostMapping
//    public ResponseEntity<NationWideRegionDto> createRegion(@Valid @RequestBody CreateNationWideRegionDto createDto) {
//        NationWideRegionDto regionDto = nationWideRegionService.createRegion(createDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(regionDto);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<NationWideRegionDto> getRegionById(@PathVariable Long id) {
//        NationWideRegionDto regionDto = nationWideRegionService.getRegionById(id);
//        return ResponseEntity.ok(regionDto);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<NationWideRegionDto> updateRegion(@PathVariable Long id, @Valid @RequestBody UpdateNationWideRegionDto updateDto) {
//        NationWideRegionDto regionDto = nationWideRegionService.updateRegion(id, updateDto);
//        return ResponseEntity.ok(regionDto);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
//        nationWideRegionService.deleteRegion(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<NationWideRegionDto>> getAllRegions() {
//        List<NationWideRegionDto> regions = nationWideRegionService.getAllRegions();
//        return ResponseEntity.ok(regions);
//    }
//}
