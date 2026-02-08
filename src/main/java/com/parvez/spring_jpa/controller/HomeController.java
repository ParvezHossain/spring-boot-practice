package com.parvez.spring_jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping
    public String sayHello() {
        return "Hello World!";
    }
}
