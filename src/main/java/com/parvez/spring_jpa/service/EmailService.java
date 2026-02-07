package com.parvez.spring_jpa.service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendEmail(String email, String resetLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset Password");

        mailMessage.setText("""
                Hello,
                
                You requested to reset your password.
                
                Click the link below to reset it:
                %s
                
                This link will expire in 15 minutes.
                
                If you did not request this, please ignore this email.
                """.formatted(resetLink));
        mailSender.send(mailMessage);
    }
}
