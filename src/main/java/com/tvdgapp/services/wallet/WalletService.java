package com.tvdgapp.services.wallet;

import com.tvdgapp.dtos.wallet.BalanceResponseDto;
import com.tvdgapp.dtos.wallet.DepositRequestDto;
import com.tvdgapp.dtos.wallet.WithdrawRequestDto;
import com.tvdgapp.exceptions.InsufficientBalanceException;
import com.tvdgapp.exceptions.InvalidAmountException;
import com.tvdgapp.exceptions.InvalidPaymentAmountException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.wallet.*;
import com.tvdgapp.models.wallet.transaction.TransactionCategory;
import com.tvdgapp.models.wallet.transaction.TransactionStatus;
import com.tvdgapp.models.wallet.transaction.TransactionType;
import com.tvdgapp.models.wallet.transaction.WalletTransaction;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.repositories.wallet.ShipmentPaymentRepository;
import com.tvdgapp.repositories.wallet.WalletRepository;
import com.tvdgapp.repositories.wallet.WalletTransactionRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.utils.WalletKeyGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WalletService extends TvdgEntityServiceImpl<Long, Wallet> {

    private final WalletRepository walletRepository;
    private final ShipmentRepository shipmentRepository;

    private final WalletTransactionRepository transactionRepository;

    private final ShipmentPaymentRepository shipmentPaymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

        public Wallet createWalletForUser(Long userId, String currency) {
        logger.debug("Creating wallet for user ID {}", userId);

            if (walletRepository.existsByUserId(userId)) {
                throw new IllegalArgumentException("User already has a wallet");
            }

        // Generate a unique wallet number
        String walletKey = generateUniqueWalletKey();

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setCurrency(Currency.valueOf(currency));
        wallet.setWalletKey(walletKey);
        Wallet savedWallet = walletRepository.save(wallet);

        logger.info("Wallet created for user ID {} with wallet number {}", userId, walletKey);
        return savedWallet;
    }

    private String generateUniqueWalletKey() {
        String walletKey;
        do {
            walletKey = WalletKeyGenerator.generateWalletKey();
        } while (walletRepository.existsByWalletKey(walletKey));
        return walletKey;
    }

    @Transactional
    public BalanceResponseDto depositToWallet(DepositRequestDto depositRequestDto) {


        Long userId = depositRequestDto.getUserId();
        BigDecimal amount = depositRequestDto.getAmount();
        var reference = depositRequestDto.getReference();


        logger.debug("Processing deposit of {} to customer ID {}", amount, userId);


        if (depositRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user id: " + depositRequestDto.getUserId()));

        wallet.setBalance(wallet.getBalance().add(amount));

        walletRepository.save(wallet);
        logTransaction(wallet.getWalletId(), amount, TransactionType.CREDIT, TransactionStatus.COMPLETED, TransactionCategory.FUNDING, "Admin deposit", "Credit the user wallet with the sum of " + depositRequestDto.getAmount(), reference);

        return new BalanceResponseDto(wallet.getUserId(), wallet.getBalance());
    }

    @Transactional
    public BalanceResponseDto withdrawFromWallet(Long userId,WithdrawRequestDto withdrawRequestDto) {

        if (withdrawRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user id: " + userId));

        if (wallet.getBalance().compareTo(withdrawRequestDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        wallet.setBalance(wallet.getBalance().subtract(withdrawRequestDto.getAmount()));

        walletRepository.save(wallet);
        logTransaction(wallet.getWalletId(), withdrawRequestDto.getAmount(), TransactionType.DEBIT, TransactionStatus.COMPLETED, TransactionCategory.MISCELLANEOUS, String.valueOf(wallet.getUserId()), "User Withdrawal", null);

        return new BalanceResponseDto(wallet.getUserId(), wallet.getBalance());
    }

    public BalanceResponseDto checkWalletBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user id: " + userId));
        return new BalanceResponseDto(wallet.getUserId(), wallet.getBalance());
    }

    @Transactional
    public boolean makeShipmentPayment(Long userId, Long shipmentId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user id: " + userId));

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found for shipment id: " + shipmentId));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for shipment payment");
        }

        if (amount.compareTo(shipment.getTotalShipmentAmount()) != 0) {
            throw new InvalidPaymentAmountException("Payment amount does not match the shipment total amount");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        ShipmentPayment shipmentPayment = new ShipmentPayment();
        shipmentPayment.setShipmentRef(shipment.getShipmentRef());
        shipmentPayment.setPaymentRef(generatePaymentReference());
        shipmentPayment.setPaymentMethod(PaymentMethod.WALLET);
        shipmentPayment.setPaymentStatus(PaymentStatus.COMPLETED);
        shipmentPayment.setPaymentAmount(amount);
        shipmentPayment.setPaymentDate(LocalDateTime.now().toLocalDate());
        shipmentPayment.setPaymentProcessor(PaymentProcessor.DLE);

        shipmentPaymentRepository.save(shipmentPayment);

        // Log the transaction in the wallet
        logTransaction(wallet.getWalletId(), amount, TransactionType.DEBIT, TransactionStatus.COMPLETED,
                TransactionCategory.SHIPPING, String.valueOf(shipment.getId()), "Payment for shipment", null);

        return true;
    }

    private void logTransaction(Long walletId, BigDecimal amount, TransactionType type, TransactionStatus status,
                                TransactionCategory category, String target, String description, String reference) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setWalletId(walletId);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setTransactionStatus(status);
        transaction.setTransactionCategory(category);
        transaction.setTransactionTarget(target);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(description);
        transaction.setReference(reference);

        transactionRepository.save(transaction);
        logger.info("Transaction logged: " + transaction);
    }

    public BigDecimal getTotalWithdrawnAmount(Long affiliateUserId) {
        List<WalletTransaction> transactions = transactionRepository.findByWalletIdAndTransactionTypeAndTransactionStatus(
                walletRepository.findByUserId(affiliateUserId).get().getWalletId(),
                TransactionType.DEBIT,
                TransactionStatus.COMPLETED //.SUCCESS
        );

        return transactions.stream()
                .map(WalletTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getAffiliateUserWalletBalance(Long affiliateUserId) {
        Wallet wallet = walletRepository.findByUserId(affiliateUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }

    public BigDecimal getTotalWithdrawableAmount(Long affiliateUserId) {
        Wallet wallet = walletRepository.findByUserId(affiliateUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return wallet.getBalance(); // Assuming the entire balance is withdrawable
    }
    private String generatePaymentReference() {
        return UUID.randomUUID().toString();
    }

}
