package com.project.shahbazi.spring_wallet_pro.controller;

import com.project.shahbazi.spring_wallet_pro.dto.AccountRequest;
import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.entity.InvoiceEntity;
import com.project.shahbazi.spring_wallet_pro.service.AccountService;
import com.project.shahbazi.spring_wallet_pro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Get all accounts
    @GetMapping
    public List<AccountEntity> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // Get account by account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountEntity> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountEntity account = accountService.getAccountByAccountNumber(accountNumber);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new account
    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<AccountEntity> createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountEntity account) {
        AccountEntity createdAccount = accountService.createAccount(customerId, account);
        return ResponseEntity.status(201).body(createdAccount);
    }

    // Create a list of accounts
    @PostMapping("/batch")
    public ResponseEntity<List<AccountEntity>> createAccounts(@Valid @RequestBody List<AccountRequest> accountRequests) {
        List<AccountEntity> createdAccounts = accountService.createAccounts(accountRequests);
        return ResponseEntity.status(201).body(createdAccounts); // Return 201 Created
    }


    // Update account by account number
    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountEntity> updateAccount(@PathVariable String accountNumber,@RequestParam String password, @Valid @RequestBody AccountRequest accountDTO) {
        // Validate credentials before allowing the update
        accountService.validateCredentials(accountNumber, password);
        AccountEntity updatedAccount = accountService.updateAccount(accountNumber, accountDTO);
        return ResponseEntity.ok(updatedAccount);
    }


    // Delete account by account number
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber, @RequestParam String password) {
        // Validate credentials before allowing delete
        accountService.validateCredentials(accountNumber, password);
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok("Account successfully deleted.");
    }

    // Deposit to account using account number
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<AccountEntity> depositToAccount(@PathVariable String accountNumber, @RequestParam Double amount) {
        return ResponseEntity.ok(accountService.depositToAccount(accountNumber, amount));
    }

    // Withdraw from account using account number
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<AccountEntity> withdrawFromAccount(@PathVariable String accountNumber, @RequestParam Double amount, @RequestParam String hmacSignature) throws Exception {
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        AccountEntity account = accountService.withdrawFromAccount(accountNumber, amount, hmacSignature, timestamp);
        return ResponseEntity.ok(account);
    }

    // Get invoice for account using account number
    @GetMapping("/{accountNumber}/invoice")
    public ResponseEntity<InvoiceEntity> getInvoiceForAccount(@PathVariable String accountNumber, @RequestParam int n, @RequestParam String hmacSignature) throws Exception {
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        InvoiceEntity invoice = accountService.getInvoiceForAccount(accountNumber, n, hmacSignature, timestamp);
        return ResponseEntity.ok(invoice);
    }
}