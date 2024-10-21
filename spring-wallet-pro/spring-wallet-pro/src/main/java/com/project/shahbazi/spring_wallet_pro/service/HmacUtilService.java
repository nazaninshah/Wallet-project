package com.project.shahbazi.spring_wallet_pro.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;

public class HmacUtilService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    // Max time allowed in seconds (e.g., 5 minutes = 300 seconds)
    private static final long MAX_TIME_DIFF = 300;

    public static String generateHMACWithTimestamp(String accountNumber, String operation, String password) throws Exception {
        String timestamp = String.valueOf(Instant.now().getEpochSecond()); // Current Unix timestamp
        String data = accountNumber + ":" + operation + ":" + timestamp;
        return generateHMAC(data, password);
    }

    public static String generateHMAC(String data, String password) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(password.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(rawHmac);
    }

    public static boolean verifyHMACWithTimestamp(String accountNumber, String operation, String receivedHmac, String password, String timestamp) throws Exception {
        String data = accountNumber + ":" + operation + ":" + timestamp;
        String generatedHmac = generateHMAC(data, password);
        if (!generatedHmac.equals(receivedHmac)) {
            return false; // HMAC does not match
        }
        long requestTime = Long.parseLong(timestamp);
        long currentTime = Instant.now().getEpochSecond();
        return Math.abs(currentTime - requestTime) <= MAX_TIME_DIFF;
    }
}