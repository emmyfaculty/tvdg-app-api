package com.tvdgapp.controllers.shipment;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.PackageCategoryDto;
import com.tvdgapp.models.shipment.PackageCategory;
import com.tvdgapp.services.shipment.PackageCategoryService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/package-categories")
public class PackageCategoryController {

    @Autowired
    private PackageCategoryService packageCategoryService;

    @GetMapping
//    @PreAuthorize("hasAuthority({'ADMIN'})")
    public List<PackageCategory> getAllPackageCategories() {
        return packageCategoryService.getAllPackageCategories();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'managePackageCategory'})")
    public ResponseEntity<PackageCategory> createPackageCategory(@RequestBody PackageCategoryDto packageCategoryDto) {
        PackageCategory packageCategory = packageCategoryService.createPackageCategory(packageCategoryDto);
        return ResponseEntity.ok(packageCategory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority({'ADMIN', 'managePackageCategory'})")
    public ResponseEntity<PackageCategory> updatePackageCategory(@PathVariable Long id, @RequestBody PackageCategoryDto packageCategoryDto) {
        PackageCategory packageCategory = packageCategoryService.updatePackageCategory(id, packageCategoryDto);
        return ResponseEntity.ok(packageCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority({'managePackageCategory'})")
    public ResponseEntity<ApiDataResponse<String>> deletePackageCategory(@PathVariable Long id) {
        packageCategoryService.deletePackageCategory(id);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }
}
