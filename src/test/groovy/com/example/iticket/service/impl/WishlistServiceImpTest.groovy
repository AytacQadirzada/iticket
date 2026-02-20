package com.example.iticket.service.impl;

import com.example.iticket.dao.entity.ProductEntity;
import com.example.iticket.dao.entity.UserEntity;
import com.example.iticket.dao.entity.WishlistEntity;
import com.example.iticket.dao.repository.ProductRepository;
import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.dao.repository.WishlistRepository;
import com.example.iticket.mapper.WishlistMapper;
import com.example.iticket.model.response.WishlistResponse
import com.example.iticket.service.concret.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceImpTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishlistMapper mapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private WishlistServiceImp wishlistService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnWishlistResponse() {
        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setId(1L);
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWishlist(wishlist);
        WishlistResponse response = new WishlistResponse();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(wishlist));
        when(mapper.toResponse(wishlist)).thenReturn(response);

        WishlistResponse result = wishlistService.getById(1L);

        assertEquals(response, result);
    }

    @Test
    void addProduct_shouldAddProductIfNotExists() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);

        WishlistEntity wishlist = new WishlistEntity();
        wishlist.setProducts(new ArrayList<>()); // <- List istifadə edilir

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setWishlist(wishlist);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        wishlistService.addProduct(1L, 1L);

        assertTrue(wishlist.getProducts().contains(product));
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void addProduct_shouldRemoveProductIfAlreadyExists() {
        ProductEntity product = new ProductEntity()
        product.setId(1L)

        WishlistEntity wishlist = new WishlistEntity()
        wishlist.setProducts(new ArrayList<>(List.of(product))) // <- List tipinə uyğun

        UserEntity user = new UserEntity()
        user.setId(1L)
        user.setWishlist(wishlist)

        when(userRepository.findById(1L)).thenReturn(Optional.of(user))
        when(productRepository.findById(1L)).thenReturn(Optional.of(product))

        wishlistService.addProduct(1L, 1L)

        assertFalse(wishlist.getProducts().contains(product))
        verify(wishlistRepository).save(wishlist)
    }

    @Test
    void getById_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> wishlistService.getById(1L));
    }

    @Test
    void addProduct_shouldThrowExceptionIfUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> wishlistService.addProduct(1L, 1L));
    }

    @Test
    void addProduct_shouldThrowExceptionIfProductNotFound() {
        WishlistEntity wishlist = new WishlistEntity();
        UserEntity user = new UserEntity();
        user.setWishlist(wishlist);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> wishlistService.addProduct(1L, 1L));
    }
}