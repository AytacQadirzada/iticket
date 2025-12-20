package com.example.iticket.controller;

import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.service.concret.HallService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/hall")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HallController {
    private final HallService service;

    @GetMapping
    public List<HallResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public HallResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody HallRequest request){
        service.create(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody HallRequest request){
        service.update(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        service.delete(id);
    }

}
