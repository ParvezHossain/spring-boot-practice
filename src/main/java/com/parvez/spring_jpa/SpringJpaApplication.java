package com.parvez.spring_jpa;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Base64;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
		System.out.println("Spring project is running.....");
//		byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
//		System.out.println(Base64.getEncoder().encodeToString(key));
	}

}
