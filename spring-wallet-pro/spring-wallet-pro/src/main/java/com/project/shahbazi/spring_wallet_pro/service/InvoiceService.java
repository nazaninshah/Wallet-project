package com.project.shahbazi.spring_wallet_pro.service;

import com.project.shahbazi.spring_wallet_pro.entity.InvoiceEntity;
import com.project.shahbazi.spring_wallet_pro.entity.TransactionEntity;
import com.project.shahbazi.spring_wallet_pro.repository.InvoiceRepository;
import com.project.shahbazi.spring_wallet_pro.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private InvoiceRepository invoiceRepository;

    public InvoiceEntity generateInvoice(String accountNumber, int n) {
        List<TransactionEntity> transactions = transactionService.getLastNTransactions(accountNumber, n);
        InvoiceEntity invoice = new InvoiceEntity(new HashSet<>(transactions));
        return invoiceRepository.save(invoice);
    }
}
