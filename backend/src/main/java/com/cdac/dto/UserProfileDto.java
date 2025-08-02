package com.cdac.dto;

public class UserProfileDto {
    private String name;
    private String email;

    public UserProfileDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters (and optionally setters)
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
