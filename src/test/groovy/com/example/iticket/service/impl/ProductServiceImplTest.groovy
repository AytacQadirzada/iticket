package com.example.iticket.service.impl

import com.example.iticket.dao.entity.*
import com.example.iticket.dao.repository.*
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.ProductEventMapper
import com.example.iticket.mapper.ProductMapper
import com.example.iticket.model.request.ProductEventRequest
import com.example.iticket.model.request.ProductRequest
import com.example.iticket.model.request.SectorPriceRequest
import com.example.iticket.model.response.ProductResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class ProductServiceImplTest {

    @Mock
    ProductRepository repository

    @Mock
    ProductMapper mapper

    @Mock
    SectorRepository sectorRepository

    @Mock
    TicketRepository ticketRepository

    @Mock
    CategoryRepository categoryRepository

    @Mock
    ProductEventMapper productEventMapper

    @InjectMocks
    ProductServiceImpl productService

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "getAll should return mapped list"() {
        def entity = new ProductEntity(id: 1L, title: "Concert")
        def response = new ProductResponse(id: 1L, title: "Concert")

        when(repository.findAll()).thenReturn([entity])
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = productService.getAll()

        assertEquals(1, result.size())
        assertEquals("Concert", result[0].title)
        verify(repository).findAll()
        verify(mapper).toResponse(entity)
    }

    @Test
    void "getById should return product when found"() {
        def entity = new ProductEntity(id: 1L, title: "Concert")
        def response = new ProductResponse(id: 1L, title: "Concert")

        when(repository.findById(1L)).thenReturn(Optional.of(entity))
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = productService.getById(1L)

        assertEquals(1L, result.id)
        assertEquals("Concert", result.title)
        verify(repository).findById(1L)
        verify(mapper).toResponse(entity)
    }

    @Test
    void "getById should throw NotFoundException when not found"() {
        when(repository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            productService.getById(1L)
        }
    }

    @Test
    void "create should save product with events and tickets"() {
        def request = new ProductRequest(
                title: "Concert",
                categoryId: 1L,
                productEvents: [
                        new ProductEventRequest(
                                sectorPrices: [
                                        new SectorPriceRequest(sectorId: 1L, price: 100.0)
                                ]
                        )
                ]
        )

        def entity = new ProductEntity(id: 1L, title: "Concert")
        def eventEntity = new ProductEventEntity()
        def sector = new SectorEntity(id: 1L, rowNumber: 2, columnNumber: 2, capacity: 0, hall: new HallEntity(id: 1L))

        when(mapper.toEntity(request)).thenReturn(entity)
        when(productEventMapper.toEntity(any())).thenReturn(eventEntity)
        when(sectorRepository.findById(1L)).thenReturn(Optional.of(sector))
        when(sectorRepository.findByHallId(1L)).thenReturn([sector])
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new CategoryEntity(id: 1L)))

        productService.create(request)

        verify(repository).save(entity)
        verify(ticketRepository, atLeastOnce()).save(any(TicketEntity.class))
    }

    @Test
    void "delete should call deleteById"() {
        productService.delete(1L)
        verify(repository).deleteById(1L)
    }

    @Test
    void "update should update existing product"() {
        def request = new ProductRequest(title: "Updated")
        def entity = new ProductEntity(id: 1L, title: "Old")

        when(repository.findById(1L)).thenReturn(Optional.of(entity))

        productService.update(1L, request)

        verify(mapper).mapForUpdate(request, entity)
        verify(repository).save(entity)
    }

    @Test
    void "update should throw NotFoundException when product not found"() {
        def request = new ProductRequest(title: "Updated")
        when(repository.findById(1L)).thenReturn(Optional.empty())

        assertThrows(NotFoundException) {
            productService.update(1L, request)
        }
    }

    @Test
    void "getAllByCategory should return mapped list"() {
        def entity = new ProductEntity(id: 1L, title: "Concert")
        def response = new ProductResponse(id: 1L, title: "Concert")

        when(repository.getAllByCategoryId(1L)).thenReturn([entity])
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = productService.getAllByCategory(1L)

        assertEquals(1, result.size())
        assertEquals("Concert", result[0].title)
        verify(repository).getAllByCategoryId(1L)
        verify(mapper).toResponse(entity)
    }
}