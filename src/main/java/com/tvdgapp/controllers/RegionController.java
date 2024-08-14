package com.tvdgapp.controllers;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.RegionDTO;
import com.tvdgapp.services.reference.RegionService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<RegionDTO>>> getAllRegions() {
        List<RegionDTO> list = regionService.getAllRegions();
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDataResponse<RegionDTO>> getRegionById(@PathVariable Integer id) {
        RegionDTO region = regionService.getRegionById(id);
        return ApiResponseUtils.response(HttpStatus.OK, region, "Resources retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiDataResponse<RegionDTO>> createRegion(@RequestBody RegionDTO regionDTO) {
        RegionDTO createdRegion = regionService.createRegion(regionDTO);
        return ApiResponseUtils.response(HttpStatus.OK, createdRegion, "Resources retrieved successfully");

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<RegionDTO>> updateRegion(@PathVariable Integer id, @RequestBody RegionDTO regionDTO) {
        RegionDTO updatedRegion = regionService.updateRegion(id, regionDTO);
        return ApiResponseUtils.response(HttpStatus.OK, updatedRegion, "Resources retrieved successfully");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteRegion(@PathVariable Integer id) {
        regionService.deleteRegion(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");

    }
}
