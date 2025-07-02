package com.cocktailbar.backend.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailbar.backend.AuthService;
import com.cocktailbar.backend.DTO.LoginResponse;
import com.cocktailbar.backend.DTO.LoginUserDto;
import com.cocktailbar.backend.DTO.RegisterUserDto;
import com.cocktailbar.backend.DTO.UtilisateurDTO;
import com.cocktailbar.backend.model.Utilisateur;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UtilisateurDTO> register(@RequestBody RegisterUserDto registerUserDto) {
        Utilisateur utilisateur = authService.register(registerUserDto);
        UtilisateurDTO dto = new UtilisateurDTO(
            utilisateur.getIdUtilisateur(),
            utilisateur.getEmailUtilisateur(),
            utilisateur.getRoleUtilisateur()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        LoginResponse response = authService.login(loginUserDto);
        ResponseCookie cookie = ResponseCookie.from("token", response.getToken())
            .httpOnly(true)
            .secure(false)
            .maxAge(response.getExpiresIn())
            .path("/")
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrityViolation(RuntimeException ex) {
        return ex.getMessage();
    }
} 