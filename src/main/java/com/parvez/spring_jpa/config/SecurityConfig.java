package com.parvez.spring_jpa.config;

import com.parvez.spring_jpa.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /* =====================  API PATH CONSTANTS  ===================== */

    private static final String[] PUBLIC_ENDPOINTS = {
            ApiPaths.LOGIN,
            ApiPaths.REGISTER,
            ApiPaths.RESET_PASSWORD,
            ApiPaths.FORGOT_PASSWORD,
    };

    private static final String[] PRIVATE_AUTH_ENDPOINTS = {
            ApiPaths.LOGOUT,
            ApiPaths.LOGOUT_ALL,
            ApiPaths.REFRESH,
    };

    /* ===================== SECURITY CONFIG ===================== */

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public APIs
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS)
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, PRIVATE_AUTH_ENDPOINTS)
                        .authenticated()

                        // HR-only APIs
                        .requestMatchers(HttpMethod.POST, ApiPaths.SALARY_INCREMENT)
                        .hasRole(Role.HR.name())

                        // HR & ADMIN read access
                        .requestMatchers(HttpMethod.GET, ApiPaths.EMPLOYEE_READ)
                        .hasAnyRole(Role.HR.name(), Role.ADMIN.name())

                        // Everything else
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /* ===================== AUTH BEANS ===================== */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
