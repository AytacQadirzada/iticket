package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.*;
import com.example.iticket.dao.repository.BasketItemRepository;
import com.example.iticket.dao.repository.BasketRepository;
import com.example.iticket.dao.repository.TicketRepository;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.BasketItemMapper;
import com.example.iticket.mapper.BasketMapper;
import com.example.iticket.mapper.ProductEventMapper;
import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.BasketResponse;
import com.example.iticket.model.response.IyzicoPaymentResult;
import com.example.iticket.model.response.ProductEventResponse;
import com.example.iticket.model.response.TicketMailResponse;
import com.example.iticket.service.concret.BasketService;
import com.example.iticket.service.concret.IyzicoPaymentService;
import com.example.iticket.service.concret.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketServiceImpl implements BasketService {
    private final UserRepository userRepository;
    private final BasketRepository basketRepository;
    private final BasketMapper mapper;
    private final BasketItemMapper basketItemMapper;
    private final BasketItemRepository basketItemRepository;
    private final TicketRepository ticketRepository;
    private final IyzicoPaymentService iyzicoPaymentService;
    private final MailService mailService;
    private final ProductEventMapper productEventMapper;

    @Override
    public void addItem(BasketItemRequest request) {
        log.info("BasketService.addItem.start userId: {}", request.getUserId());
        BasketEntity basket = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getBasket();
        List<BasketItemEntity> basketItems = new ArrayList<>();
        if(basket.getBasketItems() == null){
            basket.setBasketItems(basketItems);
        }
        else{
        basketItems = basket.getBasketItems();
        }
        if (request.getTicketNumber() != null) {
            BasketItemEntity basketItem = new BasketItemEntity();
            request.setQuantity(1);
            TicketEntity ticket = ticketRepository.findByNumber(request.getTicketNumber());
            basketItem.setTickets(ticket);
            basketItem.setPrice(ticket.getPrice());
            basketItem.setQuantity(1);
            basket.setTotalPrice(basket.getTotalPrice() + basketItem.getTickets().getPrice());
            basketItem.setBasket(basket);
            basketItems.add(basketItem);

        } else {
            if (request.getQuantity() != null) {
                List<TicketEntity> tickets = ticketRepository.findByProductEventIdAndSectorIdAndIsBooked(request.getProductEventId(), request.getSectorId(),false);
                int quantity = request.getQuantity();

                if (tickets.size() >= quantity) {
                    for (int i = 0; i < quantity; i++) {
                        TicketEntity ticketItem = tickets.get(i);

                        BasketItemEntity basketItem = new BasketItemEntity();
                        basketItem.setTickets(ticketItem);
                        basketItem.setPrice(ticketItem.getPrice());
                        basket.setTotalPrice(basket.getTotalPrice() + ticketItem.getPrice());
                        basketItem.setBasket(basket);
                        basketItems.add(basketItem);
                    }
                } else {
                    throw new NotFoundException("Daxil olunan sayda bilet yoxdur!");
                }
            }
        }
        basket.setBasketItems(basketItems);
        basketRepository.save(basket);
        log.info("BasketService.addItem.end userId: {}", request.getUserId());
    }

    @Override
    public void removeItem(Long userId, Long basketItemId) {
        log.info("BasketService.removeItem.start userId: {}", userId);

        BasketEntity basket = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getBasket();

        var basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new NotFoundException("BasketItem not found"));

        basket.setTotalPrice(basket.getTotalPrice() - basketItem.getTickets().getPrice());
        basket.getBasketItems().remove(basketItem);

        basketItemRepository.delete(basketItem);
        basketRepository.save(basket);

        log.info("BasketService.removeItem.end userId: {}", userId);
    }


    @Override
    public BasketResponse getBasket(Long userId) {
        log.info("BasketSerice.getById.start userId: {}", userId);
        var basketId = userRepository.findById(userId).get().getBasket().getId();
        BasketEntity basketEntity = basketRepository.findById(basketId).get();

        var response = mapper.toResponse(basketEntity);
        for(var item : response.getBasketItems()){
            for(var ticket : basketEntity.getBasketItems()){
                ProductEventResponse productEventResponse = productEventMapper.toResponse(ticket.getTickets().getProductEvent());
                item.setProduct(productEventResponse);
            }
        }
        log.info("BasketSerice.getById.end userId: {}", userId);
        return response;
    }

    @Override
    @Transactional
    public void buy(Long basketId, Long userId, CardRequest request) {
        log.info("BasketService.buy.start userId: {}", userId);

        BasketEntity basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new NotFoundException("Sepet tapilmadi."));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Istifadeci tapilmadi."));

        if (basket.getBasketItems() == null || basket.getBasketItems().isEmpty()) {
            throw new IllegalStateException("Sepetde bilet yoxdur.");
        }

        IyzicoPaymentResult paymentResult =
                iyzicoPaymentService.payForPlan(userId, basketId, request);

        if (!paymentResult.isSuccess()) {
            throw new IllegalStateException(
                    "Odeme basarisiz oldu: " + paymentResult.getErrorMessage()
            );
        }

        for (BasketItemEntity item : basket.getBasketItems()) {

            TicketEntity ticket = item.getTickets();
            ticket.setBooked(true);
            TransactionEntity transaction = new TransactionEntity();
            transaction.setAmount(BigDecimal.valueOf(item.getPrice()));
            transaction.setPaymentId(paymentResult.getPaymentId());
            transaction.setUser(user);
            transaction.setTickets(List.of(ticket));
            ticket.setTransaction(transaction);

            ticketRepository.save(ticket);

            TicketMailResponse mail = new TicketMailResponse();
            mail.setEventName(ticket.getProductEvent().getEventName());
            mail.setTicketNumber(ticket.getNumber());
            mail.setRowNumber(String.valueOf(ticket.getRowNumber()));
            mail.setSeatNumber(String.valueOf(ticket.getColumnNumber()));
            mail.setPrice(ticket.getPrice());
            mail.setVenue(ticket.getSector().getHall().getVenue().getName());
            mail.setHall(ticket.getSector().getHall().getName());
            mail.setSector(ticket.getSector().getName());
            mail.setStartDate(ticket.getProductEvent().getEventDate());

            mailService.sendTicketEmail(user.getEmail(), mail);
        }

        basket.getBasketItems().clear();
        basket.setTotalPrice(0);

        basketRepository.save(basket);
        log.info("BasketService.buy.end userId: {}", userId);
    }

}
