package com.example.iticket.controller;

import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.ProductEventResponse;
import com.example.iticket.service.concret.ProductEventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("update/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductEventRequest request){
        service.update(id,request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

    @GetMapping("/getAllByProductId/{productId}")
    public List<ProductEventResponse> getAllByProductId(@PathVariable Long productId){
        return service.getProductEventsByProductId(productId);
    }

}
