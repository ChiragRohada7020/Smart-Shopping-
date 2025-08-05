package com.cdac.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product implements Serializable {

    @Id
    private Long id; // Add logic to generate unique id (optional for Redis)

    private String title;
    private String source;
    private double cost;
    private String link;
}
