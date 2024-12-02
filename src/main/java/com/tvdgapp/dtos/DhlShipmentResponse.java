package com.tvdgapp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DhlShipmentResponse {
    private String shipmentTrackingNumber;
    private String trackingUrl;
    private List<DhlPackage> packages;
    private List<DhlDocument> documents;

    @Data
    public static class DhlPackage {
        private int referenceNumber;
        private String trackingNumber;
        private String trackingUrl;
    }

    @Data
    public static class DhlDocument {
        private String imageFormat;
        private String content;
        private String typeCode;
    }
}