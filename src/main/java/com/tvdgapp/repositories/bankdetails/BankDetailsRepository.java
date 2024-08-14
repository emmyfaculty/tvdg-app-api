package com.tvdgapp.repositories.bankdetails;


import com.tvdgapp.models.bankdetails.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
    Optional<BankDetails> findByAccountNumber(String accountNumber);

    Optional<BankDetails> findByUserIdAndAccountNumber(Long userId, String accountNumber);

    Optional<BankDetails> findByUserId(Long userId);
}
