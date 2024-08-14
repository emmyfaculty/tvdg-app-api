package com.tvdgapp.dtos.bankdetails;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawalRequestDTO {

    @NotNull(message = "Amount is mandatory")
    @Min(value = 1, message = "Amount must be greater than 0")
    private Double amount;

    private BankDetailsDTO bankDetails;

    private boolean saveBankDetails;

}
