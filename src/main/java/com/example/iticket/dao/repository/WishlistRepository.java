package com.example.iticket.dao.repository;

import com.example.iticket.dao.entity.WishlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishlistEntity,Long>{
}
