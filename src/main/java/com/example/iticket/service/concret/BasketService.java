package com.example.iticket.service.concret;

import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.response.BasketResponse;

public interface BasketService {
    void addItem(Long userId, BasketItemRequest request);
    void removeItem(Long userId, Long basketItemId);
    BasketResponse getBasket(Long userId);
}
