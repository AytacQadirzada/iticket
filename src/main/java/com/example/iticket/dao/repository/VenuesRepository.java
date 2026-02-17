package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.HallEntity;
import com.example.iticket.dao.entity.VenuesEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenuesRepository extends JpaRepository<VenuesEntity, Long> {
    @Override
    @EntityGraph(attributePaths = {"halls", "halls.sectors"})
    List<VenuesEntity> findAll();

}
