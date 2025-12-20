package com.example.iticket.dao.entity;

import com.example.iticket.enums.SectorClassification;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sectors")
@Setter
@Getter
public class SectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private SectorClassification sectorClassification;

    private Long rowNumber;

    private Long columnNumber;

    private Long capacity;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private HallEntity hall;

    @OneToMany(mappedBy = "sector")
    private List<TicketEntity> ticket;

    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SectorEntity that = (SectorEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
