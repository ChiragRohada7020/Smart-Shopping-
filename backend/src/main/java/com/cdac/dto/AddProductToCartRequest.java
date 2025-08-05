package com.cdac.dto;

import com.cdac.entity.Product;

import lombok.Data;

@Data
public class AddProductToCartRequest {
	private Long userId;   // Who is adding
    private Product product;
}
