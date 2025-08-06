package com.cdac.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    //Optional<User> findByEmailAndVerificationCode(String email, String code);
}

    
