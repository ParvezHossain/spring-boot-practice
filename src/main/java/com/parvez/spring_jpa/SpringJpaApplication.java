package com.parvez.spring_jpa;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.Base64;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringJpaApplication {

    public static void main(String[] args) {

        // Looks for .env in the root folder
        Dotenv dotenv = Dotenv.load();

        String jwtSecret = dotenv.get("JWT_SECRET");

        if (jwtSecret == null) {
            byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
            System.setProperty("jwt.secret", Base64.getEncoder().encodeToString(key));
        }

        dotenv.entries().forEach((entry) ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        SpringApplication.run(SpringJpaApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("Spring project is officially ready and running!");
        };
    }

    @Bean
    @Order(2)
    public CommandLineRunner dbHealthCheck(DataSource dataSource) {
        return args -> {
            dataSource.getConnection().isValid(2);
            System.out.println("Database connection is OK!");
        };
    }
}
