package com.cdac.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Lombok will auto-generate getters, setters, toString, equals, etc.
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String role = "ROLE_USER";



    @Column(name = "code_expiry_time")
    private LocalDateTime codeExpiryTime;

<<<<<<< HEAD
    
    @Column(name = "is_verified")
    private boolean isVerified = false;
=======

>>>>>>> 54ed7724b2288051fe5785d4ac49643da7802259

 // --- NEW FIELDS FOR PASSWORD RESET ---
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "token_expiry_time")
    private LocalDateTime tokenExpiryTime;
    // --- END OF NEW FIELDS ---

 
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SearchHistory> searchHistory;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> cart;


}
