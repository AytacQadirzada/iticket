package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.ProductEntity;
import com.example.iticket.dao.entity.SeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<SeatEntity,Long> {

    List<SeatEntity> findBySectorId(Long id);
}
