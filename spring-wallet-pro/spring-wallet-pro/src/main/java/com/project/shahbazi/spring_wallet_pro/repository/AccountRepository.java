package com.project.shahbazi.spring_wallet_pro.repository;

import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByAccountNumber(String accountNumber);
    void deleteByAccountNumber(String accountNumber);
}
