package com.example.iticket.service.impl

import com.example.iticket.dao.entity.ProductEventEntity
import com.example.iticket.dao.repository.HallRepository
import com.example.iticket.dao.repository.ProductEventRepository
import com.example.iticket.dao.repository.ProductRepository
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.ProductEventMapper
import com.example.iticket.model.request.ProductEventRequest
import com.example.iticket.model.response.ProductEventResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class ProductEventServiceImplTest {

    @Mock
    ProductEventRepository repository

    @Mock
    HallRepository hallRepository

    @Mock
    ProductRepository productRepository

    @Mock
    ProductEventMapper mapper

    @InjectMocks
    ProductEventServiceImpl service

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "getAll should return mapped list"() {
        def entity = new ProductEventEntity(id: 1L)
        def response = new ProductEventResponse(id: 1L)

        when(repository.findAll()).thenReturn([entity])
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = service.getAll()

        assertEquals(1, result.size())
        assertEquals(response, result[0])
        verify(repository).findAll()
        verify(mapper).toResponse(entity)
    }

    @Test
    void "getAll should return empty list when no events"() {
        when(repository.findAll()).thenReturn([])

        def result = service.getAll()

        assertTrue(result.isEmpty())
        verify(repository).findAll()
    }

    @Test
    void "getById should return mapped response when found"() {
        def entity = new ProductEventEntity(id: 1L)
        def response = new ProductEventResponse(id: 1L)

        when(repository.findById(1L)).thenReturn(Optional.of(entity))
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = service.getById(1L)

        assertEquals(1L, result.id)
        verify(repository).findById(1L)
        verify(mapper).toResponse(entity)
    }

    @Test
    void "getById should throw NotFoundException when not found"() {
        when(repository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            service.getById(1L)
        }
        verify(repository).findById(1L)
    }

    @Test
    void "delete should call repository deleteById"() {
        service.delete(1L)

        verify(repository).deleteById(1L)
    }

    @Test
    void "delete should throw exception when repository fails"() {
        doThrow(new RuntimeException("DB error")).when(repository).deleteById(1L)

        assertThrows(RuntimeException) {
            service.delete(1L)
        }
        verify(repository).deleteById(1L)
    }

    @Test
    void "update should map and save entity when found"() {
        def request = new ProductEventRequest()
        def entity = new ProductEventEntity(id: 1L)

        when(repository.findById(1L)).thenReturn(Optional.of(entity))

        service.update(1L, request)

        verify(repository).findById(1L)
        verify(mapper).mapForUpdate(request, entity)
        verify(repository).save(entity)
    }

    @Test
    void "update should throw NotFoundException when entity not found"() {
        when(repository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            service.update(1L, new ProductEventRequest())
        }
        verify(repository).findById(1L)
    }

    @Test
    void "update should throw exception when save fails"() {
        def request = new ProductEventRequest()
        def entity = new ProductEventEntity(id: 1L)

        when(repository.findById(1L)).thenReturn(Optional.of(entity))
        when(repository.save(entity)).thenThrow(new RuntimeException("DB error"))

        assertThrows(RuntimeException) {
            service.update(1L, request)
        }

        verify(repository).findById(1L)
        verify(mapper).mapForUpdate(request, entity)
        verify(repository).save(entity)
    }
}