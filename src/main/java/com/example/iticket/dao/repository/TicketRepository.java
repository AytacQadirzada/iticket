package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.SectorEntity;
import com.example.iticket.dao.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity,Long> {

}
