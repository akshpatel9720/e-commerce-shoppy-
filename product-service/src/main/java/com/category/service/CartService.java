package com.category.service;

import com.category.DTO.CartDTO;

import java.util.Map;

public interface CartService {
    Map<String, Object> save(CartDTO cartDTO);

    Map<String,Object> deleteProduct(CartDTO cartDTO);

    Map<String,Object> deleteAllProduct(Long userId);

//    Map<String,Object> deleteSelectedProduct(CartDTO cartDTO);
}