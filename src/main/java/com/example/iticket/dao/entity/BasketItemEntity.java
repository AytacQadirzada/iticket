package com.example.iticket.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "basket_items")
@Getter
@Setter
public class BasketItemEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private String ticketNumber;


    @ManyToOne
    @JoinColumn(name = "basket_id")
    private BasketEntity basket;
    private Long productId;
}
