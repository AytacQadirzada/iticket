package com.example.iticket.controller;

import com.example.iticket.model.request.SectorRequest;
import com.example.iticket.model.response.SectorResponse;
import com.example.iticket.service.concret.SectorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sector")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SectorController {
    private final SectorService service;

    @GetMapping
    public List<SectorResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SectorResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody SectorRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody SectorRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
