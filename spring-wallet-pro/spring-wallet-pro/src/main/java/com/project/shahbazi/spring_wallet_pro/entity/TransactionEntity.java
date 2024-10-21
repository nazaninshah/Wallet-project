package com.project.shahbazi.spring_wallet_pro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table (name = "transaction_table")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt;

    @NotNull(message = "amount cannot be null")
    @Min(value = 1000, message = "minimum transaction amount is 1000")
    @Column(nullable = false)
    private Long amount;

    private String description;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private InvoiceEntity invoice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount (Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public InvoiceEntity getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEntity invoice) {
        this.invoice = invoice;
    }
}
