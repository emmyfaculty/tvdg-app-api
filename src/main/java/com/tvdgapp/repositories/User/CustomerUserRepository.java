package com.tvdgapp.repositories.User;

import com.tvdgapp.dtos.user.CustomerUserDtoResponse;
import com.tvdgapp.models.user.customer.CustomerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerUserRepository extends JpaRepository<CustomerUser, Long> {

    boolean existsByEmail(String email);
    Optional<CustomerUser> findByEmail(String email);

    Optional<CustomerUser> findCustomerByPasswordResetToken(String token);

    Optional<CustomerUser> findCustomerUserById(Long customerUserId);
    List<CustomerUser> findAll();
    Optional<CustomerUser> findById(Long id);

//    Optional<CustomerUser> findByUserId(Long id);
}
