package com.parvez.spring_jpa.repository;

import com.parvez.spring_jpa.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);

    void deleteByUsername(String username);

    void deleteByUsernameAndDeviceId(String username, String s);

    Optional<RefreshToken> findByTokenAndDeviceId(
            String token, String deviceId
    );
}
