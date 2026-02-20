package com.example.iticket.service.impl

import com.example.iticket.dao.entity.TicketEntity
import com.example.iticket.dao.repository.TicketRepository
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.TicketMapper
import com.example.iticket.model.response.TicketResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class TicketServiceImplTest {

    @Mock
    TicketRepository repository

    @Mock
    TicketMapper mapper

    @InjectMocks
    TicketServiceImpl service

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "getAll should return mapped list"() {
        def entity = new TicketEntity(id: 1L)
        def response = new TicketResponse(id: 1L)

        when(repository.findAll()).thenReturn([entity])
        when(mapper.toResponse(entity)).thenReturn(response)

        def result = service.getAll()

        assertEquals(1, result.size())
        assertEquals(response, result[0])
        verify(repository).findAll()
        verify(mapper).toResponse(entity)
    }

    @Test
    void "getAll should return empty list when no tickets"() {
        when(repository.findAll()).thenReturn([])

        def result = service.getAll()

        assertTrue(result.isEmpty())
        verify(repository).findAll()
    }

    @Test
    void "getById should return mapped response when found"() {
        def entity = new TicketEntity(id: 1L)
        def response = new TicketResponse(id: 1L)

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
}