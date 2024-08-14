package com.tvdgapp.repositories.wallet;

import com.tvdgapp.models.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
    boolean existsByWalletKey(String walletNumber);
//    Wallet findByUserId(Long customerId);
}

