package com.example.iticket.service.concret;

import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    void createCategory(CategoryRequest category);
    void deleteCategory(Long id);
    void updateCategory(Long id, CategoryRequest category);
}
