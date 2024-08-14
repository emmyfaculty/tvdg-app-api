//package com.tvdgapp.controllers.wallet;
//
//import com.tvdgapp.apiresponse.ApiDataResponse;
//import com.tvdgapp.dtos.wallet.DepositRequestDto;
//import com.tvdgapp.dtos.wallet.PaymentRequest;
//import com.tvdgapp.models.wallet.Wallet;
//import com.tvdgapp.models.wallet1.Wallet;
//import com.tvdgapp.services.wallet.WalletService;
//import com.tvdgapp.utils.ApiResponseUtils;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//
//@RestController
//@RequestMapping("/api/v1/wallets")
//@RequiredArgsConstructor
//public class WalletController {
//
//    private final WalletService walletService;
//
//    @PostMapping("/deposit")
//    public ResponseEntity<ApiDataResponse<Wallet>> deposit(@RequestBody DepositRequestDto depositRequest) {
//        Wallet wallet = walletService.deposit(depositRequest);
//        return ApiResponseUtils.response(HttpStatus.CREATED, wallet, "The wallet has been credited successfully");
//    }
//
//    @PostMapping("/payment")
//    public ResponseEntity<ApiDataResponse<Wallet>> makePayment(@RequestBody PaymentRequest paymentRequest) {
//        Wallet wallet = walletService.makePayment(paymentRequest.getWalletId(), paymentRequest.getAmount());
//        return ApiResponseUtils.response(HttpStatus.CREATED, wallet, "Your payment transaction was successful");
//    }
//
//    @GetMapping("/{walletId}/balance")
//    public ResponseEntity<ApiDataResponse<BigDecimal>> getBalance(@PathVariable Long walletId) {
//        return ApiResponseUtils.response(HttpStatus.CREATED, "your Account balance is: " + walletService.getBalance(walletId));
//    }
//}
