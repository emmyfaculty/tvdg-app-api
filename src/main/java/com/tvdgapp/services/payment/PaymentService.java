package com.tvdgapp.services.payment;


import com.tvdgapp.dtos.payment.InitializeTransactionRequest;
import com.tvdgapp.dtos.payment.InitializeTransactionResponse;
import com.tvdgapp.dtos.payment.VerifyTransactionResponse;

public interface PaymentService {

    InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception;

    VerifyTransactionResponse verifyTransaction(String reference);
}
