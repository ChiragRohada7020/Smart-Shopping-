package com.cdac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Email Verification");
        message.setText("Welcome On Sanskari Platform");
        message.setText("Your OTP code is: " + code);
        mailSender.send(message);
    }
    
    // --- NEW METHOD ---
    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request - Sanskari Platform");
        // In a real app, you'd send a link like: http://yourfrontend.com/reset-password?token=...
        message.setText("You have requested to reset your password.\n\n"
                      + "Use the following token to reset your password: " + token + "\n\n"
                      + "If you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}  
