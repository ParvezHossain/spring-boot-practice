package com.parvez.spring_jpa.service;

import com.parvez.spring_jpa.amqp.EmailProducer;
import com.parvez.spring_jpa.dto.EmailEvent;
import com.parvez.spring_jpa.dto.EmployeeLoginDTO;
import com.parvez.spring_jpa.dto.EmployeeRegisterDTO;
import com.parvez.spring_jpa.dto.EmployeeResponseDTO;
import com.parvez.spring_jpa.exceptions.ResourceNotFoundException;
import com.parvez.spring_jpa.model.Employee;
import com.parvez.spring_jpa.model.PasswordResetToken;
import com.parvez.spring_jpa.model.RefreshToken;
import com.parvez.spring_jpa.model.Role;
import com.parvez.spring_jpa.repository.EmployeeRepository;
import com.parvez.spring_jpa.repository.PasswordResetTokenRepository;
import com.parvez.spring_jpa.repository.RefreshTokenRepository;
import com.parvez.spring_jpa.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final EmailProducer emailProducer;

    public EmployeeResponseDTO register(EmployeeRegisterDTO dto) {
        Employee emp = new Employee();
        emp.setUsername(dto.username());
        emp.setFirstName(dto.firstName());
        emp.setLastName(dto.lastName());
        emp.setEmail(dto.email());
        emp.setAge(dto.age());
        emp.setSalary(dto.salary());
        emp.setPassword(passwordEncoder.encode(dto.password()));
        emp.setRole(dto.role() != null ? dto.role() : Role.EMPLOYEE);

        employeeRepository.save(emp);

        return new EmployeeResponseDTO(
                emp.getId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getUsername(),
                emp.getEmail(),
                emp.getAge(),
                emp.getSalary()
        );
    }

    public String login(EmployeeLoginDTO dto) {

        try {
            Employee emp = employeeRepository
                    .findByUsername(dto.username());
            if (!passwordEncoder.matches(dto.password(), emp.getPassword())) {
                throw new ResourceNotFoundException("Invalid credentials");
            }
            return jwtUtil.generateToken(emp.getUsername(), emp.getRole().name());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public void saveRefreshToken(String username, String refreshToken, String deviceId) {
        RefreshToken token = new RefreshToken();
        token.setUsername(username);
        token.setToken(refreshToken);
        token.setDeviceId(deviceId);
        token.setExpiryDate(Instant.now().plus(1, ChronoUnit.DAYS));
        refreshTokenRepository.save(token);
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            RefreshToken token = refreshTokenRepository.findByToken(refreshToken);

            if (token.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(token);
                throw new RuntimeException("Refresh token expired");
            }
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Invalid refresh token");
        }
        return true;
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByUsername(refreshToken);
    }

    public void forgotPassword(String email) {
        Employee employee = (Employee) employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Delete old tokens
        passwordResetTokenRepository.deleteByUsername(employee.getUsername());
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUsername(employee.getUsername());
        passwordResetToken.setExpirationDate(Instant.now().plus(15, ChronoUnit.MINUTES));

        passwordResetTokenRepository.save(passwordResetToken);

        String resetLink = "Reset link: http://frontend/reset-password?token=" + token;

        EmailEvent emailEvent = new EmailEvent(
                email,
                "Password Reset",
                "Click here to reset password: " + resetLink
        );
        emailProducer.sendEmail(emailEvent);
//        emailService.sendEmail(email, resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (resetToken.getExpirationDate().isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Reset token expired");
        }

        Employee employee = (Employee) employeeRepository
                .findByUsername(resetToken.getUsername());
        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        //Clean up
        passwordResetTokenRepository.delete(resetToken);

        // invalidate all sessions
        refreshTokenRepository.deleteByUsername(employee.getUsername());
    }
}
