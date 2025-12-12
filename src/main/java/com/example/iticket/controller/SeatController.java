package com.example.iticket.controller;

import com.example.iticket.model.request.SeatRequest;
import com.example.iticket.model.response.SeatResponse;
import com.example.iticket.service.concret.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService service;

    @GetMapping
    public List<SeatResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SeatResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody SeatRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody SeatRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
