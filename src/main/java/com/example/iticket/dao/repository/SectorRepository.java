package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<SectorEntity,Long> {

    List<SectorEntity> findByHallId(Long id);
}
