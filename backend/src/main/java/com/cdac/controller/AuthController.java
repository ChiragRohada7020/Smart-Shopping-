package com.cdac.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cdac.dto.JwtResponse;
import com.cdac.dto.LoginRequest;
import com.cdac.dto.RegisterRequest;
import com.cdac.dto.VerifyRequest;
import com.cdac.entity.User;
import com.cdac.repository.UserRepository;
import com.cdac.security.JwtUtil;
import com.cdac.service.EmailService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;
    
    @GetMapping("/test")
    public String testing() {
    	return "Hello Chirag";
    }

   
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerified(false); // default
        String otp = generateOTP();
        user.setVerificationCode(otp);
        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(10)); // 10 mins expiry

        userRepo.save(user);
        emailService.sendVerificationEmail(user.getEmail(), otp);

        return ResponseEntity.ok("Registered successfully. Please check your email for the OTP.");
    }

   
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyRequest request) {
        Optional<User> userOpt = userRepo.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();

        if (user.isVerified()) {
            return ResponseEntity.ok("Email already verified.");
        }

        if (!request.getCode().equals(user.getVerificationCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP code.");
        }

        if (user.getCodeExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired.");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiryTime(null);
        userRepo.save(user);

        return ResponseEntity.ok("Email verified successfully. You can now login.");
    }

    // -------------------------
    // Login
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepo.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();
        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not verified. Please check your email.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = jwtUtil.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token, request.getEmail()));
    }

   
    private String generateOTP() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit number
    }
}
