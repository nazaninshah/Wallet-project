package com.project.shahbazi.spring_wallet_pro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "account_table")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Account number cannot be null")
    @Size(min = 22, max = 22, message = "Account number must be 22 digits")
    @Pattern(regexp = "^[0-9]{22}$", message = "Account number must be 22 digits")
    @Column(nullable = false, unique = true)
    private String accountNumber; // AES encrypted accountNumber

    @Min(value = 10000, message = "Minimum balance is 10000!")
    private Double balance; // AES encrypted balance

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date openDate;

    @NotNull(message = "Sheba number cannot be null")
    @Size(min = 26, max = 26, message = "Account sheba must be 26 char")
    @Pattern(regexp = "^IR[0-9]{24}$", message = "Account sheba must be 26 char")
    @Column(nullable = false, unique = true)
    private String shebaNumber; // AES encrypted shebaNumber

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CustomerEntity customer;

    @Size(min = 6, max = 10, message = "Account password must be at least 6 and at most 10 chars")
    @NotEmpty (message = "Password cannot be null")
    @Column(nullable = false)
    private String password;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber (String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance (Double balance) {
        this.balance = balance;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getShebaNumber() {
        return shebaNumber;
    }

    public void setShebaNumber (String shebaNumber) {
        this.shebaNumber = shebaNumber;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Set<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
