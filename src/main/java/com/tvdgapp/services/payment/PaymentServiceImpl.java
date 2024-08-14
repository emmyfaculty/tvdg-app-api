package com.tvdgapp.services.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tvdgapp.dtos.payment.InitializeTransactionRequest;
import com.tvdgapp.dtos.payment.InitializeTransactionResponse;
import com.tvdgapp.dtos.payment.VerifyTransactionResponse;
import com.tvdgapp.exceptions.AuthenticationException;
import com.tvdgapp.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final Map<String, String> trackReference;

    public InitializeTransactionResponse initTransaction(InitializeTransactionRequest request) throws Exception {
        InitializeTransactionResponse initializeTransactionResponse = null;
        try {
            Gson gson = new Gson();
            request.setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)));
            StringEntity postingString = new StringEntity(gson.toJson(request));
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost("https://api.paystack.co/transaction/initialize");
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/json");
            post.addHeader("Authorization", "Bearer sk_test_0153f4208929412b896c9cd52160e64d3552dd07");

            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String result = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                initializeTransactionResponse = mapper.readValue(result, InitializeTransactionResponse.class);
            } else if (statusCode == 401) {
                throw new AuthenticationException("Unauthorized access to Paystack API");
            } else {
                throw new Exception("Error occurred while initializing transaction: " + statusCode);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Failure initializing Paystack transaction: " + ex.getMessage());
        }
        return initializeTransactionResponse;
    }

// todo : fix
    public VerifyTransactionResponse verifyTransaction(String reference) {
        VerifyTransactionResponse payStackResponse = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("https://api.paystack.co/transaction/verify/" + reference);
            request.addHeader("Content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + "sk_test_0153f4208929412b896c9cd52160e64d3552dd07");

            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.OK.value()) {
                String result = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                payStackResponse = mapper.readValue(result, VerifyTransactionResponse.class);

                if (payStackResponse == null || !payStackResponse.getStatus().equals("true")) {
                    throw new Exception("An error occurred while verifying payment");
                } else if (payStackResponse.getData().getStatus().equals("success")) {
                    BigDecimal amount = payStackResponse.getData().getAmount();
                    String email = trackReference.get(reference);
                    trackReference.remove(reference);
                }
            } else {
                throw new Exception("Error occurred while connecting to PayStack URL: " + statusCode);
                }
        } catch (Exception ex) {
            ex.printStackTrace();
            // Handle exception, possibly log and return a default response
        }
        return payStackResponse;
    }
}
