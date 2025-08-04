package com.cdac.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
<<<<<<< HEAD
    Optional<User> findByEmailAndVerificationCode(String email , String code);
    
    Optional<User> findByPasswordResetToken(String token) ;
    
=======
    //Optional<User> findByEmailAndVerificationCode(String email, String code);
>>>>>>> 54ed7724b2288051fe5785d4ac49643da7802259
}

    
