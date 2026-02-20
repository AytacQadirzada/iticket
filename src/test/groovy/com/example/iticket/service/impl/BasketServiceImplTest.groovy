package com.example.iticket.service.impl

import com.example.iticket.dao.entity.*
import com.example.iticket.dao.repository.*
import com.example.iticket.exception.NotFoundException
import com.example.iticket.mapper.BasketItemMapper
import com.example.iticket.mapper.BasketMapper
import com.example.iticket.mapper.ProductEventMapper
import com.example.iticket.model.request.BasketItemRequest
import com.example.iticket.model.request.CardRequest
import com.example.iticket.model.response.BasketResponse
import com.example.iticket.model.response.BasketItemResponse
import com.example.iticket.model.response.IyzicoPaymentResult
import com.example.iticket.model.response.ProductEventResponse
import com.example.iticket.service.impl.BasketServiceImpl
import com.example.iticket.service.concret.IyzicoPaymentService
import com.example.iticket.service.concret.MailService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class BasketServiceImplTest {

    @Mock
    UserRepository userRepository
    @Mock
    BasketRepository basketRepository
    @Mock
    BasketItemRepository basketItemRepository
    @Mock
    TicketRepository ticketRepository
    @Mock
    BasketMapper mapper
    @Mock
    BasketItemMapper basketItemMapper
    @Mock
    IyzicoPaymentService iyzicoPaymentService
    @Mock
    MailService mailService
    @Mock
    ProductEventMapper productEventMapper

    @InjectMocks
    BasketServiceImpl basketService

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "addItem should add ticket to basket successfully"() {
        TicketEntity ticket = new TicketEntity() {
            @Override
            Double getPrice() {
                return 100.0
            }
        }

        BasketEntity basket = new BasketEntity() {
            double totalPrice = 0.0
            List<BasketItemEntity> basketItems = []

            @Override
            double getTotalPrice() { return totalPrice }

            @Override
            void setTotalPrice(double price) { totalPrice = price }

            @Override
            List<BasketItemEntity> getBasketItems() { return basketItems }

            @Override
            void setBasketItems(List<BasketItemEntity> items) { basketItems = items }
        }

        UserEntity user = new UserEntity() {
            @Override
            BasketEntity getBasket() { return basket }
        }

        BasketItemRequest request = new BasketItemRequest(userId: 1L, ticketNumber: "T123")

        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(ticketRepository.findByNumber("T123")).thenReturn(ticket)

        basketService.addItem(request)

        verify(basketRepository).save(basket)
    }


    @Test
    void "addItem should throw NotFoundException when user not found"() {
        def request = new BasketItemRequest(userId: 1L, ticketNumber: "T123")
        when(userRepository.findById(1L)).thenReturn(Optional.empty())

        def exception = assertThrows(NotFoundException) {
            basketService.addItem(request)
        }

        assertEquals("User not found", exception.message)
    }

    @Test
    void "removeItem should remove item from basket successfully"() {
        def basket = new BasketEntity() {
            double totalPrice = 100.0
            List<BasketItemEntity> basketItems = []

            @Override
            double getTotalPrice() { return totalPrice }

            @Override
            void setTotalPrice(double price) { totalPrice = price }

            @Override
            List<BasketItemEntity> getBasketItems() { return basketItems }

            @Override
            void setBasketItems(List<BasketItemEntity> items) { basketItems = items }
        }

        def ticket = new TicketEntity() {
            @Override
            Double getPrice() { return 50.0 }
        }

        def basketItem = mock(BasketItemEntity)
        when(basketItem.getTickets()).thenReturn(ticket)

        basket.getBasketItems() << basketItem

        def user = new UserEntity() {
            @Override
            BasketEntity getBasket() { return basket }
        }

        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(basketItemRepository.findById(10L)).thenReturn(Optional.of(basketItem))

        basketService.removeItem(1L, 10L)

        verify(basketItemRepository).delete(basketItem)
        verify(basketRepository).save(basket)
    }

    @Test
    void "removeItem should throw NotFoundException when basketItem not found"() {
        def basket = mock(BasketEntity)
        def user = mock(UserEntity)

        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(user.getBasket()).thenReturn(basket)
        when(basketItemRepository.findById(10L)).thenReturn(Optional.empty())

        def exception = assertThrows(NotFoundException) {
            basketService.removeItem(1L, 10L)
        }

        assertEquals("BasketItem not found", exception.message)
    }


    @Test
    void "getBasket should return BasketResponse successfully"() {
        def basket = mock(BasketEntity)
        def user = mock(UserEntity)
        def basketItem = mock(BasketItemEntity)
        def ticket = mock(TicketEntity)
        def productEvent = mock(ProductEventEntity)
        def responseItem = mock(com.example.iticket.model.response.BasketItemResponse) // tam path
        def response = mock(BasketResponse)
        def productEventResponse = mock(ProductEventResponse)

        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(user.getBasket()).thenReturn(basket)
        when(basket.getId()).thenReturn(100L)
        when(basketRepository.findById(100L)).thenReturn(Optional.of(basket))

        when(mapper.toResponse(basket)).thenReturn(response)
        when(response.getBasketItems()).thenReturn([responseItem])

        when(basket.getBasketItems()).thenReturn([basketItem])
        when(basketItem.getTickets()).thenReturn(ticket)
        when(ticket.getProductEvent()).thenReturn(productEvent)
        when(productEventMapper.toResponse(productEvent)).thenReturn(productEventResponse)

        basketService.getBasket(1L)

        verify(mapper).toResponse(basket)
        verify(productEventMapper).toResponse(productEvent)
    }

    @Test
    void "buy should succeed and clear basket"() {
        def venue = new VenuesEntity() {
            @Override
            String getName() { return "Olympic Stadium" }
        }

        def hall = new HallEntity() {
            VenuesEntity venueEntity = venue
            @Override
            VenuesEntity getVenue() { return venueEntity }
            @Override
            String getName() { return "Main Hall" }
        }

        def sector = new SectorEntity() {
            HallEntity hallEntity = hall
            @Override
            HallEntity getHall() { return hallEntity }
        }

        def productEvent = new ProductEventEntity() {
            @Override
            String getEventName() { return "Concert" }
        }

        def ticket = new TicketEntity() {
            boolean booked = false
            ProductEventEntity productEventEntity = productEvent
            SectorEntity sectorEntity = sector
            @Override
            Double getPrice() { return 100.0 }
            @Override
            void setBooked(boolean b) { booked = b }
            @Override
            boolean isBooked() { return booked }
            @Override
            ProductEventEntity getProductEvent() { return productEventEntity }
            @Override
            SectorEntity getSector() { return sectorEntity }
        }

        def basketItem = new BasketItemEntity() {
            TicketEntity tickets = ticket
            @Override
            TicketEntity getTickets() { return tickets }
        }

        def basket = new BasketEntity() {
            List<BasketItemEntity> basketItems = [basketItem]
            double totalPrice = 0.0
            @Override
            List<BasketItemEntity> getBasketItems() { return basketItems }
            @Override
            void setBasketItems(List<BasketItemEntity> items) { basketItems = items }
            @Override
            double getTotalPrice() { return totalPrice }
            @Override
            void setTotalPrice(double price) { totalPrice = price }
        }

        def user = new UserEntity() {
            BasketEntity basketEntity = basket
            @Override
            BasketEntity getBasket() { return basketEntity }
        }

        def cardRequest = new CardRequest()
        def paymentResult = mock(IyzicoPaymentResult)

        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket))
        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(iyzicoPaymentService.payForPlan(1L, 1L, cardRequest)).thenReturn(paymentResult)
        when(paymentResult.isSuccess()).thenReturn(true)

        basketService.buy(1L, 1L, cardRequest)

        assert ticket.isBooked() == true
        verify(basketRepository).save(basket)
    }



    @Test
    void "buy should throw IllegalStateException when basket empty"() {
        def basket = mock(BasketEntity)
        def user = mock(UserEntity)
        def cardRequest = new CardRequest()

        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket))
        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(basket.getBasketItems()).thenReturn([])

        def exception = assertThrows(IllegalStateException) {
            basketService.buy(1L, 1L, cardRequest)
        }

        assertEquals("Sepetde bilet yoxdur.", exception.message)
    }

    @Test
    void "buy should throw IllegalStateException when payment fails"() {
        def venue = new VenuesEntity() {
            @Override String getName() { return "Olympic Stadium" }
        }
        def hall = new HallEntity() {
            VenuesEntity venueEntity = venue
            @Override VenuesEntity getVenue() { return venueEntity }
            @Override String getName() { return "Main Hall" }
        }
        def sector = new SectorEntity() {
            HallEntity hallEntity = hall
            @Override HallEntity getHall() { return hallEntity }
        }

        def productEvent = new ProductEventEntity() {
            @Override String getEventName() { return "Concert" }
        }

        def ticket = new TicketEntity() {
            boolean booked = false
            ProductEventEntity productEventEntity = productEvent
            SectorEntity sectorEntity = sector
            @Override Double getPrice() { return 100.0 }
            @Override void setBooked(boolean b) { booked = b }
            @Override boolean isBooked() { return booked }
            @Override ProductEventEntity getProductEvent() { return productEventEntity }
            @Override SectorEntity getSector() { return sectorEntity }
        }

        def basketItem = new BasketItemEntity() {
            TicketEntity tickets = ticket
            @Override TicketEntity getTickets() { return tickets }
        }
        def basket = new BasketEntity() {
            List<BasketItemEntity> basketItems = [basketItem]
            @Override List<BasketItemEntity> getBasketItems() { return basketItems }
        }
        def user = new UserEntity() {
            BasketEntity basketEntity = basket
            @Override BasketEntity getBasket() { return basketEntity }
        }

        def cardRequest = new CardRequest()
        def paymentResult = mock(IyzicoPaymentResult)

        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket))
        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(iyzicoPaymentService.payForPlan(1L, 1L, cardRequest)).thenReturn(paymentResult)
        when(paymentResult.isSuccess()).thenReturn(false)
        when(paymentResult.getErrorMessage()).thenReturn("Kart redd edildi")

        def exception = assertThrows(IllegalStateException) {
            basketService.buy(1L, 1L, cardRequest)
        }

        assertEquals("Odeme basarisiz oldu: Kart redd edildi", exception.message)
    }

}

