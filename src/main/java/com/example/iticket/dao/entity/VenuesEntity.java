package com.example.iticket.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venues")
@Setter
@Getter
public class VenuesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(unique = true, nullable = false)
    private String address;
    @Column(nullable = false)
    private String mobile;
//    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String mapLat;
    @Column(nullable = false)
    private String mapLng;

    @OneToMany(mappedBy = "venue")
    private List<HallEntity> halls;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VenuesEntity that = (VenuesEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
