package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.BasketEntity;
import com.example.iticket.dao.entity.TransactionEntity;
import com.example.iticket.dao.entity.UserEntity;
import com.example.iticket.dao.repository.BasketRepository;
import com.example.iticket.dao.repository.TransactionRepository;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.model.request.CardRequest;
import com.example.iticket.model.response.IyzicoPaymentResult;
import com.example.iticket.service.concret.IyzicoPaymentService;
import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCancelRequest;
import com.iyzipay.request.CreatePaymentRequest;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class IyzicoPaymentServiceImpl implements IyzicoPaymentService {

    private final BasketRepository basketRepository;
    private final UserRepository userRepository;

    private final Options options = new Options();
    private final TransactionRepository transactionRepository;

    public IyzicoPaymentServiceImpl(
            BasketRepository basketRepository,
            UserRepository userRepository,
            @Value("${iyzico.api-key}") String apiKey,
            @Value("${iyzico.secret-key}") String secretKey,
            @Value("${iyzico.base-url:https://sandbox-api.iyzipay.com}") String baseUrl,
            TransactionRepository transactionRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;

        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public IyzicoPaymentResult payForPlan(Long userId, Long basketId, CardRequest request) {

        BasketEntity basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Double totalPrice = basket.getTotalPrice();

        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setLocale(Locale.forLanguageTag("tr").getLanguage());
        paymentRequest.setConversationId(UUID.randomUUID().toString());
        paymentRequest.setPrice(BigDecimal.valueOf(totalPrice));
        paymentRequest.setPaidPrice(BigDecimal.valueOf(totalPrice));
        paymentRequest.setInstallment(1);
        paymentRequest.setBasketId(basketId.toString());
        paymentRequest.setPaymentChannel(PaymentChannel.WEB.name());
        paymentRequest.setPaymentGroup(PaymentGroup.PRODUCT.name());

        // CARD
        PaymentCard card = new PaymentCard();
        card.setCardHolderName(request.getCardHolderName());
        card.setCardNumber(request.getCardNumber());
        card.setExpireMonth(request.getExpireMonth());
        card.setExpireYear(request.getExpireYear());
        card.setCvc(request.getCvv());
        card.setRegisterCard(0);

        paymentRequest.setPaymentCard(card);

        // BUYER
        Buyer buyer = new Buyer();
        buyer.setId(String.valueOf(userId));
        buyer.setName(request.getCardHolderName());
        buyer.setSurname("-");
        buyer.setGsmNumber("+905555555555");
        buyer.setEmail(user.getEmail());
        buyer.setIdentityNumber("11111111110");
        buyer.setRegistrationAddress("Default address");
        buyer.setIp("127.0.0.1");
        buyer.setCity("Istanbul");
        buyer.setCountry("Türkiye");
        buyer.setZipCode("34000");

        paymentRequest.setBuyer(buyer);

        // ADDRESS
        Address address = new Address();
        address.setContactName(request.getCardHolderName());
        address.setCity(buyer.getCity());
        address.setCountry(buyer.getCountry());
        address.setAddress(buyer.getRegistrationAddress());
        address.setZipCode(buyer.getZipCode());
        paymentRequest.setBillingAddress(address);
        paymentRequest.setShippingAddress(address);

        // BASKET ITEM
        String names = basket.getBasketItems()
                .stream()
                .map(x -> x.getTickets().getProductEvent().getEventName())
                .collect(Collectors.joining("_"));

        BasketItem basketItem = new BasketItem();
        basketItem.setId(UUID.randomUUID().toString());
        basketItem.setName(names);
        basketItem.setCategory1("Subscription");
        basketItem.setItemType(BasketItemType.VIRTUAL.name());
        basketItem.setPrice(BigDecimal.valueOf(totalPrice));

        paymentRequest.setBasketItems(Collections.singletonList(basketItem));

        Payment payment = Payment.create(paymentRequest, options);

        IyzicoPaymentResult result = new IyzicoPaymentResult();
        result.setSuccess("success".equalsIgnoreCase(payment.getStatus()));
        result.setPaymentId(payment.getPaymentId());
        result.setErrorCode(payment.getErrorCode());
        result.setErrorMessage(payment.getErrorMessage());

        return result;
    }

    @Override
    public void refund(String paymentId) {

        CreateCancelRequest request = new CreateCancelRequest();
        request.setLocale(Locale.forLanguageTag("tr").getLanguage());
        request.setConversationId(UUID.randomUUID().toString());
        request.setPaymentId(paymentId);
        request.setIp("127.0.0.1");

        Cancel cancel = Cancel.create(request, options);

        if (!"success".equalsIgnoreCase(cancel.getStatus())) {
            throw new RuntimeException(
                    "Iyzipay cancel failed: " +
                            cancel.getErrorCode() + " - " + cancel.getErrorMessage()
            );
        }
    }

    @Transactional
    public IyzicoPaymentResult addBalance(
            Long userId,
            double amount,
            CardRequest request) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (request == null) {
            throw new IllegalArgumentException("Card information is missing");
        }

        String priceStr = String.format(Locale.US, "%.2f", amount);
        BigDecimal newPrice = new BigDecimal(priceStr);

        CreatePaymentRequest createRequest = new CreatePaymentRequest();
        createRequest.setLocale("tr");
        createRequest.setConversationId(UUID.randomUUID().toString().replace("-", ""));
        createRequest.setPrice(newPrice);
        createRequest.setPaidPrice(newPrice);
        createRequest.setInstallment(1);
        createRequest.setBasketId("BALANCE_" + userId);
        createRequest.setPaymentChannel("WEB");
        createRequest.setPaymentGroup("PRODUCT");

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(request.getCardHolderName());
        paymentCard.setCardNumber(request.getCardNumber());
        paymentCard.setExpireMonth(request.getExpireMonth());
        paymentCard.setExpireYear(request.getExpireYear());
        paymentCard.setCvc(request.getCvv());
        paymentCard.setRegisterCard(0);

        createRequest.setPaymentCard(paymentCard);

        Buyer buyer = new Buyer();
        buyer.setId(String.valueOf(user.getId()));
        buyer.setName(user.getName() != null ? user.getName() : "User");
        buyer.setSurname(user.getSurname() != null ? user.getSurname() : "-");
        buyer.setEmail(user.getEmail());
        buyer.setIdentityNumber("11111111110");
        buyer.setRegistrationAddress("Balance Topup");
        buyer.setIp("127.0.0.1");
        buyer.setCity("Istanbul");
        buyer.setCountry("Turkey");
        buyer.setZipCode("34000");

        createRequest.setBuyer(buyer);

        Address address = new Address();
        address.setContactName(user.getName() + " " + user.getName());
        address.setCity("Istanbul");
        address.setCountry("Turkey");
        address.setAddress("Balance Topup");
        address.setZipCode("34000");

        createRequest.setShippingAddress(address);
        createRequest.setBillingAddress(address);

        // Basket
        BasketItem item = new BasketItem();
        item.setId("BALANCE");
        item.setName("Balance Top-Up");
        item.setCategory1("Wallet");
        item.setItemType("VIRTUAL");
        item.setPrice(newPrice);

        createRequest.setBasketItems(List.of(item));

        Payment payment = Payment.create(createRequest, options);

        IyzicoPaymentResult result = new IyzicoPaymentResult();
        result.setSuccess("success".equals(payment.getStatus()));
        result.setPaymentId(payment.getPaymentId());
        result.setErrorCode(payment.getErrorCode());
        result.setErrorMessage(payment.getErrorMessage());

        if (result.isSuccess()) {

            user.setBalance(user.getBalance().add(BigDecimal.valueOf(amount)));
            userRepository.save(user);

            TransactionEntity transaction = new TransactionEntity();
            transaction.setUser(user);
            transaction.setAmount(BigDecimal.valueOf(amount));
            transaction.setPaymentId(payment.getPaymentId());
            transaction.setCreatedAt(LocalDateTime.now());

            transactionRepository.save(transaction);
        }

        return result;
    }
}
