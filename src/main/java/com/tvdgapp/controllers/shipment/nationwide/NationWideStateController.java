//package com.tvdgapp.controllers.shipment.nationwide;
//
//import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideStateDto;
//import com.tvdgapp.dtos.shipment.nationwide.NationWideStateDto;
//import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideStateDto;
//import com.tvdgapp.services.shipment.nationwide.NationWideStateService;
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
//@RequestMapping("/api/v1/nation-wide-states")
//public class NationWideStateController {
//
//    private final NationWideStateService nationWideStateService;
//
//    @PostMapping
//    public ResponseEntity<NationWideStateDto> createState(@Valid @RequestBody CreateNationWideStateDto createDto) {
//        NationWideStateDto stateDto = nationWideStateService.createState(createDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(stateDto);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<NationWideStateDto> getStateById(@PathVariable Long id) {
//        NationWideStateDto stateDto = nationWideStateService.getStateById(id);
//        return ResponseEntity.ok(stateDto);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<NationWideStateDto> updateState(@PathVariable Long id, @Valid @RequestBody UpdateNationWideStateDto updateDto) {
//        NationWideStateDto stateDto = nationWideStateService.updateState(id, updateDto);
//        return ResponseEntity.ok(stateDto);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
//        nationWideStateService.deleteState(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<NationWideStateDto>> getAllStates() {
//        List<NationWideStateDto> states = nationWideStateService.getAllStates();
//        return ResponseEntity.ok(states);
//    }
//}
