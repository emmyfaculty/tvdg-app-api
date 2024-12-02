package com.tvdgapp.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvdgapp.dtos.dhltrackingresponse.DhlShipmentTrackingResponse;
import com.tvdgapp.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DhlTrackingService {

    private final HttpClient httpClient;
//    private final String encodedCredentials;

    @Value("${dhl.api.username}")
    private String username;

    @Value("${dhl.api.password}")
    private String password;

    public DhlTrackingService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public DhlShipmentTrackingResponse trackShipment(String trackingNumber, String trackingView, String levelOfDetail) throws IOException, InterruptedException {
        String url = String.format("https://express.api.dhl.com/mydhlapi/test/shipments/%s/tracking?trackingView=%s&levelOfDetail=%s", trackingNumber, trackingView, levelOfDetail);
        String authHeader = createBasicAuthHeader(username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", authHeader)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return new ObjectMapper().readValue(response.body(), DhlShipmentTrackingResponse.class);
        } else {
            throw new RuntimeException("Failed to track shipment: " + response.body());
        }
    }

    public CompletableFuture<DhlShipmentTrackingResponse> trackShipmentAsync(String trackingNumber, String trackingView, String levelOfDetail) {
        String url = String.format("https://express.api.dhl.com/mydhlapi/test/shipments/%s/tracking?trackingView=%s&levelOfDetail=%s", trackingNumber, trackingView, levelOfDetail);

        String authHeader = createBasicAuthHeader(username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", authHeader)
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            return new ObjectMapper().readValue(response.body(), DhlShipmentTrackingResponse.class);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to parse response", e);
                        }
                    } else {
                        throw new RuntimeException("Failed to track shipment: " + response.body());
                    }
                });
    }

    private String createBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth, StandardCharsets.UTF_8);
    }
}
