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

    @GetMapping
    public WishlistResponse getById(){
        return wishlistService.getById();
    }

    @PutMapping("/{productId}")
    public void addProduct(@PathVariable Long productId){
        wishlistService.addProduct(productId);
    }
}
