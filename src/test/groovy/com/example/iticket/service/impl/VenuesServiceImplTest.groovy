package com.example.iticket.service.impl

import com.example.iticket.dao.entity.HallEntity
import com.example.iticket.dao.entity.SectorEntity
import com.example.iticket.dao.entity.VenuesEntity
import com.example.iticket.dao.repository.HallRepository
import com.example.iticket.dao.repository.VenuesRepository
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.HallMapper
import com.example.iticket.mapper.SectorMapper
import com.example.iticket.mapper.VenuesMapper
import com.example.iticket.model.request.HallUpdateRequestDto
import com.example.iticket.model.request.VenuesRequest
import com.example.iticket.model.request.VenuesUpdateRequestDto
import com.example.iticket.model.response.VenuesResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class VenuesServiceImplTest {

    @Mock
    VenuesRepository venuesRepository

    @Mock
    HallRepository hallRepository

    @Mock
    VenuesMapper venuesMapper

    @Mock
    HallMapper hallMapper

    @Mock
    SectorMapper sectorMapper

    @InjectMocks
    VenuesServiceImpl venuesService

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "getAll should return mapped venues"() {
        def venueEntity = new VenuesEntity(id: 1L, name: "Venue1")
        when(venuesRepository.findAll()).thenReturn([venueEntity])
        def venueResponse = mock(VenuesResponse)
        when(venuesMapper.toResponse(venueEntity)).thenReturn(venueResponse)

        def result = venuesService.getAll()

        assertEquals(1, result.size())
        assertEquals(venueResponse, result[0])
    }

    @Test
    void "getById should return mapped venue if found"() {
        def venueEntity = new VenuesEntity(id: 1L, name: "Venue1")
        when(venuesRepository.findById(1L)).thenReturn(Optional.of(venueEntity))
        def venueResponse = mock(VenuesResponse)
        when(venuesMapper.toResponse(venueEntity)).thenReturn(venueResponse)

        def result = venuesService.getById(1L)

        assertEquals(venueResponse, result)
    }

    @Test
    void "getById should throw NotFoundException if venue not found"() {
        when(venuesRepository.findById(1L)).thenReturn(Optional.empty())
        assertThrows(NotFoundException) {
            venuesService.getById(1L)
        }
    }

    @Test
    void "createVenues should save venue and set relations"() {
        def sector = new SectorEntity()
        def hall = new HallEntity(sectors: [sector])
        def venueRequest = new VenuesRequest(name: "Venue1", halls: [mock(HallUpdateRequestDto)])
        def venueEntity = new VenuesEntity(name: "Venue1", halls: [hall])

        when(venuesMapper.toEntity(venueRequest)).thenReturn(venueEntity)

        venuesService.createVenues(venueRequest)

        assertEquals(venueEntity, hall.venue)
        assertEquals(hall, sector.hall)
        verify(venuesRepository).save(venueEntity)
    }

    @Test
    void "deleteVenues should call repository deleteById"() {
        venuesService.deleteVenues(1L)
        verify(venuesRepository).deleteById(1L)
    }

    @Test
    void "updateVenues should add new hall if not exists"() {
        def venueEntity = new VenuesEntity(id: 1L, name: "Venue1", halls: [])

        when(venuesRepository.findById(1L)).thenReturn(Optional.of(venueEntity))

        def hallDto = mock(HallUpdateRequestDto)
        def venueUpdateDto = new VenuesUpdateRequestDto(halls: [hallDto])

        // HallEntity-ni mock edirik ki, Mockito stub-ları işləsin
        def newHall = mock(HallEntity)

        // doReturn() istifadə edirik və Set qaytarırıq
        doReturn(new HashSet<SectorEntity>()).when(newHall).getSectors()

        when(hallMapper.toEntity(hallDto)).thenReturn(newHall)

        venuesService.updateVenues(1L, venueUpdateDto)

        assertTrue(venueEntity.halls.contains(newHall))
        verify(venuesRepository).save(venueEntity)
    }

    @Test
    void "updateVenues should throw NotFoundException if venue not found"() {
        when(venuesRepository.findById(1L)).thenReturn(Optional.empty())
        def venueUpdateDto = new VenuesUpdateRequestDto(halls: [])
        assertThrows(NotFoundException) {
            venuesService.updateVenues(1L, venueUpdateDto)
        }
    }
}