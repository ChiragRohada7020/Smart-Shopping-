package com.cdac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	private Long Id;

    private String title;     // Product title
    private String source;    // amazon or flipkart
    private double cost;      // price
    private String link;      // product link
}
