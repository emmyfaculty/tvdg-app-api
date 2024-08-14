package com.tvdgapp.repositories.User.admin;

import com.tvdgapp.models.user.admin.CustomerCseMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserCustomerUserMapRepository extends JpaRepository<CustomerCseMap, Long> {
    List<CustomerCseMap> findByCustomerId(Long customerId);
    List<CustomerCseMap> findByCseId(Long adminId);

    @Query("SELECT map.customerId FROM CustomerCseMap map WHERE map.cseId = :cseId")
    List<Long> findCustomerIdsByCseId(Long cseId);
}
