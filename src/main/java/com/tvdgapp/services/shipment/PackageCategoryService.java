package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.PackageCategoryDto;
import com.tvdgapp.models.shipment.PackageCategory;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.List;

public interface PackageCategoryService extends TvdgEntityService<Long, PackageCategory> {

    List<PackageCategory> getAllPackageCategories();
    PackageCategory updatePackageCategory(Long id, PackageCategoryDto packageCategoryDto);
    PackageCategory createPackageCategory(PackageCategoryDto packageCategoryDto);
    public String deletePackageCategory(Long id);
}
