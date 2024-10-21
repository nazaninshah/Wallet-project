package com.project.shahbazi.spring_wallet_pro.repository;

import com.project.shahbazi.spring_wallet_pro.entity.AccountEntity;
import com.project.shahbazi.spring_wallet_pro.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    //List<TransactionEntity> findByAccountOrderByCreatedAtAsc(Long accountId);
    Page<TransactionEntity> findByAccountOrderByCreatedAtDesc(AccountEntity account, Pageable pageable);
    List<TransactionEntity> findByAccountAndCreatedAtBetweenAndDescription(AccountEntity account, Date startDate, Date endDate, String description);
}
