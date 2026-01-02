package com.example.iticket.controller;

import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.BasketResponse;
import com.example.iticket.service.concret.BasketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/basket")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping("/{userId}")
    public BasketResponse getBasket(@PathVariable Long userId) {
        BasketResponse basket = basketService.getBasket(userId);
        return basket;
    }

    @PostMapping
    public void addItem(@RequestBody BasketItemRequest request) {
        basketService.addItem(request);
    }

    @PutMapping("/{userId}/{basketItemId}")
    public void removeItem(@PathVariable Long userId, @PathVariable Long basketItemId) {
        basketService.removeItem(userId, basketItemId);
    }

    @PostMapping("/buy/{userId}/{basketId}")
    public void buy(@PathVariable Long userId, @PathVariable Long basketId, @RequestBody CardRequest request) {
        basketService.buy(basketId, userId, request);
    }

}
