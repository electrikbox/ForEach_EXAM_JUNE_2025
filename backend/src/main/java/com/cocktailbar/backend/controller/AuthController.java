package com.cocktailbar.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.AuthService;
import com.cocktailbar.backend.DTO.LoginResponse;
import com.cocktailbar.backend.DTO.LoginUserDto;
import com.cocktailbar.backend.DTO.RegisterUserDto;
import com.cocktailbar.backend.model.Utilisateur;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody RegisterUserDto registerUserDto) {
        Utilisateur utilisateur = authService.register(registerUserDto);
        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        LoginResponse response = authService.login(loginUserDto);
        return ResponseEntity.ok(response);
    }
} 