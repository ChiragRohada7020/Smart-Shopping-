package com.cdac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data  
public class SearchRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Search query cannot be blank")
    private String query;
}
