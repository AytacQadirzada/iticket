package com.example.iticket.controller;

import com.example.iticket.model.response.WishlistResponse;
import com.example.iticket.service.concret.WishlistService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/wishlist")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/{userId}")
    public WishlistResponse getById(@PathVariable Long userId){
        return wishlistService.getById(userId);
    }

    @PutMapping("/{userId}/{productId}")
    public void addProduct(@PathVariable Long userId,@PathVariable Long productId){
        wishlistService.addProduct(userId, productId);
    }
}
