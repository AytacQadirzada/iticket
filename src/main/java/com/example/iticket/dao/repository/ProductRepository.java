package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.ProductEntity;
import com.example.iticket.dao.entity.ProductEventEntity;
import com.example.iticket.model.response.ProductResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> getAllByCategoryId(Long categoryId);
}
