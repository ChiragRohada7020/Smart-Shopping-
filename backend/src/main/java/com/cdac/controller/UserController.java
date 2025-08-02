package com.cdac.controller;

import com.cdac.entity.User;
import com.cdac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

@GetMapping("/me")
public UserProfileDto getLoggedInUserDetails() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return new UserProfileDto(user.getName(), user.getEmail());
}
    
}
