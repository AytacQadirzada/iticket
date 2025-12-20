package com.example.iticket.controller;

import com.example.iticket.model.request.ProductRequest;
import com.example.iticket.model.response.ProductResponse;
import com.example.iticket.service.concret.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService service;

    @GetMapping
    public List<ProductResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody ProductRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
