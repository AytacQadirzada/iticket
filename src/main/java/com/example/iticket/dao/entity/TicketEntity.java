package com.example.iticket.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tickets")
@Setter
@Getter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private String number;
    private Long rowNumber;
    private Long columnNumber;
    private boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @ManyToOne
    @JoinColumn(name = "product_event_id")
    private ProductEventEntity productEvent;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
