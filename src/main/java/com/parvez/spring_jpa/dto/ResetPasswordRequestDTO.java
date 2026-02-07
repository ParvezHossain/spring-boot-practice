package com.parvez.spring_jpa.dto;

public record ResetPasswordRequestDTO(String resetToken, String newPassword) {
}
