package com.tvdgapp.repositories.wallet;

import com.tvdgapp.models.wallet.transaction.TransactionStatus;
import com.tvdgapp.models.wallet.transaction.TransactionType;
import com.tvdgapp.models.wallet.transaction.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Integer> {
    List<WalletTransaction> findByWalletIdAndTransactionTypeAndTransactionStatus(Long walletId, TransactionType transactionType, TransactionStatus transactionStatus);
}
