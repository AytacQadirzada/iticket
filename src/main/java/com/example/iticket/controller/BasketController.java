package com.example.iticket.controller;

import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.BasketResponse;
import com.example.iticket.service.concret.BasketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/basket")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping
    public BasketResponse getBasket() {
        return basketService.getBasket();
    }

    @PostMapping
    public void addItem(@RequestBody BasketItemRequest request) {
        basketService.addItem(request);
    }

    @DeleteMapping("/{basketItemId}")
    public void removeItem(@PathVariable Long basketItemId) {
        basketService.removeItem(basketItemId);
    }

    @PostMapping("/buy/{basketId}")
    public void buy(@PathVariable Long basketId, @Valid @RequestBody CardRequest request) {
        basketService.buy(basketId, request);
    }

}
