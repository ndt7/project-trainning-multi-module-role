package com.example.controller;

import com.example.auth.AuthenticationService;
import com.example.dto.AuthenticationRequest;
import com.example.dto.AuthenticationResponse;
import com.example.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService service;

    @RequestMapping("/register")
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        return null;
    }

    @RequestMapping("/login")
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        return null;
    }

}
