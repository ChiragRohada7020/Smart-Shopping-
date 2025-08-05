package com.cdac.entity;

<<<<<<< HEAD
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

=======
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
>>>>>>> f8c3d8756f3048b9c7d39613f6c799d9d0a20172
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@Entity
public class Product implements Serializable {

    @Id
    private Long id; // Add logic to generate unique id (optional for Redis)

    private String title;
    private String source;
    private double cost;
    private String link;
=======
public class Product {
	
	@Id
	private Long Id;

    private String title;     // Product title
    private String source;    // amazon or flipkart
    private double cost;      // price
    private String link;      // product link
>>>>>>> f8c3d8756f3048b9c7d39613f6c799d9d0a20172
}
