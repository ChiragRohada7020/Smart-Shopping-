package com.cdac.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cdac.dto.*;
import com.cdac.dto.*;
import com.cdac.entity.User;
import com.cdac.repository.UserRepository;
import com.cdac.security.JwtUtil;
import com.cdac.service.EmailService;
import com.cdac.temp.TempUser;

import jakarta.servlet.http.HttpSession;

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

		// if (tempUsers.containsKey(request.getEmail())) {
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already
		// pending verification.");
		// }

		String otp = generateOTP();
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

		TempUser tempUser = new TempUser(request.getUsername(), request.getEmail(),
				passwordEncoder.encode(request.getPassword()), otp, expiry);

		tempUsers.put(request.getEmail(), tempUser);
		emailService.sendVerificationEmail(request.getEmail(), otp);

		return ResponseEntity.ok("OTP sent to your email. Please verify to complete registration.");
	}

	// Verify: Save user to DB only if OTP valid

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

	    try {
	        // Try to authenticate user with email and password
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
	    } catch (BadCredentialsException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
	    }

	    // If authentication successful
	    String token = jwtUtil.generateToken(request.getEmail());
	    return ResponseEntity.ok(new JwtResponse(token, request.getEmail()));
	}
	
	
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPassword request, HttpSession session) {
		// Find user by email
		Optional<User> userOptional = userRepo.findByEmail(request.getEmail());

		// Security Note: Always return a generic success message.
		// This prevents an attacker from learning which emails are registered.
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			String otp = generateOTP(); // Reusing your OTP logic is fine for this
			session.setAttribute("email", request.getEmail());
			session.setAttribute("otp", otp);
			session.setAttribute("expireTime", LocalDateTime.now().plusMinutes(10));

			// Send email with the token
			emailService.sendPasswordResetEmail(user.getEmail(), otp);
		}

		return ResponseEntity.ok("If an account with this email exists, a password reset token has been sent.");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPassword request, HttpSession session) {
		// Find the user by the provided token
		Optional<User> userOptional = userRepo.findByEmail((String) session.getAttribute("email"));
		User user = userOptional.get();

		// check if otp is valid or not
		if (!request.getOtp().equals((String) session.getAttribute("otp"))) {
			
			
			// Invalidate the token after use so it cannot be used again
			session.removeAttribute("email");
			session.removeAttribute("otp");
			session.removeAttribute("expireTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
		}

		// Check if the token has expired
		if (((LocalDateTime) session.getAttribute("expireTime")).isBefore(LocalDateTime.now())) {			
			// Invalidate the token after use so it cannot be used again
			session.removeAttribute("email");
			session.removeAttribute("otp");
			session.removeAttribute("expireTime");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
		}

		// otp is valid, update the password
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepo.save(user);

		// Invalidate the token after use so it cannot be used again

		session.removeAttribute("email");
		session.removeAttribute("otp");
		session.removeAttribute("expireTime");

		return ResponseEntity.ok("Password has been reset successfully. You can now login.");
	}

	// ----------------------------
	// Generate 6-digit OTP
	// ----------------------------
	private String generateOTP() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}
}
