package com.cdac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.dto.AddProductToCartRequest;
import com.cdac.service.CartService;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*") // allow cross-origin requests if frontend is separate
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addProductToCart(@RequestBody AddProductToCartRequest request) {
        cartService.addProductToCart(request);
        return "Product added to cart successfully";
    }
}
