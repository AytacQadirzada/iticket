package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.ProductEntity;
import com.example.iticket.dao.entity.WishlistEntity;
import com.example.iticket.dao.repository.ProductRepository;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.dao.repository.WishlistRepository;
import com.example.iticket.mapper.WishlistMapper;
import com.example.iticket.model.response.WishlistResponse;
import com.example.iticket.service.concret.ProductService;
import com.example.iticket.service.concret.WishlistService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class WishlistServiceImp implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final WishlistMapper mapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public WishlistResponse getById() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = authentication.getName();
        log.info("WishlistService.getById.start email: {}", email);
        var wishlistId = userRepository.findByEmail(email).get().getWishlist().getId();
        var wishlistEntity = wishlistRepository.findById(wishlistId).get();
        var response = mapper.toResponse(wishlistEntity);
        log.info("WishlistService.getById.end email: {}", email);
        return response;
    }

    @Override
    public void addProduct(Long productId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = authentication.getName();
        log.info("WishlistService.addProduct.start email: {}, productId: {}", email, productId);

        var wishlist = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getWishlist();

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlist.getProducts().contains(productEntity)) {
            wishlist.getProducts().remove(productEntity);
        } else {
            wishlist.getProducts().add(productEntity);
        }

        wishlistRepository.save(wishlist);

        log.info("WishlistService.addProduct.end email: {}, productId: {}", email, productId);
    }

}
