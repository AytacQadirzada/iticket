package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.HallEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HallRepository extends JpaRepository<HallEntity,Long> {
    @Override
    @Query(value = "SELECT h FROM HallEntity h left JOIN SectorEntity s ON s.hall.id = h.id")
    List<HallEntity> findAll();
}
