package com.project.shahbazi.spring_wallet_pro.service;

import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.entity.TransactionEntity;
import com.project.shahbazi.spring_wallet_pro.repository.AccountRepository;
import com.project.shahbazi.spring_wallet_pro.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    public TransactionEntity addTransaction(AccountEntity account, Long amount, String description) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCreatedAt(new Date());
        return transactionRepository.save(transaction);
    }

    // Method to enforce the limit of reporting 100 transactions per invoice to avoid huge invoice records
    public List<TransactionEntity> getLastNTransactions(String accountNumber, int n) {
        if (n < 1 || n > 100){
            throw new IllegalArgumentException("The number of transactions (n) must be between 1 and 100.");
        }
        // Retrieve the AccountEntity using the account number
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }

        // Use PageRequest to limit the result to 'n' transactions
        Pageable pageable = PageRequest.of(0, n);
        return transactionRepository.findByAccountOrderByCreatedAtDesc(account, pageable).getContent();
    }

    public double getTotalWithdrawalsForToday(AccountEntity account) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startOfDay = calendar.getTime(); // represents 12:00 AM (midnight) of the current day
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endOfDay = calendar.getTime(); //representing the start (12:00 AM) of the next day
        List<TransactionEntity> withdrawalsToday = transactionRepository.findByAccountAndCreatedAtBetweenAndDescription(
                account, startOfDay, endOfDay, "Withdrawal");
        return withdrawalsToday.stream().mapToDouble(TransactionEntity::getAmount).sum();
    }

}

