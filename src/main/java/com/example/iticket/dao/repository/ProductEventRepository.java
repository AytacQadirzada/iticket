package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.ProductEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductEventRepository extends JpaRepository<ProductEventEntity,Long> {

}
