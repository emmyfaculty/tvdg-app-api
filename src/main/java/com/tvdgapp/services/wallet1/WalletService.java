//package com.tvdgapp.services.wallet;
//
//import com.tvdgapp.dtos.wallet.DepositRequestDto;
//import com.tvdgapp.exceptions.InsufficientFundsException;
//import com.tvdgapp.exceptions.WalletNotFoundException;
//import com.tvdgapp.models.transactions.Transaction;
//import com.tvdgapp.models.user.User;
//import com.tvdgapp.models.wallet.Wallet;
//import com.tvdgapp.models.wallet1.Wallet;
//import com.tvdgapp.repositories.User.UserRepository;
//import com.tvdgapp.repositories.transaction.TransactionRepository;
//import com.tvdgapp.repositories.wallet.WalletRepository;
//import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
//import com.tvdgapp.utils.CodeGeneratorUtils;
//import com.tvdgapp.utils.WalletNumberGenerator;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class WalletService extends TvdgEntityServiceImpl<Integer, Wallet> {
//
//    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);
//
//
//    private final WalletRepository walletRepository;
//
//    private final TransactionRepository transactionRepository;
//
//    private final UserRepository userRepository;
//
//
////    public Wallet createWalletForUser(Long userId) {
////        logger.debug("Creating wallet for user ID {}", userId);
////        Wallet wallet = new Wallet();
////        wallet.setUserId(userId);
////        Wallet savedWallet = walletRepository.save(wallet);
////        logger.info("Wallet created for user ID {}", userId);
////        return savedWallet;
////    }
//
//
//    public Wallet createWalletForUser(Long userId) {
//        logger.debug("Creating wallet for user ID {}", userId);
//
//        // Generate a unique wallet number
//        String walletNumber = generateUniqueWalletNumber();
//
//        Wallet wallet = new Wallet();
//        wallet.setUserId(userId);
//        wallet.setWalletNumber(walletNumber);
//        Wallet savedWallet = walletRepository.save(wallet);
//
//        logger.info("Wallet created for user ID {} with wallet number {}", userId, walletNumber);
//        return savedWallet;
//    }
//
//    private String generateUniqueWalletNumber() {
//        String walletNumber;
//        do {
//            walletNumber = WalletNumberGenerator.generateWalletNumber();
//        } while (walletRepository.existsByWalletNumber(walletNumber));
//        return walletNumber;
//    }
//
//    @Transactional
//    public Wallet deposit(DepositRequestDto depositRequestDto) {
//
//        Long customerId = depositRequestDto.getCustomerId();
//        BigDecimal amount = depositRequestDto.getAmount();
//
//
//        logger.debug("Processing deposit of {} to customer ID {}", amount, customerId);
//
//        Wallet wallet = walletRepository.findByUserId(customerId);
////                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user id: " + customerId));
//
//
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Deposit amount must be positive");
//        }
//
//
//        wallet.setBalance(wallet.getBalance().add(amount));
//        walletRepository.save(wallet);
//
//        Transaction transaction = new Transaction();
//        transaction.setWalletId(wallet.getWalletId());
//        transaction.setTransactionType("deposit");
//        transaction.setTransactionMode("Wallet");
//        transaction.setAmount(amount);
//        transaction.setReference(CodeGeneratorUtils.generateReference());
//        transactionRepository.save(transaction);
//
//        User user = userRepository.findById(wallet.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Map<String, Object> variables = new HashMap<>();
//        // variables.put("username", user.getUsername());
//        // variables.put("amount", amount);
//        // emailService.sendEmail(user.getEmail(), "Deposit Successful", "deposit-email.html", variables);
//
//        logger.info("Deposit of {} to wallet ID {} successful", amount, customerId);
//        return wallet;
//    }
//
//    @Transactional
//    public Wallet makePayment(Long walletId, BigDecimal amount) {
//        logger.debug("Processing payment of {} from wallet ID {}", amount, walletId);
//
//        Wallet wallet = walletRepository.findById(walletId)
//                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
//
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Payment amount must be positive");
//        }
//
//        if (wallet.getBalance().compareTo(amount) < 0) {
//            logger.warn("Insufficient funds for payment of {} from wallet ID {}", amount, walletId);
//            throw new InsufficientFundsException("Insufficient funds in wallet");
//        }
//
//        wallet.setBalance(wallet.getBalance().subtract(amount));
//        walletRepository.save(wallet);
//
//        Transaction transaction = new Transaction();
//        transaction.setWalletId(walletId);
//        transaction.setTransactionType("payment");
//        transaction.setAmount(amount);
//        transactionRepository.save(transaction);
//
//        User user = userRepository.findById(wallet.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Map<String, Object> variables = new HashMap<>();
//        // variables.put("username", user.getUsername());
//        // variables.put("amount", amount);
//        // emailService.sendEmail(user.getEmail(), "Payment Successful", "payment-email.html", variables);
//
//        logger.info("Payment of {} from wallet ID {} successful", amount, walletId);
//        return wallet;
//    }
//    @Transactional
//    public void deductAmount(Long customerId, BigDecimal amount, Long shipmentId) {
////        Wallet wallet = walletRepository.findByUserId(customerId);
////        if (wallet.getBalance().compareTo(amount) >= 0) {
////            wallet.setBalance(wallet.getBalance().subtract(amount));
////            walletRepository.save(wallet);
//
////            // Save the transaction
////            Transaction transaction = new Transaction();
////            transaction.setWalletId(wallet.getId());
////            transaction.setAmount(amount.negate());
////            transaction.setShipmentId(shipmentId);
////            transaction.setTransactionType("PAYMENT");
////            transactionRepository.save(transaction);
//
//            logger.debug("Processing payment of {} from wallet ID {}", amount, shipmentId);
//
//            Wallet wallet = walletRepository.findById(customerId)
//                    .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
//
//            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//                throw new IllegalArgumentException("Payment amount must be positive");
//            }
//
//            if (wallet.getBalance().compareTo(amount) < 0) {
//                logger.warn("Insufficient funds for payment of {} from wallet ID {}", amount, shipmentId);
//                throw new InsufficientFundsException("Insufficient funds in wallet");
//            }
//
//            wallet.setBalance(wallet.getBalance().subtract(amount));
//            walletRepository.save(wallet);
//
//            // Save the transaction
//            Transaction transaction = new Transaction();
//            transaction.setWalletId(wallet.getId());
//            transaction.setAmount(amount.negate());
//            transaction.setShipmentId(shipmentId);
//            transaction.setTransactionType("PAYMENT");
//            transactionRepository.save(transaction);
//
//            // Send email notification
////            emailService.sendPaymentNotification(customerId, amount, wallet.getBalance());
////        } else {
////            throw new InsufficientFundsException("Insufficient balance in wallet.");
////        }
//    }
//
//    public BigDecimal getBalance(Long walletId) {
//        logger.debug("Retrieving balance for wallet ID {}", walletId);
//        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
//        User user = userRepository.findById(wallet.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
////        Map<String, Object> variables = new HashMap<>();
////        variables.put("username", user.getUsername());
////        variables.put("balance", wallet.getBalance());
////        emailService.sendEmail(user.getEmail(), "Balance Enquiry", "balance-email.html", variables);
//        logger.info("Balance enquiry for wallet ID {} successful", walletId);
//        return wallet.getBalance();
//    }
//}
