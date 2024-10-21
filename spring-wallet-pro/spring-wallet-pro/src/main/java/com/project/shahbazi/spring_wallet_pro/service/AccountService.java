package com.project.shahbazi.spring_wallet_pro.service;

import com.project.shahbazi.spring_wallet_pro.dto.AccountRequest;
import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_wallet_pro.entity.InvoiceEntity;
import com.project.shahbazi.spring_wallet_pro.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private CustomerService customerService;

    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountEntity getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public AccountEntity createAccount(Long customerId, AccountEntity account) {
        // Find the customer entity using the provided customerId
        CustomerEntity customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        account.setCustomer(customer);
        account.setOpenDate(new Date());
        return accountRepository.save(account);
    }

    public List<AccountEntity> createAccounts(List<AccountRequest> accountRequests) {
        List<AccountEntity> createdAccounts = new ArrayList<>();
        for (AccountRequest request : accountRequests) {
            CustomerEntity customer = customerService.getCustomerById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.getCustomerId()));
            AccountEntity account = new AccountEntity();
            account.setAccountNumber(request.getAccountNumber());
            account.setBalance(request.getBalance());
            account.setShebaNumber(request.getShebaNumber());
            account.setPassword(request.getPassword());
            account.setCustomer(customer);
            account.setOpenDate(new Date()); // Set the current date as the open date
            createdAccounts.add(accountRepository.save(account));
        }

        return createdAccounts;
    }

    public AccountEntity validateCredentials(String accountNumber, String password) {
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.getPassword().equals(password)) {
            throw new SecurityException("Invalid accountNumber or password.");
        }
        return account;
    }

    public AccountEntity updateAccount(String accountNumber, AccountRequest accountDetails) {
        AccountEntity account = getAccountByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }
        // Find the new customer by customerId (if provided)
        if (accountDetails.getCustomerId() != null) {
            CustomerEntity newCustomer = customerService.getCustomerById(accountDetails.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + accountDetails.getCustomerId()));
            account.setCustomer(newCustomer); // Update the customer owner
        }

        account.setBalance(accountDetails.getBalance());
        account.setShebaNumber(accountDetails.getShebaNumber());
        account.setPassword(accountDetails.getPassword());
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String accountNumber) {
        accountRepository.deleteByAccountNumber(accountNumber);
    }

    public AccountEntity depositToAccount(String accountNumber, Double amount) {
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }
        if (amount > 0) {
            account.setBalance(account.getBalance() + amount);
            AccountEntity updatedAccount = accountRepository.save(account);
            transactionService.addTransaction(account, amount.longValue(), "Deposit");
            return updatedAccount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
    }

    public AccountEntity withdrawFromAccount(String accountNumber, Double amount, String hmacSignature, String timestamp) throws Exception {
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }
        if (!HmacUtilService.verifyHMACWithTimestamp(accountNumber, "withdrawal", hmacSignature, account.getPassword(), timestamp)) {
            throw new SecurityException("Invalid HMAC signature or request expired. Access denied.");
        }
        if (amount < 100000) {
            throw new IllegalArgumentException("Withdrawal amount must be at least 100,000.");
        }
        if (account.getBalance() - amount < 10000) {
            throw new IllegalArgumentException("Account balance must be at least 10,000 after withdrawal.");
        }
        double totalWithdrawalsToday = transactionService.getTotalWithdrawalsForToday(account);
        if (totalWithdrawalsToday + amount > 10000000) {
            throw new IllegalArgumentException("Total withdrawals for the day cannot exceed 10,000,000.");
        }

        account.setBalance(account.getBalance() - amount);
        AccountEntity updatedAccount = accountRepository.save(account);
        transactionService.addTransaction(account, amount.longValue(), "Withdrawal");

        return updatedAccount;
    }


    public InvoiceEntity getInvoiceForAccount(String accountNumber, int n, String hmacSignature, String timestamp) throws Exception {
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }
        if (!HmacUtilService.verifyHMACWithTimestamp(accountNumber, "invoice", hmacSignature, account.getPassword(), timestamp)) {
            throw new SecurityException("Invalid HMAC signature or request expired. Access denied.");
        }
        return invoiceService.generateInvoice(accountNumber, n);
    }

}

