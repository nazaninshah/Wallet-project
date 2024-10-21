package com.project.shahbazi.spring_wallet_pro.repository;

import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
