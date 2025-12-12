package com.example.iticket.service.concret;


import com.example.iticket.model.request.HallRequest;
import com.example.iticket.model.request.ProductEventRequest;
import com.example.iticket.model.response.HallResponse;
import com.example.iticket.model.response.ProductEventResponse;

import java.util.List;

public interface ProductEventService {
    List<ProductEventResponse> getAll();
    ProductEventResponse getById(Long id);
    void create(ProductEventRequest request);
    void delete(Long id);
    void update(Long id, ProductEventRequest request);
}
