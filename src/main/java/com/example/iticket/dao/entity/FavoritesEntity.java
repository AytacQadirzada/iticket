package com.example.iticket.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "favorites")
@Getter
@Setter

public class FavoritesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    @ManyToMany
    @JoinTable(name = "favorites_products", joinColumns = @JoinColumn(name = "favorites_id"), inverseJoinColumns = @JoinColumn(name = "products_id"))
    private List<ProductEntity> Products;

}
