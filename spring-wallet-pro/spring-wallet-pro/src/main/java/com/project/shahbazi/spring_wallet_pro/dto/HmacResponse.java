package com.project.shahbazi.spring_wallet_pro.dto;

public class HmacResponse {

    private String hmacSignature;
    private String timestamp;

    // Constructor
    public HmacResponse(String hmacSignature, String timestamp) {
        this.hmacSignature = hmacSignature;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getHmacSignature() {
        return hmacSignature;
    }

    public void setHmacSignature(String hmacSignature) {
        this.hmacSignature = hmacSignature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
