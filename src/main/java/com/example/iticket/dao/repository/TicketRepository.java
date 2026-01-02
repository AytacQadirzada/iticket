package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.SectorEntity;
import com.example.iticket.dao.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity,Long> {
    TicketEntity findByNumber(String ticketNumber);
    List<TicketEntity> findByProductEventIdAndIsBooked(Long productEventId, boolean isBooked);
    List<TicketEntity> findByProductEventIdAndSectorIdAndIsBooked(Long productEventId, Long sectorId, boolean isBooked);
}
