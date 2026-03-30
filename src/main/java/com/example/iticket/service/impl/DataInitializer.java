package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.BasketEntity;
import com.example.iticket.dao.entity.UserEntity;
import com.example.iticket.dao.entity.WishlistEntity;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@gmail.com";

        var admin = userRepository.findByEmail(adminEmail);

        if (admin.isEmpty()) {
            UserEntity user = new UserEntity();
            WishlistEntity wishlistEntity = new WishlistEntity();
            BasketEntity basketEntity = new BasketEntity();
            basketEntity.setUser(user);
            wishlistEntity.setUser(user);
            user.setName("Admin");
            user.setSurname("Admin");
            user.setEmail(adminEmail);
            user.setPhone("1234567890");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setCountry("Azerbaijan");
            user.setEmailVerified(true);
            user.setDateOfBirth(LocalDate.of(2005,1,18));
            user.setGender(false);
            user.setWishlist(wishlistEntity);
            user.setBasket(basketEntity);
            user.setBalance(BigDecimal.valueOf(0));
            user.setRoles(Set.of(Role.ROLE_USER.name(), Role.ROLE_ADMIN.name()));
            userRepository.save(user);
        }
    }
}