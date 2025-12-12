package com.example.iticket.controller;

import com.example.iticket.model.request.TicketRequest;
import com.example.iticket.model.response.TicketResponse;
import com.example.iticket.service.concret.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ticket")
@RequiredArgsConstructor
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody TicketRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody TicketRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
