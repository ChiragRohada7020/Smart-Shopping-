package com.cdac.controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import com.cdac.dto.ForgotPassword;
import com.cdac.dto.JwtResponse;
import com.cdac.dto.LoginRequest;
import com.cdac.dto.RegisterRequest;
import com.cdac.dto.ResetPassword;
import com.cdac.dto.VerifyRequest;
=======
import com.cdac.dto.*;
>>>>>>> 54ed7724b2288051fe5785d4ac49643da7802259
import com.cdac.entity.User;
import com.cdac.repository.UserRepository;
import com.cdac.security.JwtUtil;
import com.cdac.service.EmailService;
import com.cdac.temp.TempUser;

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

   
    private Map<String, TempUser> tempUsers = new HashMap<>();

    @GetMapping("/test")
    public String testing() {
        return "Hello Chirag";
    }

   
    // Register: Store in memory + send OTP
   
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }

        if (tempUsers.containsKey(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already pending verification.");
        }

        String otp = generateOTP();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        TempUser tempUser = new TempUser(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            otp,
            expiry
        );

        tempUsers.put(request.getEmail(), tempUser);
        emailService.sendVerificationEmail(request.getEmail(), otp);

        return ResponseEntity.ok("OTP sent to your email. Please verify to complete registration.");
    }

   
<<<<<<< HEAD
    

   
=======
    // Verify: Save user to DB only if OTP valid
    
>>>>>>> 54ed7724b2288051fe5785d4ac49643da7802259
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyRequest request) {
        TempUser tempUser = tempUsers.get(request.getEmail());

        if (tempUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No registration found for this email.");
        }

        if (!tempUser.getOtp().equals(request.getOtp_code())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP.");
        }

        if (tempUser.getOtpExpiry().isBefore(LocalDateTime.now())) {
            tempUsers.remove(request.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP expired. Please register again.");
        }

        // ✅ Save to DB after verification
        User user = new User();
        user.setUsername(tempUser.getUsername());
        user.setEmail(tempUser.getEmail());
        user.setPassword(tempUser.getEncodedPassword());
      //  user.setVerified(true);
      //  user.setVerificationCode(null);
        user.setCodeExpiryTime(null);

        userRepo.save(user);
        tempUsers.remove(request.getEmail());

        return ResponseEntity.ok("Email verified and user registered successfully.");
    }

   
    // Login (only if verified)
   
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepo.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOpt.get();



        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(new JwtResponse(token, request.getEmail()));
    }

<<<<<<< HEAD
    
    /**
     * Step 1: User requests a password reset.
     * We generate a token and email it to them.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPassword request) {
        // Find user by email
        Optional<User> userOptional = userRepo.findByEmail(request.getEmail());

        // Security Note: Always return a generic success message.
        // This prevents an attacker from learning which emails are registered.
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Generate a unique token
            String token = generateOTP(); // Reusing your OTP logic is fine for this
            user.setPasswordResetToken(token);
            user.setTokenExpiryTime(LocalDateTime.now().plusMinutes(10)); // 10 minute validity
            userRepo.save(user);

            // Send email with the token
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        }

        return ResponseEntity.ok("If an account with this email exists, a password reset token has been sent.");
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword request) {
        // Find the user by the provided token
        Optional<User> userOptional = userRepo.findByPasswordResetToken(request.getToken());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
        }

        User user = userOptional.get();

        // Check if the token has expired
        if (user.getTokenExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
        }

        // Token is valid, update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Invalidate the token after use so it cannot be used again
        user.setPasswordResetToken(null);
        user.setTokenExpiryTime(null);
        userRepo.save(user);

        return ResponseEntity.ok("Password has been reset successfully. You can now login.");
    }
   
=======
    // ----------------------------
    // Generate 6-digit OTP
    // ----------------------------
>>>>>>> 54ed7724b2288051fe5785d4ac49643da7802259
    private String generateOTP() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
