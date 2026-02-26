package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.config.ApiPaths;
import com.parvez.spring_jpa.dto.*;
import com.parvez.spring_jpa.model.Employee;
import com.parvez.spring_jpa.model.RefreshToken;
import com.parvez.spring_jpa.repository.RefreshTokenRepository;
import com.parvez.spring_jpa.security.JwtUtil;
import com.parvez.spring_jpa.service.AuthService;
import com.parvez.spring_jpa.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(ApiPaths.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<EmployeeResponseDTO> register(@RequestBody EmployeeRegisterDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(
            @RequestBody EmployeeLoginDTO dto,
            @RequestHeader("Device-Id") String deviceId
    ) {
        return ResponseEntity.ok(authService.login(dto, deviceId));

    }

    @Transactional
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("Device-Id") String deviceId
    ) {
        String token = authService.extractToken(authHeader);
        String accessToken = authService.accessToken(token, deviceId);
        return ResponseEntity.ok(new TokenResponseDTO(accessToken, token));
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("Device-Id") String deviceId
    ) {
        authService.logout(authHeader, deviceId);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAllDevices(
            @RequestHeader("Authorization") String authHeader
    ) {
        authService.logoutAllDevices(authHeader);
        return ResponseEntity.ok().build();
    }
}
