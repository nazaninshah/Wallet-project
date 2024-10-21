package com.project.shahbazi.spring_wallet_pro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shahbazi.spring_wallet_pro.dto.AccountRequest;
import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_wallet_pro.entity.InvoiceEntity;
import com.project.shahbazi.spring_wallet_pro.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testGetAllAccounts() throws Exception {
        // Test data
        List<AccountEntity> accounts = new ArrayList<>();
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("1234567890123456789012");
        account.setBalance(10000.0);
        account.setShebaNumber("IR123456789012345678901234");
        accounts.add(account);

        // Mock behavior of accountService
        Mockito.when(accountService.getAllAccounts()).thenReturn(accounts);

        // Send GET request
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890123456789012"))
                .andExpect(jsonPath("$[0].balance").value(10000.0));
    }

    @Test
    public void testGetAccountByAccountNumber() throws Exception {
        // Test data
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("1234567890123456789012");
        account.setBalance(10000.0);
        account.setShebaNumber("IR123456789012345678901234");

        // Mock behavior of accountService
        Mockito.when(accountService.getAccountByAccountNumber(anyString())).thenReturn(account);

        // Send GET request
        mockMvc.perform(get("/api/accounts/1234567890123456789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("1234567890123456789012"))
                .andExpect(jsonPath("$.balance").value(10000.0));
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Test data
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("1234567890123456789012");
        account.setBalance(10000.0);
        account.setShebaNumber("IR123456789012345678901234");
        account.setPassword("pass123");
        account.setOpenDate(new Date());

        // Create a mock CustomerEntity
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);  // Make sure the customer exists with ID 1
        account.setCustomer(customer);

        // Mock behavior of accountService
        Mockito.when(accountService.createAccount(any(Long.class), any(AccountEntity.class))).thenReturn(account);

        // Convert account to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String accountJson = objectMapper.writeValueAsString(account);

        // Send POST request
        mockMvc.perform(post("/api/accounts/1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("1234567890123456789012"))
                .andExpect(jsonPath("$.balance").value(10000.0))
                .andExpect(jsonPath("$.openDate").exists()) // Verify that openDate is set
                .andExpect(jsonPath("$.openDate").isNotEmpty());
    }


    @Test
    public void testUpdateAccount() throws Exception {
        // Test data
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setBalance(20000.0);
        accountRequest.setShebaNumber("IR123456789012345678901234");

        // Mock behavior of accountService
        AccountEntity updatedAccount = new AccountEntity();
        updatedAccount.setAccountNumber("1234567890123456789012");
        updatedAccount.setBalance(20000.0);
        updatedAccount.setShebaNumber("IR123456789012345678901234");

        Mockito.when(accountService.updateAccount(anyString(), any(AccountRequest.class)))
                .thenReturn(updatedAccount);

        // Convert accountRequest to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String accountRequestJson = objectMapper.writeValueAsString(accountRequest);

        // Send PUT request
        mockMvc.perform(put("/api/accounts/1234567890123456789012")
                        .param("password", "pass123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(20000.0))
                .andExpect(jsonPath("$.shebaNumber").value("IR123456789012345678901234"));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        // Mock behavior of accountService
        Mockito.doNothing().when(accountService).deleteAccount(anyString());

        // Send DELETE request
        mockMvc.perform(delete("/api/accounts/1234567890123456789012")
                        .param("password", "pass123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Account successfully deleted."));
    }

    @Test
    public void testDepositToAccount() throws Exception {
        // Test data
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("1234567890123456789012");
        account.setBalance(20000.0);

        // Mock behavior of accountService
        Mockito.when(accountService.depositToAccount(anyString(), any(Double.class))).thenReturn(account);

        // Send POST request
        mockMvc.perform(post("/api/accounts/1234567890123456789012/deposit")
                        .param("amount", "5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(20000.0));
    }

    @Test
    public void testWithdrawFromAccount() throws Exception {
        // Test data
        AccountEntity account = new AccountEntity();
        account.setAccountNumber("1234567890123456789012");
        account.setBalance(5000.0);

        // Mock behavior of accountService
        Mockito.when(accountService.withdrawFromAccount(anyString(), any(Double.class), anyString(), anyString()))
                .thenReturn(account);

        // Send POST request
        mockMvc.perform(post("/api/accounts/1234567890123456789012/withdraw")
                        .param("amount", "5000")
                        .param("hmacSignature", "someHmacSignature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(5000.0));
    }

    @Test
    public void testGetInvoiceForAccount() throws Exception {
        // Test data
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1L);

        // Mock behavior of accountService
        Mockito.when(accountService.getInvoiceForAccount(anyString(), anyInt(), anyString(), anyString()))
                .thenReturn(invoice);

        // Send GET request
        mockMvc.perform(get("/api/accounts/1234567890123456789012/invoice")
                        .param("n", "10")
                        .param("hmacSignature", "someHmacSignature"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
