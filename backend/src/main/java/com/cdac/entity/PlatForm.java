package com.cdac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PlatForm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long platformId ;
	
	String platformName ;
	
	String baseUrl ;
	

}
