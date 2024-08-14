package com.tvdgapp.controllers.wallet;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.wallet.BalanceResponseDto;
import com.tvdgapp.dtos.wallet.DepositRequestDto;
import com.tvdgapp.dtos.wallet.WithdrawRequestDto;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.wallet.WalletService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final UserInfoUtil userInfoUtil;

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('fundWallet')")
    public ResponseEntity<ApiDataResponse<BalanceResponseDto>> depositToWallet(@Valid @RequestBody DepositRequestDto depositRequestDto) {
        BalanceResponseDto balanceResponse = walletService.depositToWallet(depositRequestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, balanceResponse, "The wallet has been credited successfully");
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<BalanceResponseDto>> withdrawFromWallet(@Valid @RequestBody WithdrawRequestDto withdrawRequestDto) {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        BalanceResponseDto balanceResponse = walletService.withdrawFromWallet(userInfo.getUserId(), withdrawRequestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, balanceResponse, "Your withdrawal transaction was successful");
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<BalanceResponseDto>> checkWalletBalance() {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        BalanceResponseDto balanceResponse = walletService.checkWalletBalance(userInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "your Account balance is: " + balanceResponse.getBalance());
    }

    @GetMapping("/{affiliateUserId}/balance")
    @PreAuthorize("hasAuthority('fundWallet')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getAffiliateUserWalletBalance(@PathVariable Long affiliateUserId) {
        BigDecimal balance = walletService.getAffiliateUserWalletBalance(affiliateUserId);
        return ApiResponseUtils.response(HttpStatus.OK, "Your wallet balance is: " + balance);
    }
    @GetMapping("/balance-check")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getAffiliateUserWalletBalance() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        BigDecimal balance = walletService.getAffiliateUserWalletBalance(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "Your wallet balance is: " + balance);
    }

    @GetMapping("/{affiliateUserId}/total-withdrawable")
    @PreAuthorize("hasAuthority('fundWallet')")
    public ResponseEntity<ApiDataResponse<Object>> getTotalWithdrawableAmount(@PathVariable Long affiliateUserId) {
        BigDecimal totalWithdrawable = walletService.getTotalWithdrawableAmount(affiliateUserId);
        return ApiResponseUtils.response(HttpStatus.OK, "Your earnings withdrawable balance is: " + totalWithdrawable);
    }
    @GetMapping("/total-withdrawable")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<Object>> getTotalWithdrawableAmount() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        BigDecimal totalWithdrawable = walletService.getTotalWithdrawableAmount(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "Your earnings withdrawable balance is: " + totalWithdrawable);
    }

    @GetMapping("/{affiliateUserId}/total-withdrawn")
    @PreAuthorize("hasAuthority('fundWallet')")
    public ResponseEntity<ApiDataResponse<Object>> getTotalWithdrawnAmount(@PathVariable Long affiliateUserId) {
        BigDecimal totalWithdrawn = walletService.getTotalWithdrawnAmount(affiliateUserId);
        return ApiResponseUtils.response(HttpStatus.OK, "Your earnings withdrawn amount is: " + totalWithdrawn);
    }
    @GetMapping("/total-withdrawn")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<Object>> getTotalWithdrawnAmount() {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        BigDecimal totalWithdrawn = walletService.getTotalWithdrawnAmount(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "Your earnings withdrawn amount is: " + totalWithdrawn);
    }

}
