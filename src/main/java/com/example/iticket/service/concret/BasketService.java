package com.example.iticket.service.concret;

import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.BasketResponse;

public interface BasketService {
    void addItem(BasketItemRequest request);
    void removeItem(Long userId, Long basketItemId);
    BasketResponse getBasket(Long userId);
    void buy(Long basketId, Long userId, CardRequest request);
}
