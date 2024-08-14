package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.PackageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageCategoryRepository extends JpaRepository<PackageCategory, Long> {
    boolean existsByCategoryName(String categoryName);

    Optional<PackageCategory> findByCategoryName(String packageCategoryName);

}
