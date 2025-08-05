package com.cdac.entity;

import lombok.*;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Product implements Serializable {

    private String id; // product ID (Amazon/Flipkart)

    private String title;
    private String source; // Amazon / Flipkart
    private double cost;
    private String imageUrl;
    private String productUrl;
}
