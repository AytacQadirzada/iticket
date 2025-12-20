package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.BasketEntity;
import com.example.iticket.dao.entity.BasketItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketItemRepository extends JpaRepository<BasketItemEntity,Long> {
}
