package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.BasketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<BasketEntity,Long> {
}
