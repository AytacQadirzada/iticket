package com.example.iticket.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/basket")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BasketController {
}
