package com.cdac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.dto.AddProductToCartRequest;
import com.cdac.entity.Cart;
import com.cdac.entity.Product;
import com.cdac.entity.User;
import com.cdac.repository.CartRepository;
import com.cdac.repository.UserRepository;


@Service
public class CartService {
	 @Autowired
	    private CartRepository cartRepository;

	    @Autowired
	    private UserRepository userRepository;

	    public void addProductToCart(AddProductToCartRequest request) {
	        User user = userRepository.findById(request.getUserId())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        Cart cart = cartRepository.findByUser(user).orElse(new Cart());
	        cart.setUser(user);

	        // Convert ProductDto to Product (Embeddable)
	        Product dto = request.getProduct();
	        Product product = new Product(dto.getId(), dto.getTitle(), dto.getSource(),
	                dto.getCost(), dto.getImageUrl(), dto.getProductUrl());

	        cart.addProduct(product);
	        cartRepository.save(cart);
	    }
}
