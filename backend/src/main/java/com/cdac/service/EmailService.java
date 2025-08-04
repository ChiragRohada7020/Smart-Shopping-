package com.cdac.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Email Verification");

            
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif;'>" +
                    "<h2>Welcome to Sanskari Platform!</h2>" +
                    "<p>Thank you for registering.</p>" +
                    "<p>Your <strong>OTP code</strong> is: " +
                    "<span style='font-size:18px; color:blue;'>" + code + "</span></p>" +
                    "<p>This OTP is valid for 10 minutes.</p>" +
                    "<p>If you didn’t request this, please ignore the email.</p>" +
                    "<br><p>Regards,<br/>Sanskari Team</p>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true); 

            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}
