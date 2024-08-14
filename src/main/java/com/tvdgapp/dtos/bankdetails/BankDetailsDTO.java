package com.tvdgapp.dtos.bankdetails;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BankDetailsDTO {

    @NotBlank(message = "Account number is mandatory")
    @Size(min = 10, max = 10, message = "Account number must be be 10 characters")
    private String accountNumber;

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

    @NotBlank(message = "Account name is mandatory")
    private String accountName;

}
