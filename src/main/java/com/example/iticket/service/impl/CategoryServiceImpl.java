package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.CategoryEntity;
import com.example.iticket.dao.repository.CategoryRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.CategoryMapper;
import com.example.iticket.model.request.CategoryRequest;
import com.example.iticket.model.response.CategoryResponse;
import com.example.iticket.service.concret.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public List<CategoryResponse> getAll() {
        log.info("ActionLog.getAll.start");
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        var categories = categoryEntities.stream().map(categoryMapper::toResponse).toList();
        log.info("ActionLog.getAll.end");
        return categories;
    }

    @Override
    public CategoryResponse getById(Long id) {
        log.info("ActionLog.getById.start id: {} ", id);
        CategoryEntity  categoryEntity = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.getById.error Category not found with id: {} ", id);
            return new NotFoundException("Category not found");
        });
        var categoryResponse = categoryMapper.toResponse(categoryEntity);
        log.info("ActionLog.getById.end id: {} ", id);
        return categoryResponse;
    }

    @Override
    public void createCategory(CategoryRequest category) {
        log.info("ActionLog.createCategory.start name: {} ", category.getName());
        var  categoryEntity = categoryMapper.toEntity(category);
        categoryRepository.save(categoryEntity);
        log.info("ActionLog.createCategory.end name: {} ", categoryEntity.getName());
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("ActionLog.deleteCategory.start id: {} ", id);
        categoryRepository.deleteById(id);
        log.info("ActionLog.deleteCategory.end id: {} ", id);

    }

    @Override
    public void updateCategory(Long id, CategoryRequest category) {
        log.info("ActionLog.updateCategory.start id: {} ", id);
        var entity = categoryRepository.findById(id).orElseThrow(() -> {
            log.error("ActionLog.updateCategory.error Category not found with id: {} ", id);
            return new NotFoundException("Category not found");
        });
        categoryMapper.mapForUpdate(category, entity);
        entity.setId(id);
        categoryRepository.save(entity);
        log.info("ActionLog.updateCategory.end id: {} ", id);
    }
}
