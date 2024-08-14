package com.tvdgapp.controllers.payment;

import com.tvdgapp.dtos.payment.InitializeTransactionRequest;
import com.tvdgapp.services.payment.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/make-payment")
    public ResponseEntity<?> pay(@RequestBody InitializeTransactionRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.initTransaction(request));
    }
    // todo : fix
    @GetMapping("/verify-transaction/{reference}")
    public void verifyTransaction(@PathVariable String reference, HttpServletResponse response) throws IOException {
        paymentService.verifyTransaction(reference);
        response.sendRedirect("http://localhost:3000/tvdgApp");
    }
}
