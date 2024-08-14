package com.tvdgapp.controllers.bankdetails;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.bankdetails.BankDetailsDTO;
import com.tvdgapp.dtos.bankdetails.WithdrawalRequestDTO;
import com.tvdgapp.services.bankdetails.BankingService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banking")
@Validated
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService;
    private final UserInfoUtil userInfoUtil;

    @PostMapping("/bank-details/{userId}")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<BankDetailsDTO>> addBankDetails(@PathVariable Long userId, @Valid @RequestBody BankDetailsDTO bankDetailsDTO) {
        BankDetailsDTO createdBankDetails = bankingService.addBankDetails(userId, bankDetailsDTO);
        return ApiResponseUtils.response(HttpStatus.CREATED, createdBankDetails, "Bank details added successfully");
    }

    @PostMapping("/withdraw/{userId}")
    @PreAuthorize("hasAuthority('manageWallet')")
    public ResponseEntity<ApiDataResponse<Object>> makeWithdrawal(@PathVariable Long userId, @Valid @RequestBody WithdrawalRequestDTO requestDTO) {
        bankingService.makeWithdrawal(userId, requestDTO);
        return ApiResponseUtils.response(HttpStatus.OK, "Withdrawal processed successfully");

    }
}
