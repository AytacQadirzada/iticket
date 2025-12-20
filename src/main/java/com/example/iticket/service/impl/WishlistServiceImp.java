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
    public WishlistResponse getById(Long userId) {
        log.info("WishlistService.getById.start userId: {}", userId);
        var wishlistId = userRepository.findById(userId).get().getWishlist().getId();
        var wishlistEntity = wishlistRepository.findById(wishlistId).get();
        var response = mapper.toResponse(wishlistEntity);
        log.info("WishlistService.getById.end userId: {}", userId);
        return response;
    }

    @Override
    public void addProduct(Long userId, Long productId) {
        log.info("WishlistService.addProduct.start userId: {}, productId: {}", userId, productId);

        var wishlist = userRepository.findById(userId)
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

        log.info("WishlistService.addProduct.end userId: {}, productId: {}", userId, productId);
    }

}
