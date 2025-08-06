package com.cdac.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private String id; // product ID (Amazon/Flipkart)

    private String title;
    private String source; // Amazon / Flipkart
    private double cost;
    private String imageUrl;
    private String productUrl;
}
