package com.parvez.spring_jpa.config;

import com.parvez.spring_jpa.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        /* ================= PUBLIC ================= */
                        .requestMatchers(
                                ApiPaths.LOGIN,
                                ApiPaths.REGISTER,
                                ApiPaths.FORGOT_PASSWORD,
                                ApiPaths.RESET_PASSWORD,
                                ApiPaths.HOME
                        ).permitAll()


                        /* ================= PROTECTED API ================= */
                        .requestMatchers(ApiPaths.API_BASE + "/**")
                        .authenticated()

                        /* ================= EVERYTHING ELSE ================= */
                        .anyRequest()
                        .permitAll()
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );
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
