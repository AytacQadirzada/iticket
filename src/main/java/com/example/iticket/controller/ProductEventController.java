package com.example.iticket.controller;

import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.ProductEventResponse;
import com.example.iticket.service.concret.ProductEventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/productEvent")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductEventController {
    private final ProductEventService service;

    @GetMapping
    public List<ProductEventResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductEventResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductEventRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductEventRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
