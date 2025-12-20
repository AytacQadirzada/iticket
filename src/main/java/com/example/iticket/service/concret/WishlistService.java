package com.example.iticket.service.concret;

import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.response.CategoryResponse;
import com.example.iticket.model.response.WishlistResponse;

import java.util.List;

public interface WishlistService {
    WishlistResponse getById(Long userId);
    void addProduct(Long userId, Long productId);
}
