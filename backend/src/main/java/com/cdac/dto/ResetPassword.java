package com.cdac.dto;

import lombok.Data;

@Data
public class ResetPassword {
	 private String token;
	 private String newPassword;
	
}
