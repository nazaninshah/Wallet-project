package com.project.shahbazi.spring_wallet_pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shahbazi.spring_wallet_pro.dto.HmacResponse;
import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HmacControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testGenerateHMAC() throws Exception {
        // Test data
        String accountNumber = "1234567890123456789012";
        String password = "pass123";
        String operation = "withdraw";

        // Mock AccountService to return a valid account
        AccountEntity account = new AccountEntity();
        account.setAccountNumber(accountNumber);
        account.setPassword(password);
        Mockito.when(accountService.validateCredentials(anyString(), anyString())).thenReturn(account);

        // Send POST request to generate HMAC
        mockMvc.perform(post("/api/hmac/generate")
                        .param("accountNumber", accountNumber)
                        .param("password", password)
                        .param("operation", operation)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hmacSignature").isNotEmpty())
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    public void testGenerateHMACUnauthorized() throws Exception {
        // Test data
        String accountNumber = "1234567890123456789012";
        String password = "wrongpass";
        String operation = "withdraw";

        // Mock AccountService to throw SecurityException for invalid credentials
        Mockito.when(accountService.validateCredentials(anyString(), anyString()))
                .thenThrow(new SecurityException("Invalid credentials."));

        // Send POST request with invalid credentials
        mockMvc.perform(post("/api/hmac/generate")
                        .param("accountNumber", accountNumber)
                        .param("password", password)
                        .param("operation", operation)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}

