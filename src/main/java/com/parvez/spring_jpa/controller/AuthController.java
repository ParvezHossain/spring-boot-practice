package com.parvez.spring_jpa.controller;

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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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
        // Remove old refresh token for same device
        refreshTokenRepository.deleteByUsernameAndDeviceId(
                dto.username(), deviceId
        );

        String accessToken = authService.login(dto);
        String refreshToken = jwtUtil.generateRefreshToken(dto.username());

        authService.saveRefreshToken(dto.username(), refreshToken, deviceId);

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken));
    }

    @Transactional
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("Device-Id") String deviceId
    ) {

        String token = authHeader.substring(7);

        authService.validateRefreshToken(token);

        RefreshToken storedToken =
                refreshTokenRepository
                        .findByTokenAndDeviceId(token, deviceId)
                        .orElse(null);


        if (storedToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.extractUsername(token);
        Employee employee = employeeService.findByUsername(username);

        String newAccessToken = jwtUtil.generateToken(username, employee.getRole().toString());
        return ResponseEntity.ok(new TokenResponseDTO(newAccessToken, token));
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("Device-Id") String deviceId
    ) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        refreshTokenRepository.deleteByUsernameAndDeviceId(
                username, deviceId
        );
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAllDevices(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        refreshTokenRepository.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }
}
