package com.example.iticket.controller;

import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.request.VenuesRequest;
import com.example.iticket.model.response.CategoryResponse;
import com.example.iticket.model.response.VenuesResponse;
import com.example.iticket.service.concret.CategoryService;
import com.example.iticket.service.concret.VenuesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/venues")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class VenuesController {
    private final VenuesService venuesService;

    @GetMapping
    public List<VenuesResponse> getAll(){
        return venuesService.getAll();
    }

    @GetMapping("/{id}")
    public VenuesResponse getById(@PathVariable Long id){
        return venuesService.getById(id);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createVenues(@RequestBody VenuesRequest request){
        venuesService.createVenues(request);
    }

    @PutMapping("/{id}")
    public void updateVenues(@PathVariable Long id, @RequestBody VenuesRequest request){
        venuesService.updateVenues(id,request);
    }

    @DeleteMapping("/{id}")
    public void deleteVenues(@PathVariable Long id){
        venuesService.deleteVenues(id);
    }
}
