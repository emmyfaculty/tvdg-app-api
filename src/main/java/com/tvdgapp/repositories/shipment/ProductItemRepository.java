package com.tvdgapp.repositories.shipment;

import com.tvdgapp.models.shipment.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    @Modifying
    @Query("UPDATE ProductItem p SET p.packageCategory = NULL WHERE p.packageCategory.id = :categoryId")
    void updatePackageCategoryToNull(@Param("categoryId") Long categoryId);

    @Query("SELECT SUM(p.value) FROM ProductItem p WHERE p.shipment.id = :shipmentId")
    double sumTotalItemValueByShipmentId(@Param("shipmentId") Long shipmentId);

    @Query("SELECT SUM(p.weight) FROM ProductItem p WHERE p.shipment.id = :shipmentId")
    double sumTotalItemWeightByShipmentId(@Param("shipmentId") Long shipmentId);

    @Modifying
    @Query("DELETE FROM ProductItem p WHERE p.packageCategory.id = :categoryId")
    void deleteByPackageCategoryId(@Param("categoryId") Long categoryId);
}
