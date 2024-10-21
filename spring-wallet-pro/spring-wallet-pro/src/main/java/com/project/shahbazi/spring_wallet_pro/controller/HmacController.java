package com.project.shahbazi.spring_wallet_pro.controller;

import com.project.shahbazi.spring_wallet_pro.dto.HmacResponse;
import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.service.AccountService;
import com.project.shahbazi.spring_wallet_pro.service.HmacUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/hmac")
public class HmacController {

    @Autowired
    private AccountService accountService;

    // API to generate HMAC signature
    @PostMapping("/generate")
    public ResponseEntity<HmacResponse> generateHMAC(@RequestParam String accountNumber, @RequestParam String password, @RequestParam String operation) throws Exception {
        try {
            AccountEntity account = accountService.validateCredentials(accountNumber, password);
            String hmacSignature = HmacUtilService.generateHMACWithTimestamp(accountNumber, operation, password);
            String timestamp = String.valueOf(Instant.now().getEpochSecond());
            // Return the HMAC signature and timestamp
            HmacResponse response = new HmacResponse(hmacSignature, timestamp);
            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            // Log the error if account is not found or password is incorrect
            log.error("Failed to generate HMAC: Invalid credentials for accountNumber: {}", accountNumber);
            return ResponseEntity.status(401).body(null); // Return an unauthorized response
        }
    }
}

