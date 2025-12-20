package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.BasketItemEntity;
import com.example.iticket.dao.repository.BasketItemRepository;
import com.example.iticket.dao.repository.BasketRepository;
import com.example.iticket.dao.repository.TicketRepository;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.mapper.BasketItemMapper;
import com.example.iticket.mapper.BasketMapper;
import com.example.iticket.model.request.BasketItemRequest;
import com.example.iticket.model.response.BasketResponse;
import com.example.iticket.service.concret.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Override
    public void addItem(Long userId, BasketItemRequest request) {
        log.info("BasketService.addItem.start userId: {}", userId);
        var basket = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getBasket();
        BasketItemEntity basketItem = basketItemMapper.toEntity(request);
        if(basketItem.getTicketNumber() != null){
            basketItem.setQuantity(1);
        basket.getBasketItems().add(basketItem);
        }
        else {
//            ticketRepository.findAll().forEach();
            basket.getBasketItems().add(basketItem);
        }
    }

    @Override
    public void removeItem(Long userId, Long basketItemId) {
        log.info("BasketService.removeItem.start userId: {}", userId);

        var basket = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"))
                .getBasket();

        var basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new NotFoundException("BasketItem not found"));

        basket.getBasketItems().remove(basketItem);

        basketRepository.save(basket);

        log.info("BasketService.removeItem.end userId: {}", userId);
    }


    @Override
    public BasketResponse getBasket(Long userId) {
        log.info("BasketSerice.getById.start userId: {}", userId);
        var basketId = userRepository.findById(userId).get().getWishlist().getId();
        var basketEntity = basketRepository.findById(basketId).get();
        var response = mapper.toResponse(basketEntity);
        log.info("BasketSerice.getById.end userId: {}", userId);
        return response;
    }
}
