package com.tvdgapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvdgapp.dtos.DhlShipmentResponse;
import com.tvdgapp.dtos.dhl.ShippingRequestDto;
import com.tvdgapp.dtos.dhltrackingresponse.DhlShipmentTrackingResponse;
import com.tvdgapp.exceptions.TvdgAppSystemException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class DhlApiService {

    private static final Logger logger = LoggerFactory.getLogger(DhlApiService.class);

    @Value("${dhl.api.url}")
    private String dhlApiUrl;

    @Value("${dhl.api.username}")
    private String username;

    @Value("${dhl.api.password}")
    private String password;

    private final ObjectMapper objectMapper;

    public DhlApiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DhlShipmentResponse sendShipmentRequest(ShippingRequestDto requestPayload) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(requestPayload);

        // Log the request payload before sending it
        logger.info("Sending DHL shipment request: {}", jsonPayload);

        String authHeader = createBasicAuthHeader(username, password);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(dhlApiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", authHeader);

            StringEntity entity = new StringEntity(jsonPayload);
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            // Log the response status and body
            logger.info("Received response with status code: {}", statusCode);
            logger.info("Response body: {}", responseBody);

            if (statusCode == 200 || statusCode == 201) {
                return objectMapper.readValue(responseBody, DhlShipmentResponse.class);
            } else {
                throw new TvdgAppSystemException("Failed to send request: " + statusCode + " " + responseBody);
            }
        }
    }

    private String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
    }




}
