package com.tvdgapp.services.bankdetails;

import com.tvdgapp.dtos.bankdetails.BankDetailsDTO;
import com.tvdgapp.dtos.bankdetails.WithdrawalRequestDTO;
import com.tvdgapp.exceptions.BankDetailsNotFoundException;
import com.tvdgapp.exceptions.InsufficientBalanceException;
import com.tvdgapp.exceptions.InvalidWithdrawalException;
import com.tvdgapp.models.bankdetails.BankDetails;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.models.wallet.Wallet;
import com.tvdgapp.models.wallet.transaction.TransactionCategory;
import com.tvdgapp.models.wallet.transaction.TransactionStatus;
import com.tvdgapp.models.wallet.transaction.TransactionType;
import com.tvdgapp.models.wallet.transaction.WalletTransaction;
import com.tvdgapp.repositories.affiliate.AffiliateRepository;
import com.tvdgapp.repositories.bankdetails.BankDetailsRepository;
import com.tvdgapp.repositories.wallet.WalletRepository;
import com.tvdgapp.repositories.wallet.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankingService {

    private static final Logger logger = LoggerFactory.getLogger(BankingService.class);

    private final BankDetailsRepository bankDetailsRepository;

    private final WalletRepository walletRepository;

    private final WalletTransactionRepository walletTransactionRepository;
    private final AffiliateRepository userRepository;

    @Transactional
    public BankDetailsDTO addBankDetails(Long userId, BankDetailsDTO bankDetailsDTO) {
        AffiliateUser user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidWithdrawalException("User not found"));

        Optional<BankDetails> existingBankDetails = bankDetailsRepository.findByUserIdAndAccountNumber(userId, bankDetailsDTO.getAccountNumber());
        if (existingBankDetails.isPresent()) {
            throw new InvalidWithdrawalException("Bank details already exist for this account number");
        }

        BankDetails bankDetails = new BankDetails();
        bankDetails.setAccountNumber(bankDetailsDTO.getAccountNumber());
        bankDetails.setBankName(bankDetailsDTO.getBankName());
        bankDetails.setAccountName(bankDetailsDTO.getAccountName());
        bankDetails.setUser(user);

        bankDetailsRepository.save(bankDetails);
        logger.info("Bank details added for user: {}, account number: {}", userId, bankDetailsDTO.getAccountNumber());
        return bankDetailsDTO;
    }

    @Transactional
    public void makeWithdrawal(Long userId, WithdrawalRequestDTO requestDTO) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidWithdrawalException("User wallet not found"));

        BigDecimal amount = BigDecimal.valueOf(requestDTO.getAmount());

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in wallet");
        }

        BankDetails bankDetails = getOrCreateBankDetails(userId, requestDTO);

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWalletId(wallet.getWalletId());
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionCategory(TransactionCategory.MISCELLANEOUS);
        transaction.setTransactionTarget(bankDetails.getAccountNumber());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription("Withdrawal to bank account");
        walletTransactionRepository.save(transaction);

        logger.info("Withdrawal of {} processed to account number: {}", amount, bankDetails.getAccountNumber());
    }

    private BankDetails getOrCreateBankDetails(Long userId, WithdrawalRequestDTO requestDTO) {
        Optional<BankDetails> optionalBankDetails = bankDetailsRepository.findByUserId(userId);

        if (optionalBankDetails.isPresent()) {
            BankDetails bankDetails = optionalBankDetails.get();
            logger.info("Using existing bank details for user: {}, account number: {}", userId, bankDetails.getAccountNumber());
            return bankDetails;
        } else {
            if (requestDTO.getBankDetails() == null) {
                throw new BankDetailsNotFoundException("Bank details not provided");
            }

            BankDetailsDTO bankDetailsDTO = requestDTO.getBankDetails();
            BankDetails bankDetails = new BankDetails();
            bankDetails.setAccountNumber(bankDetailsDTO.getAccountNumber());
            bankDetails.setBankName(bankDetailsDTO.getBankName());
            bankDetails.setAccountName(bankDetailsDTO.getAccountName());

            AffiliateUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new InvalidWithdrawalException("User not found"));
            bankDetails.setUser(user);

            if (requestDTO.isSaveBankDetails()) {
                bankDetailsRepository.save(bankDetails);
                logger.info("Bank details saved for future use for user: {}, account number: {}", userId, bankDetails.getAccountNumber());
            }
            return bankDetails;
        }
    }
}
