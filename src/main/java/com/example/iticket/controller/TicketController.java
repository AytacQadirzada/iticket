package com.example.iticket.controller;

import com.example.iticket.model.response.TicketResponse;
import com.example.iticket.service.concret.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ticket")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TicketController {
    private final TicketService service;

    @GetMapping
    public List<TicketResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TicketResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

}
