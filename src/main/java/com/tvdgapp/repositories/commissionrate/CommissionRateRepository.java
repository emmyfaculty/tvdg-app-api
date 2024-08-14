package com.tvdgapp.repositories.commissionrate;

import com.tvdgapp.models.commissionrate.CommissionRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CommissionRateRepository extends JpaRepository<CommissionRate, Long> {
    @Query("SELECT c FROM CommissionRate c WHERE c.minSalesAmount <= :minSalesAmount AND c.maxSalesAmount >= :maxSalesAmount")
    Optional<CommissionRate> findByMinSalesAmountLessThanEqualAndMaxSalesAmountGreaterThanEqual(@Param("minSalesAmount") BigDecimal minSalesAmount, @Param("maxSalesAmount") BigDecimal maxSalesAmount);
}
