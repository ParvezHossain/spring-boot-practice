package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.config.ApiPaths;
import com.parvez.spring_jpa.dto.ForgotPasswordRequestDTO;
import com.parvez.spring_jpa.dto.ResetPasswordRequestDTO;
import com.parvez.spring_jpa.model.PasswordResetToken;
import com.parvez.spring_jpa.service.AuthService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.AUTH)
@RequiredArgsConstructor
public class PasswordController {
    private final AuthService authService;

    @Transactional
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotpassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {
        authService.forgotPassword(requestDTO.email());
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetpassword(@Valid @RequestBody ResetPasswordRequestDTO requestDTO) {
        authService.resetPassword(requestDTO.resetToken(), requestDTO.newPassword());
        return ResponseEntity.ok().build();
    }
}
