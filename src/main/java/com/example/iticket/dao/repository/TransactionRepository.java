package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> getAllByUserId(Long userId);
}
