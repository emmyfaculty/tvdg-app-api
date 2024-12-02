package com.tvdgapp.services.shipment;

import com.tvdgapp.dtos.shipment.PackageCategoryDto;
import com.tvdgapp.exceptions.DuplicateEntityException;
import com.tvdgapp.models.shipment.PackageCategory;
import com.tvdgapp.repositories.shipment.PackageCategoryRepository;
import com.tvdgapp.repositories.shipment.ProductItemRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.tvdgapp.exceptions.EntityType.ADMIN_USER;
import static com.tvdgapp.exceptions.EntityType.CATEGORY;

@Service
public class PackageCategoryServiceImpl extends TvdgEntityServiceImpl<Long, PackageCategory> implements PackageCategoryService {

    @Autowired
    private PackageCategoryRepository packageCategoryRepository;
    @Autowired private ProductItemRepository productItemRepository;

    public List<PackageCategory> getAllPackageCategories() {
        return packageCategoryRepository.findAll();
    }


    public PackageCategory createPackageCategory(PackageCategoryDto packageCategoryDto) {
        if (packageCategoryDto.getCategoryName() == null || packageCategoryDto.getCategoryName().isEmpty()) {
            throw new IllegalArgumentException("Category name must not be null or empty");
        }
        if (packageCategoryDto.getCategoryDocumentAmount() < 0) {
            throw new IllegalArgumentException("Category document amount must not be negative");
        }

        // Check if category name already exists
        if (packageCategoryRepository.existsByCategoryName(packageCategoryDto.getCategoryName())) {
//            throw new DuplicateEntityException("Category name already exists");
            throw new DuplicateEntityException(CATEGORY.name(), "email", packageCategoryDto.getCategoryName());
        }

        PackageCategory packageCategory = new PackageCategory();
        packageCategory.setCategoryName(packageCategoryDto.getCategoryName());
        packageCategory.setCategoryAmount(BigDecimal.valueOf(packageCategoryDto.getCategoryDocumentAmount()));
        return packageCategoryRepository.save(packageCategory);
    }
    @Override
    public PackageCategory updatePackageCategory(Long id, PackageCategoryDto packageCategoryDto) {
        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findById(id);

        if (!optionalPackageCategory.isPresent()) {
            throw new IllegalArgumentException("Package category not found");
        }

        PackageCategory existingPackageCategory = optionalPackageCategory.get();

        // Check if the new category name already exists, except for the current one being updated
        if (packageCategoryRepository.existsByCategoryName(packageCategoryDto.getCategoryName()) &&
                !existingPackageCategory.getCategoryName().equals(packageCategoryDto.getCategoryName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        existingPackageCategory.setCategoryName(packageCategoryDto.getCategoryName());
        existingPackageCategory.setCategoryAmount(BigDecimal.valueOf(packageCategoryDto.getCategoryDocumentAmount()));

        return packageCategoryRepository.save(existingPackageCategory);
    }
    @Override
//    public void deletePackageCategory(Long id) {
//        Optional<PackageCategory> optionalPackageCategory = packageCategoryRepository.findById(id);
//
//        if (!optionalPackageCategory.isPresent()) {
//            throw new IllegalArgumentException("Package category not found");
//        }
//
//        packageCategoryRepository.delete(optionalPackageCategory.get());
//    }

    @Transactional
    public String deletePackageCategory(Long id) {
        if (packageCategoryRepository.existsById(id)) {
            // Update product items to remove the reference to the package category
            productItemRepository.updatePackageCategoryToNull(id);
            packageCategoryRepository.deleteById(id);
            return "Package category with ID " + id + " has been successfully deleted.";
        } else {
            return "Package category with ID " + id + " does not exist.";
        }
    }
}
