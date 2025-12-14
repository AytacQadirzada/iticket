package com.example.iticket.service.concret;


import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.request.ProductRequest;
import com.example.iticket.model.response.ProductEventResponse;
import com.example.iticket.model.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    void create(ProductRequest request);
    void delete(Long id);
    void update(Long id, ProductRequest request);
    List<ProductResponse> getAllByCategory(Long categoryId);
}
