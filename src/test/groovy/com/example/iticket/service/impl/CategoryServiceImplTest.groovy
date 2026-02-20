package com.example.iticket.service.impl

import com.example.iticket.dao.entity.CategoryEntity
import com.example.iticket.dao.repository.CategoryRepository
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.CategoryMapper
import com.example.iticket.model.request.CategoryRequest
import com.example.iticket.model.response.CategoryResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Mockito.*
import static org.junit.jupiter.api.Assertions.*

class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository

    @Mock
    CategoryMapper categoryMapper

    @InjectMocks
    CategoryServiceImpl categoryService

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "getAll should return mapped list"() {
        def entity = new CategoryEntity(id: 1L, name: "Music")
        def response = new CategoryResponse(id: 1L, name: "Music")

        when(categoryRepository.findAll()).thenReturn([entity])
        when(categoryMapper.toResponse(entity)).thenReturn(response)

        def result = categoryService.getAll()

        assertEquals(1, result.size())
        verify(categoryRepository).findAll()
        verify(categoryMapper).toResponse(entity)
    }

    @Test
    void "getAll should return empty list when no categories"() {
        when(categoryRepository.findAll()).thenReturn([])

        def result = categoryService.getAll()

        assertTrue(result.isEmpty())
        verify(categoryRepository).findAll()
    }

    @Test
    void "getAll should throw exception when repository fails"() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("DB error"))

        assertThrows(RuntimeException) {
            categoryService.getAll()
        }
    }

    @Test
    void "getById should return category when found"() {
        def entity = new CategoryEntity(id: 1L)
        def response = new CategoryResponse(id: 1L)

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity))
        when(categoryMapper.toResponse(entity)).thenReturn(response)

        def result = categoryService.getById(1L)

        assertEquals(1L, result.id)
        verify(categoryMapper).toResponse(entity)
    }

    @Test
    void "getById should throw NotFoundException when not found"() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            categoryService.getById(1L)
        }
    }

    @Test
    void "createCategory should save entity"() {
        def request = new CategoryRequest(name: "Tech")
        def entity = new CategoryEntity(name: "Tech")

        when(categoryMapper.toEntity(request)).thenReturn(entity)

        categoryService.createCategory(request)

        verify(categoryMapper).toEntity(request)
        verify(categoryRepository).save(entity)
    }

    @Test
    void "createCategory should throw exception when save fails"() {
        def request = new CategoryRequest(name: "Tech")
        def entity = new CategoryEntity(name: "Tech")

        when(categoryMapper.toEntity(request)).thenReturn(entity)
        when(categoryRepository.save(entity)).thenThrow(new RuntimeException("DB error"))

        assertThrows(RuntimeException) {
            categoryService.createCategory(request)
        }
    }

    @Test
    void "deleteCategory should call deleteById"() {
        categoryService.deleteCategory(1L)

        verify(categoryRepository).deleteById(1L)
    }

    @Test
    void "deleteCategory should throw exception when repository fails"() {
        doThrow(new RuntimeException("DB error"))
                .when(categoryRepository).deleteById(1L)

        assertThrows(RuntimeException) {
            categoryService.deleteCategory(1L)
        }
    }

    @Test
    void "updateCategory should update and save entity"() {
        def request = new CategoryRequest(name: "Updated")
        def entity = new CategoryEntity(id: 1L, name: "Old")

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity))

        categoryService.updateCategory(1L, request)

        verify(categoryMapper).mapForUpdate(request, entity)
        verify(categoryRepository).save(entity)
    }

    @Test
    void "updateCategory should throw NotFoundException when entity not found"() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            categoryService.updateCategory(1L, new CategoryRequest())
        }
    }

    @Test
    void "updateCategory should throw exception when save fails"() {
        def request = new CategoryRequest(name: "Updated")
        def entity = new CategoryEntity(id: 1L)

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity))
        when(categoryRepository.save(entity)).thenThrow(new RuntimeException("DB error"))

        assertThrows(RuntimeException) {
            categoryService.updateCategory(1L, request)
        }
    }
}